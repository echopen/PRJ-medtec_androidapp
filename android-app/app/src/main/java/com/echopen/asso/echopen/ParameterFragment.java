package com.echopen.asso.echopen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParameterFragment extends Fragment {


    public ParameterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.parameter, container, false);

        if (v.findViewById(R.id.valid_param)!= null){
            v.findViewById(R.id.valid_param).setOnClickListener((MainActivity) getActivity());
        }

        return v;
    }

}
