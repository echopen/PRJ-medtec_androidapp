package com.echopen.asso.echopen;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PatientFragment extends Fragment  {
    public static PatientFragment newInstance() {
        PatientFragment fragment = new PatientFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient, container, false);

        //listener on "valider" button to redirect towards scanner
        Button valid = (Button) view.findViewById(R.id.Valid);
        valid.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, ScannerFragment.newInstance());
                transaction.commit();
                return true;
            }


        });
        return  view;
    }
}
