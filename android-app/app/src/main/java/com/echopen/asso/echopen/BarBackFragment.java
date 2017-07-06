package com.echopen.asso.echopen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarBackFragment extends Fragment {


    public BarBackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_main_bar_back, container, false);

        if (v.findViewById(R.id.back)!= null){
            v.findViewById(R.id.back).setOnClickListener((MainActivity) getActivity());
        }

        return v;
    }

}
