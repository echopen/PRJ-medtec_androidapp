package com.echopen.asso.echopen;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

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

        final ImageButton male = (ImageButton) view.findViewById(R.id.male);
        final ImageButton female = (ImageButton) view.findViewById(R.id.female);
        male.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                male.setBackgroundColor(Color.parseColor("#4CB8FB"));
                female.setBackgroundColor(Color.parseColor("#FFFFFF"));
                return true;
            }
        });
        female.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                female.setBackgroundColor(Color.parseColor("#4CB8FB"));
                male.setBackgroundColor(Color.parseColor("#FFFFFF"));
                return true;
            }
        });

        final ImageButton baby = (ImageButton) view.findViewById(R.id.baby);
        final ImageButton child = (ImageButton) view.findViewById(R.id.child);
        final ImageButton adult = (ImageButton) view.findViewById(R.id.adult);
        baby.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                baby.setBackgroundColor(Color.parseColor("#4CB8FB"));
                child.setBackgroundColor(Color.parseColor("#FFFFFF"));
                adult.setBackgroundColor(Color.parseColor("#FFFFFF"));
                return true;
            }
        });
        child.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                baby.setBackgroundColor(Color.parseColor("#FFFFFF"));
                child.setBackgroundColor(Color.parseColor("#4CB8FB"));
                adult.setBackgroundColor(Color.parseColor("#FFFFFF"));
                return true;
            }
        });
        adult.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                baby.setBackgroundColor(Color.parseColor("#FFFFFF"));
                child.setBackgroundColor(Color.parseColor("#FFFFFF"));
                adult.setBackgroundColor(Color.parseColor("#4CB8FB"));
                return true;
            }
        });


        final ImageButton thin = (ImageButton) view.findViewById(R.id.thin);
        final ImageButton normal = (ImageButton) view.findViewById(R.id.normal);
        final ImageButton big = (ImageButton) view.findViewById(R.id.big);
        thin.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                thin.setBackgroundColor(Color.parseColor("#4CB8FB"));
                normal.setBackgroundColor(Color.parseColor("#FFFFFF"));
                big.setBackgroundColor(Color.parseColor("#FFFFFF"));
                return true;
            }
        });
        normal.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                thin.setBackgroundColor(Color.parseColor("#FFFFFF"));
                normal.setBackgroundColor(Color.parseColor("#4CB8FB"));
                big.setBackgroundColor(Color.parseColor("#FFFFFF"));
                return true;
            }
        });
        big.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                thin.setBackgroundColor(Color.parseColor("#FFFFFF"));
                normal.setBackgroundColor(Color.parseColor("#FFFFFF"));
                big.setBackgroundColor(Color.parseColor("#4CB8FB"));
                return true;
            }
        });


        return  view;

    }

}