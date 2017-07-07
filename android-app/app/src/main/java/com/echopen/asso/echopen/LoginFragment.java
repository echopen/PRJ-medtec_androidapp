package com.echopen.asso.echopen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.home, container, false);

        if (v.findViewById(R.id.button_connexion)!= null){
            v.findViewById(R.id.button_connexion).setOnClickListener((MainActivity) getActivity());
        }
        if (v.findViewById(R.id.button_inscription)!= null){
            v.findViewById(R.id.button_inscription).setOnClickListener((MainActivity) getActivity());
        }
        if (v.findViewById(R.id.button_scan)!= null){
            v.findViewById(R.id.button_scan).setOnClickListener((MainActivity) getActivity());
        }

        return v;
    }
}
