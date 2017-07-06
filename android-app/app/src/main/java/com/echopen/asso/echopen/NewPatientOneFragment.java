package com.echopen.asso.echopen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPatientOneFragment extends Fragment {


    public NewPatientOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_patient_one, container, false);
    }

    public static NewPatientOneFragment newInstance () {
        NewPatientOneFragment f = new NewPatientOneFragment();
        return f;
    }

}
