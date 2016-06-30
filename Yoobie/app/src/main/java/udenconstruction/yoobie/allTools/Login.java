package udenconstruction.yoobie.allTools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

import udenconstruction.yoobie.MainActivity;
import udenconstruction.yoobie.MainFragment;
import udenconstruction.yoobie.allModels.Member;

public class Login extends AsyncTask<String,String,String>
{
    private MainActivity mContext;
    private TextView tti;
    private TextView ttii;
    private Member me;
    private ProgressDialog mDialog;

    public Login(MainActivity act, Member mem, TextView ttv, TextView ttvy) {
        mContext = act;
        tti = ttv;
        me = mem;
        ttii = ttvy;
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
            editor.putString("email", myId.email);
            editor.putString("dob", myId.date_of_birth);
            editor.putString("location", myId.location);
            editor.putString("mobile", myId.mobile);
            editor.putString("gender", myId.gender);
            editor.putBoolean("active", true);
            editor.commit();
            mContext.startService(new Intent(mContext, InternetService.class));
            mContext.replaceFrag(new MainFragment());
        }
        else{
            tti.setText(result);
            ttii.setVisibility(View.VISIBLE);
        }
        mDialog.dismiss();
    }
}
