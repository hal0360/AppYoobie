package udenconstruction.yoobie;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import udenconstruction.yoobie.allModels.Member;
import udenconstruction.yoobie.allTools.Login;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private MainActivity mainAct;
    private TextView warning;
    private TextView forgetPass;
    private EditText ed;
    private EditText ed2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ed = (EditText)view.findViewById(R.id.editLogMail);
        ed2 = (EditText)view.findViewById(R.id.editLogPass);
        Button b = (Button) view.findViewById(R.id.LoginButton);
        b.setOnClickListener(this);
        Button face = (Button) view.findViewById(R.id.faceBookButton);
        face.setOnClickListener(this);
        forgetPass = (TextView) view.findViewById(R.id.textForget);
        forgetPass.setOnClickListener(this);
        TextView signup = (TextView) view.findViewById(R.id.textSignup);
        signup.setOnClickListener(this);
        warning = (TextView) view.findViewById(R.id.textWarning);
        /*final EditText email = (EditText)view.findViewById(R.id.editLogMail);
        final float density = getResources().getDisplayMetrics().density;
        final Drawable drawable_mail = ResourcesCompat.getDrawable(getResources(), R.drawable.iconemail, null);
        final int width = Math.round(27*density);
        final int height = Math.round(27*density);
        drawable_mail.setBounds(0, 0, width, height);
        email.setCompoundDrawables(drawable_mail,null,null,null);*/
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
            case R.id.LoginButton:
                String usr = ed.getText().toString().trim();
                String pass = ed2.getText().toString().trim();
                ed.clearFocus();
                ed2.clearFocus();
                Member me = new Member(0, "dur", pass, usr, "dur", "dur", "dur", Build.MANUFACTURER + " " + android.os.Build.MODEL,"df");
                new Login(mainAct,me,warning,forgetPass).execute("http://103.18.58.26/Alpha/users/signin");
                break;
            case R.id.faceBookButton:
                mainAct.facebookCallback();
                break;
            case R.id.textForget:
                mainAct.forgotCallback();
                break;
            case R.id.textSignup:
                mainAct.sigupCallback();
                break;
        }
    }
}
