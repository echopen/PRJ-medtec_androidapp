package com.echopen.asso.echopen;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by KevinNabeth on 05/07/2017.
 */

public class DFragment extends DialogFragment {
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popup, container,
                false);
       // getDialog().setTitle("DialogFragment Tutorial");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Do something else

        return rootView;
    }
}
