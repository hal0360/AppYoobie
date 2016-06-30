package udenconstruction.yoobie;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ForgotFragment extends Fragment implements View.OnClickListener {

    private MainActivity mainAct;
    private TextView warning;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mainAct = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot, container, false);
        warning = (TextView) view.findViewById(R.id.resetWarn);
        TextView signup = (TextView) view.findViewById(R.id.textResetSignup);
        signup.setOnClickListener(this);
        Button b = (Button) view.findViewById(R.id.resetButton);
        b.setOnClickListener(this);
        Button face = (Button) view.findViewById(R.id.faceBookButton3);
        face.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetButton:
                warning.setText("Your password has been reset. Check you email for a new password");
                break;
            case R.id.faceBookButton3:
                mainAct.facebookCallback();
                break;
            case R.id.textResetSignup:
                mainAct.sigupCallback();
                break;
        }
    }
}
