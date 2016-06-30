package udenconstruction.yoobie;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import udenconstruction.yoobie.allModels.Member;
import udenconstruction.yoobie.allTools.Register;

public class RegisterActivity extends AppCompatActivity {

    private Calendar myCalendar;
    private EditText edty;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        gender = "male";
        edty = (EditText)findViewById(R.id.editRegDob);
        myCalendar = Calendar.getInstance();
        edty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        edty.setText(sdf.format(myCalendar.getTime()));
                    }
                };
                new DatePickerDialog(v.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void submit(View v) {
        EditText ed = (EditText)findViewById(R.id.editRegName);
        String usr = ed.getText().toString().trim();
        EditText ed2 = (EditText)findViewById(R.id.editRegMobile);
        String mob = ed2.getText().toString().trim();
        EditText ed5 = (EditText)findViewById(R.id.editRegDob);
        String bir = ed5.getText().toString().trim();
        EditText ed6 = (EditText)findViewById(R.id.editRegCountry);
        String loc = ed6.getText().toString().trim();
        ed.clearFocus();
        ed2.clearFocus();
        ed5.clearFocus();
        ed6.clearFocus();
        if (usr.equals("") || mob.equals("") || bir.equals("") || loc.equals("")){
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE); //1
        Member me = new Member(0, usr, pref.getString("password", "N/A"), pref.getString("email", "N/A"), gender, bir, loc, Build.MANUFACTURER + " " + android.os.Build.MODEL,mob);
        new Register(this,me).execute("http://103.18.58.26/Alpha/users/register");
    }

    public void closeup(View v){
        finish();
    }

    public void gender(View v) {
        if(v.getId() == R.id.radioButton111){
            RadioButton rad = (RadioButton)findViewById(R.id.radioButton222);
            rad.setChecked(false);
            gender = "male";
        }else{
            RadioButton rad = (RadioButton)findViewById(R.id.radioButton111);
            rad.setChecked(false);
            gender = "female";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
