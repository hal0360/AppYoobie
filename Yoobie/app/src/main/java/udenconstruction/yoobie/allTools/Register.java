package udenconstruction.yoobie.allTools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import udenconstruction.yoobie.RegisterActivity;
import udenconstruction.yoobie.allModels.Member;

/**
 * Created by user on 28/06/2016.
 */
public class Register extends AsyncTask<String,String,String>
{
    private RegisterActivity mContext;
    private Member me;
    private ProgressDialog mDialog;

    public Register(RegisterActivity act, Member mem) {
        mContext = act;
        me = mem;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait...");
        mDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        InternetService inServe = new InternetService();
        Gson gson = new Gson();
        return inServe.connAndGet(gson.toJson(me), params[0]);
    }

    private Member tryParse(String text) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(text, Member.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Member myId = tryParse(result);
        if(myId != null){
            SharedPreferences settings;
            SharedPreferences.Editor editor;
            settings = mContext.getSharedPreferences("app", Context.MODE_PRIVATE); //1
            editor = settings.edit();
            editor.putInt("id", myId.id);
            editor.putString("name", myId.username);
            editor.putString("gender", myId.gender);
            editor.putString("mobile", myId.mobile);
            editor.putString("location", myId.location);
            editor.putString("dob", myId.date_of_birth);
            editor.putBoolean("active", true);
            editor.putBoolean("registered", true);
            editor.commit();
            mContext.startService(new Intent(mContext, InternetService.class));
            mContext.finish();
        }
        else{
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        }
        mDialog.dismiss();
    }
}
