package com.echopen.asso.echopen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.echopen.asso.echopen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private boolean modaleVisible = false;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_help, container, false);

        Button btn_login = (Button) v.findViewById(R.id.btn_login);
        final RelativeLayout modale_login = (RelativeLayout) v.findViewById(R.id.modale_connexion);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle connexion modale
                if (modaleVisible) {
                    modale_login.setVisibility(View.INVISIBLE);
                    modaleVisible = false;
                } else {
                    modale_login.setVisibility(View.VISIBLE);
                    modaleVisible = true;
                }
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

}
