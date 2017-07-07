package com.echopen.asso.echopen.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.R;

import static com.echopen.asso.echopen.R.string.preference_personne;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    public static final String PREFS_PERSONNE = "Preferences_user";
    public static final String PREFS_MORPHO = "Preferences_morpho";
    public static final String PREFS_ORGANE = "Preferences_organe";
    SharedPreferences sharedPreferences;

    public String preference_personne;
    public String preference_morpho;
    public String preference_organe;





    public SettingsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageButton btn_switch_to_main = (ImageButton) getView().findViewById(R.id.switch_page_settings);


        sharedPreferences = getContext().getSharedPreferences(PREFS_PERSONNE, Context.MODE_PRIVATE);


        if (sharedPreferences.contains(PREFS_PERSONNE) && sharedPreferences.contains(PREFS_MORPHO)){

            preference_personne = sharedPreferences.getString(PREFS_PERSONNE, null);
            preference_morpho = sharedPreferences.getString(PREFS_MORPHO, null);



            switch (preference_personne){
                case "woman":
                    RadioButton btn_preferences_woman = (RadioButton) getActivity().findViewById(R.id.btn_personne_woman);

                    btn_preferences_woman.setChecked(true);
                    break;
                case "baby":
                    RadioButton btn_preferences_baby = (RadioButton) getActivity().findViewById(R.id.btn_personne_baby);

                    btn_preferences_baby.setChecked(true);
                    break;

                case "man":
                    RadioButton btn_preferences_man = (RadioButton) getActivity().findViewById(R.id.btn_personne_man);

                    btn_preferences_man.setChecked(true);
                    break;
            }

            switch (preference_morpho){
                case "l":
                    RadioButton btn_preferences_l = (RadioButton) getActivity().findViewById(R.id.btn_morpho_l);

                    btn_preferences_l.setChecked(true);
                    break;
                case "m":
                    RadioButton btn_preferences_m = (RadioButton) getActivity().findViewById(R.id.btn_morpho_m);

                    btn_preferences_m.setChecked(true);
                    break;

                case "s":
                    RadioButton btn_preferences_s = (RadioButton) getActivity().findViewById(R.id.btn_morpho_s);

                    btn_preferences_s.setChecked(true);
                    break;
            }

        }




    }



}
