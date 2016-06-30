package udenconstruction.yoobie.allTools;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import udenconstruction.yoobie.allModels.Image_stamp;
import udenconstruction.yoobie.allModels.Image;
import udenconstruction.yoobie.allModels.Member;

public class InternetService extends IntentService {

    public InternetService() {
        super("InternetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        MySQLiteHelper dbHandler = new MySQLiteHelper(this);
        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();

        List<Image_stamp> listy = dbHandler.getStamps();
        if(listy.size() > 0){
            connAndGet(gson.toJson(dbHandler.getStamps()), "http://103.18.58.26/Alpha/image-stamps/recieveStamp");
            dbHandler.clearStamp();
        }

        Calendar calendar = Calendar.getInstance();
        int tooday = calendar.get(Calendar.DAY_OF_WEEK);
        if(tooday != pref.getInt("today", 0)){
            editor.putInt("today", tooday);
            editor.commit();
            Image[] imgs = tryParse(connAndGet("blah", "http://103.18.58.26/Alpha/images/getAllImages/" + tooday));
            Bitmap bitMap;
            if(imgs != null){
                File filess = getExternalFilesDir(null);
                if (filess != null) {
                    File[] filenames = filess.listFiles();
                    for (File tmpf : filenames) {
                        tmpf.delete();
                    }
                }
                dbHandler.clearImage();

                for(Image img : imgs) {
                    try {
                        bitMap = BitmapFactory.decodeStream(new URL("http://103.18.58.26/images2/" + img.id + ".jpg").openConnection().getInputStream());
                        FileOutputStream out = new FileOutputStream(new File(getExternalFilesDir(null), img.id + ".jpg"));
                        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                        dbHandler.addImage(img);
                    } catch (Exception e) {

                    }
                }
            }
        }
        UpdateReceiver.completeWakefulIntent(intent);
    }

    public String connAndGet(String up, String urll){
        HttpURLConnection urlConnection;
        String result;
        try {
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(urll).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            //Write
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(up);
            writer.close();
            //Read
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            result = sb.toString();
            urlConnection.disconnect();
        } catch (Exception e) {
            result = "Bad server or connection, try again later.";
        }
        return result;
    }

    public Image[] tryParse(String text) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(text, Image[].class);
        } catch (Exception e) {
            return null;
        }
    }
}
