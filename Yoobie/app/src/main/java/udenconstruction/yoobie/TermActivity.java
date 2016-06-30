package udenconstruction.yoobie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class TermActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.8f;
        params.dimAmount = 0.0f;
        getWindow().setAttributes(params);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getWindow().setLayout((int) (metrics.widthPixels * .8), (int) (metrics.heightPixels * .75));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event ) {
        finish();
        return super.onTouchEvent ( event );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
