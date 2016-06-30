package udenconstruction.yoobie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private MainActivity mainAct;
    private EditText ed;
    private EditText ed2;
    private EditText ed3;
    private CheckBox terms;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ed = (EditText)view.findViewById(R.id.editRegMail);
        ed2 = (EditText)view.findViewById(R.id.editRegPass);
        ed3 = (EditText)view.findViewById(R.id.editRegPass2);
        terms = (CheckBox) view.findViewById(R.id.termCond);
        terms.setOnClickListener(this);
        Button face = (Button) view.findViewById(R.id.faceBookButton2);
        face.setOnClickListener(this);
        Button account = (Button) view.findViewById(R.id.AccountButton);
        account.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mainAct = (MainActivity)activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.termCond:
                if(terms.isChecked()) mainAct.termCallback();
                break;
            case R.id.faceBookButton2:
                Toast.makeText(mainAct, "Under construction", Toast.LENGTH_SHORT).show();
                break;
            case R.id.AccountButton:
                String usr = ed.getText().toString().trim();
                String pass = ed2.getText().toString().trim();
                String pass2 = ed3.getText().toString().trim();
                ed.clearFocus();
                ed2.clearFocus();
                ed3.clearFocus();
                if (usr.equals("") || pass.equals("") || !terms.isChecked()){
                    Toast.makeText(mainAct, "Please fill all fields and agree to terms and conditions", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(pass2)){
                    Toast.makeText(mainAct, "Password not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = mainAct.pref.edit();
                editor.putString("email", usr);
                editor.putString("password", pass);
                editor.commit();
                mainAct.registerCallback();
                break;
        }
    }
}
