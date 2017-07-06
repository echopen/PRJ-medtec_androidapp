package com.echopen.asso.echopen.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.echopen.asso.echopen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener{

    private FragmentManager fragmentManager;

    private Activity activity;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();

        ((AppCompatActivity)activity).getSupportActionBar().hide();

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Button btn_quick_scan = (Button) v.findViewById(R.id.quick_scan);
        Button btn_custom_scan = (Button) v.findViewById(R.id.custom_scan);
        Button btn_help = (Button) v .findViewById(R.id.help);

        btn_quick_scan.setOnClickListener(this);
        btn_custom_scan.setOnClickListener(this);
        btn_help.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (v.getId()) {
            case R.id.quick_scan: {
                EchoFragment echoFragment = new EchoFragment();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);
                fragmentTransaction.replace(R.id.pager, echoFragment).commit();
                break;
            }
            case R.id.custom_scan: {
                EchoFragment echoFragment = new EchoFragment();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);
                fragmentTransaction.replace(R.id.pager, echoFragment).commit();
                break;
            }
            case R.id.help: {
                HelpFragment helpFragment = new HelpFragment();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.pager, helpFragment).commit();
                break;
            }
        }
    }
}
