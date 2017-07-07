package com.echopen.asso.echopen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandFragment extends Fragment {


    public HandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.hand, container, false);
        if (v.findViewById(R.id.left_hand)!= null){
            v.findViewById(R.id.left_hand).setOnClickListener((MainActivity) getActivity());
        }
        if (v.findViewById(R.id.right_hand)!= null){
            v.findViewById(R.id.right_hand).setOnClickListener((MainActivity) getActivity());
        }
        if (v.findViewById(R.id.skip)!= null){
            v.findViewById(R.id.skip).setOnClickListener((MainActivity) getActivity());
        }

        return v;
    }

}
