package udenconstruction.yoobie;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import udenconstruction.yoobie.allTools.MySQLiteHelper;

public class YoobieService extends Service {
    private WindowManager windowManager;
    private ImageView ads;
    private WindowManager.LayoutParams params;
    private Handler handler;
    private long tStart;
    private MySQLiteHelper dbHandler;
    private int targetImg;
    private SharedPreferences pref;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mReceiver, filter);

        pref = getSharedPreferences("app", MODE_PRIVATE);
        dbHandler = new MySQLiteHelper(this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        ads = new ImageView(this);
        ads.setScaleType(ImageView.ScaleType.FIT_XY);

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        params = new WindowManager.LayoutParams(
                metrics.widthPixels / 2,
                metrics.widthPixels / 2,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;

        handler = new Handler();
        ads.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private boolean kill;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        kill = true;
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (kill) {
                            handler.removeCallbacks(killit);
                            killit.run();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        kill = false;
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        if (ads.isShown()) windowManager.updateViewLayout(ads, params);
                        break;
                }
                return true;
            }
        });
        targetImg = dbHandler.getImage();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (pref.getInt("transparency", 0)) {
            case 0:
                params.alpha = (float) 1.0;
                break;
            case 1:
                params.alpha = (float) 0.8;
                break;
            case 2:
                params.alpha = (float) 0.6;
                break;
        }

        switch (pref.getInt("animation", 0)) {
            case 0:
                params.windowAnimations = android.R.style.Animation_Translucent;
                break;
            case 1:
                params.windowAnimations = android.R.style.Animation_Dialog;
                break;
            case 2:
                params.windowAnimations = android.R.style.Animation_Toast;
                break;
        }
        /*Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText("All set")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(null)
                .setOngoing(true)
                .build();
        startForeground(4327, notification);*/
        return START_STICKY;
    }

    private final Runnable killit = new Runnable() {
        @Override
        public void run() {
            if(!ads.isShown()) return;
            if (targetImg > 0) {
                SharedPreferences.Editor editor = pref.edit();
                float curEntry = pref.getFloat("entry", 0);
                float elapsed = (SystemClock.elapsedRealtime() - tStart) / 1000.0f;
                editor.putFloat("entry", elapsed + curEntry);
                editor.commit();
                dbHandler.addStamp(targetImg, pref.getInt("id", 0), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()), elapsed);
            }
            windowManager.removeView(ads);
            ads.setImageDrawable(null);
            targetImg = dbHandler.getImage();
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            // KeyguardManager kgMgr = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            //if (tm.getCallState() != TelephonyManager.CALL_STATE_IDLE || pref.getInt("priority", 0) == 0 && !kgMgr.inKeyguardRestrictedInputMode()) return;
            if (tm.getCallState() != TelephonyManager.CALL_STATE_IDLE || !pref.getBoolean("active", false)) return;

            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON) && pref.getInt("priority", 0) == 0 || intent.getAction().equals(Intent.ACTION_USER_PRESENT) && pref.getInt("priority", 0) == 1) {
                if (targetImg > 0) {
                    ads.setImageDrawable(Drawable.createFromPath(getExternalFilesDir(null) + "/" + targetImg + ".jpg"));
                    tStart = SystemClock.elapsedRealtime();
                } else {
                    ads.setImageResource(R.drawable.notaval);
                }
                //if (ads.isShown()) windowManager.removeView(ads);
                windowManager.addView(ads, params);
                handler.postDelayed(killit, pref.getInt("lifespan", 3) * 1000);
            }
            else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    handler.removeCallbacks(killit);
                    killit.run();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (ads.isShown()) windowManager.removeView(ads);
        handler.removeCallbacks(null);
    }
}
