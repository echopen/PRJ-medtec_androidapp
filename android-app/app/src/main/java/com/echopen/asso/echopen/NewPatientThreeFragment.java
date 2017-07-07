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
public class NewPatientThreeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button finishButton;
    private ImageButton maigre_button;
    private ImageButton normal_button;
    private ImageButton gros_button;
    public NewPatientThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        finishButton = (Button) getView().findViewById(R.id.finishForm);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.finishForm();
            }
        });

        maigre_button = (ImageButton) getView().findViewById(R.id.imageButtonMaigre);
        normal_button = (ImageButton) getView().findViewById(R.id.imageButtonNormal);
        gros_button = (ImageButton) getView().findViewById(R.id.imageButtonGros);
        // when you click on the Adult Button
        // ...
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_patient_three, container, false);
    }

    public static NewPatientThreeFragment newInstance () {
        NewPatientThreeFragment f = new NewPatientThreeFragment();
        return f;
    }
    public interface OnFragmentInteractionListener {
        void finishForm();
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
