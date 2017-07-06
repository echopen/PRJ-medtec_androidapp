package com.echopen.asso.echopen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPatientOneFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    public NewPatientOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        nextButton = (Button) getView().findViewById(R.id.goStep2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goStep2();
            }
        });
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

    public interface OnFragmentInteractionListener {
        void goStep2();
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
