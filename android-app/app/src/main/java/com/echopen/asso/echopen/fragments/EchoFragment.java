package com.echopen.asso.echopen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echopen.asso.echopen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EchoFragment extends Fragment {


    public EchoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_echo, container, false);
    }

}
