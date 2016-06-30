package udenconstruction.yoobie;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import udenconstruction.yoobie.allTools.UpdateReceiver;

public class MainActivity extends AppCompatActivity {

    private android.support.v4.app.FragmentManager manager;
    public SharedPreferences pref;
    private Handler handler;
    private boolean stepBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, YoobieService.class));
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, new Intent("philAppStamp"), PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmUp)
        {
            UpdateReceiver ta = new UpdateReceiver();
            ta.starting(this);
        }
        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
          //  Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
          //  startActivityForResult(intent, 1234);
       // }

        stepBack = false;
        pref = getSharedPreferences("app", MODE_PRIVATE); //1
        manager = getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction transactionn = manager.beginTransaction();
        transactionn.add(R.id.lower, new DummyFragment());
        transactionn.add(R.id.upper, new DummyFragment());
        transactionn.commit();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                if (pref.getInt("id", 0) > 0) {
                    transaction.replace(R.id.lower, new MainFragment());
                } else {
                    transaction.replace(R.id.lower, new LoginFragment());
                }
                transaction.replace(R.id.upper, new UpperFragment());
                transaction.commit();
            }
        }, 4000);

        final ImageView image = (ImageView)findViewById(R.id.imageView777);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationSet s = new AnimationSet(false);
                s.addAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zooming));
                s.addAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveup));
                s.setFillAfter(true);
                image.startAnimation(s);
            }
        }, 2000);

    }

    public void replaceFrag(Fragment frag){
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.lower, frag);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pref.getBoolean("registered", false)) {
            replaceFrag(new MainFragment());
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("registered", false);
            editor.commit();
        }
    }

    public void settings(View v){
        startActivity(new Intent(this, ControlActivity.class));
    }

    public void backup(View v){
        if(stepBack){
            replaceFrag(new LoginFragment());
            stepBack = false;
        }else{
            finish();
        }
    }

    public void termCallback(){
        startActivity(new Intent(this, TermActivity.class));
    }

    public void registerCallback(){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void sigupCallback(){
        stepBack = true;
        replaceFrag(new RegisterFragment());
    }

    public void forgotCallback(){
        stepBack = true;
        replaceFrag(new ForgotFragment());
    }

    public void facebookCallback(){
        Toast.makeText(this, "Under construction", Toast.LENGTH_SHORT).show();
    }

    public void archieveCallback(View v){
        Toast.makeText(this, "Under construction", Toast.LENGTH_SHORT).show();
    }

    public void profileCallback(View v){
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        Runtime.getRuntime().gc();
        System.gc();
    }
}
