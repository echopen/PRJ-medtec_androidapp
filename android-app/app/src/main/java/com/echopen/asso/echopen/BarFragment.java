package com.echopen.asso.echopen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarFragment extends Fragment {


    public BarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_main_bar, container, false);

        if (v.findViewById(R.id.seances)!= null){
            v.findViewById(R.id.seances).setOnClickListener((MainActivity) getActivity());
        }
        if (v.findViewById(R.id.profil)!= null){
            v.findViewById(R.id.profil).setOnClickListener((MainActivity) getActivity());
        }
        if (v.findViewById(R.id.faq)!= null){
            v.findViewById(R.id.faq).setOnClickListener((MainActivity) getActivity());
        }

        return v;
    }

}
