package com.echopen.asso.echopen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class InscriptionFragment extends Fragment {


    public InscriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.inscription, container, false);

        if (v.findViewById(R.id.button_register)!= null){
            v.findViewById(R.id.button_register).setOnClickListener((MainActivity) getActivity());
        }

        return v;
    }

}
