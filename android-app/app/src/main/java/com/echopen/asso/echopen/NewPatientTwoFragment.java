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
public class NewPatientTwoFragment extends Fragment {

    private NewPatientOneFragment.OnFragmentInteractionListener mListener;

    public NewPatientTwoFragment() {
        // Required empty public constructor
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
