package udenconstruction.yoobie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ControlActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
        editor = pref.edit();
        RadioButton r1 = (RadioButton)findViewById(R.id.radioButton1);
        RadioButton r2 = (RadioButton)findViewById(R.id.radioButton2);
        RadioButton r3 = (RadioButton)findViewById(R.id.radioButton3);
        RadioButton r4 = (RadioButton)findViewById(R.id.radioButton4);
        RadioButton r5 = (RadioButton)findViewById(R.id.radioButton5);
        RadioButton r6 = (RadioButton)findViewById(R.id.radioButton6);
        RadioButton r7 = (RadioButton)findViewById(R.id.radioButton7);
        RadioButton r8 = (RadioButton)findViewById(R.id.radioButton8);
        RadioButton r9 = (RadioButton)findViewById(R.id.radioButton9);
        RadioButton r10 = (RadioButton)findViewById(R.id.radioButton10);
        RadioButton r11 = (RadioButton)findViewById(R.id.radioButton11);

        if(pref.getInt("transparency", 0) == 0){
            r4.setChecked(true);
        }
        else if(pref.getInt("transparency", 0) == 1){
            r5.setChecked(true);
        }
        else {
            r6.setChecked(true);
        }

        if(pref.getInt("animation", 0) == 0){
            r1.setChecked(true);
        }
        else if(pref.getInt("animation", 0) == 1){
            r2.setChecked(true);
        }
        else{
            r3.setChecked(true);
        }

        if(pref.getInt("lifespan", 3) == 2){
            r7.setChecked(true);
        }
        else if(pref.getInt("lifespan", 3) == 3){
            r8.setChecked(true);
        }
        else{
            r9.setChecked(true);
        }

        if(pref.getInt("priority", 0) == 0){
            r10.setChecked(true);
        }
        else{
            r11.setChecked(true);
        }

        RadioGroup.OnCheckedChangeListener RadioOnclic = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButton1:
                        editor.putInt("animation", 0);
                        break;
                    case R.id.radioButton2:
                        editor.putInt("animation", 1);
                        break;
                    case R.id.radioButton3:
                        editor.putInt("animation", 2);
                        break;
                    case R.id.radioButton4:
                        editor.putInt("transparency", 0);
                        break;
                    case R.id.radioButton5:
                        editor.putInt("transparency", 1);
                        break;
                    case R.id.radioButton6:
                        editor.putInt("transparency", 2);
                        break;
                    case R.id.radioButton7:
                        editor.putInt("lifespan", 2);
                        break;
                    case R.id.radioButton8:
                        editor.putInt("lifespan", 3);
                        break;
                    case R.id.radioButton9:
                        editor.putInt("lifespan", 4);
                        break;
                    case R.id.radioButton10:
                        editor.putInt("priority", 0);
                        break;
                    case R.id.radioButton11:
                        editor.putInt("priority", 1);
                        break;
                }
                editor.commit();
            }
        };

        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup);
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
        RadioGroup radioGroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
        radioGroup1.setOnCheckedChangeListener(RadioOnclic);
        radioGroup2.setOnCheckedChangeListener(RadioOnclic);
        radioGroup3.setOnCheckedChangeListener(RadioOnclic);
        radioGroup4.setOnCheckedChangeListener(RadioOnclic);
    }

    public void goback(View view) {
        startService(new Intent(this, YoobieService.class));
        finish();
    }
}
