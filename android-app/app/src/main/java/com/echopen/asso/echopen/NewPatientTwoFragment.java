package com.echopen.asso.echopen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPatientTwoFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    private ImageButton pregnant_button;
    private ImageButton adult_button;
    public NewPatientTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        nextButton = (Button) getView().findViewById(R.id.goStep3);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goStep3();
            }
        });

        pregnant_button = (ImageButton) getView().findViewById(R.id.imageButtonWomanPregnant);
        adult_button = (ImageButton) getView().findViewById(R.id.imageButtonAdult);
        // when you click on the Adult Button
        adult_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String resource = (String) adult_button.getTag();

                if("clicked".equals(resource)){
                    adult_button.setImageResource(R.drawable.adulte);
                    adult_button.setTag("uncliked");
                }else {
                    adult_button.setImageResource(R.drawable.adulte_blue);
                    adult_button.setTag("clicked");
                }
            }
        });

        // when you click on the pregnant Button
        pregnant_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String resource = (String) pregnant_button.getTag();

                if("clicked".equals(resource)){
                    pregnant_button.setImageResource(R.drawable.pregnant);
                    pregnant_button.setTag("uncliked");
                }else {
                    pregnant_button.setImageResource(R.drawable.pregnant_blue);
                    pregnant_button.setTag("clicked");
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_patient_two, container, false);
    }

    public static NewPatientTwoFragment newInstance () {
        NewPatientTwoFragment f = new NewPatientTwoFragment();
        return f;
    }

    public interface OnFragmentInteractionListener {
        void goStep3();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString());
        }

    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }
}
