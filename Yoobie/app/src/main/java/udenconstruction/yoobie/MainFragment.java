package udenconstruction.yoobie;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private View thumb;
    private View thumb2;
    private MainActivity mainAct;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mainAct = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        View mySwitch = v.findViewById(R.id.mySwitch);
        mySwitch.setOnClickListener(this);
        thumb = v.findViewById(R.id.thumb);
        thumb2 = v.findViewById(R.id.thumb2);

        if(mainAct.pref.getBoolean("active", false)){
            thumb.setVisibility(View.GONE);
        }
        else{
            thumb2.setVisibility(View.GONE);
        }


        return v;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = mainAct.pref.edit();
        if(mainAct.pref.getBoolean("active", false)){
            thumb.setVisibility(View.VISIBLE);
            thumb2.setVisibility(View.GONE);
            editor.putBoolean("active", false);
        }
        else{
            thumb2.setVisibility(View.VISIBLE);
            thumb.setVisibility(View.GONE);
            editor.putBoolean("active", true);
        }
        editor.commit();
    }
}
