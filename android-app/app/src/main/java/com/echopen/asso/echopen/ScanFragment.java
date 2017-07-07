package com.echopen.asso.echopen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment {


    public ScanFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.scan, container, false);

        if (v.findViewById(R.id.gallery) != null) {
            v.findViewById(R.id.gallery).setOnClickListener((MainActivity) getActivity());
        }
        if (v.findViewById(R.id.button_seances) != null) {
            v.findViewById(R.id.button_seances).setOnClickListener((MainActivity) getActivity());
        }
        return v;
    }
}