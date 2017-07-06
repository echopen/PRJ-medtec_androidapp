package com.echopen.asso.echopen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPatientThreeFragment extends Fragment {

    private NewPatientOneFragment.OnFragmentInteractionListener mListener;
    public NewPatientThreeFragment() {
        // Required empty public constructor
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
        if (context instanceof NewPatientOneFragment.OnFragmentInteractionListener) {
            mListener = (NewPatientOneFragment.OnFragmentInteractionListener) context;
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
