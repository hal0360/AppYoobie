package udenconstruction.yoobie;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE); //1

        TextView ttt = (TextView) findViewById(R.id.profileEmailText);
        ttt.setText(pref.getString("email", "N/A"));
        ttt = (TextView) findViewById(R.id.profileName);
        ttt.setText(pref.getString("name", "N/A"));
        ttt = (TextView) findViewById(R.id.profileMobile);
        ttt.setText(pref.getString("mobile", "N/A"));
        ttt = (TextView) findViewById(R.id.profileDob);
        ttt.setText(pref.getString("dob", "N/A"));
        ttt = (TextView) findViewById(R.id.profileLocation);
        ttt.setText(pref.getString("location", "N/A"));
        ttt = (TextView) findViewById(R.id.profileGender);
        ttt.setText(pref.getString("gender", "N/A"));
    }

    public void backToMenu(View v){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
