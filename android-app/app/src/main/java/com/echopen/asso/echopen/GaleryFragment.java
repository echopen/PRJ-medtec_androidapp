package com.echopen.asso.echopen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class GaleryFragment extends Fragment {


    public GaleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_galery, container, false);

        Button closeBtn = (Button) view.findViewById(R.id.close_fragment_galery);



        closeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag" , "close fragment galery !!");
                // close fragment
                getActivity().getSupportFragmentManager().beginTransaction().remove(GaleryFragment.this).commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
