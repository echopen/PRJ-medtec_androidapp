package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.echopen.asso.echopen.bdd.DAOBase;
import com.echopen.asso.echopen.bdd.DatabaseHandler;
import com.echopen.asso.echopen.bdd.Image;
import com.echopen.asso.echopen.bdd.ImageDAO;
import com.echopen.asso.echopen.fragments.CaptureFragment;
import com.echopen.asso.echopen.fragments.GalleryFragment;
import com.echopen.asso.echopen.fragments.SettingsFragment;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import static android.R.attr.checked;


/**
 * MainActivity class handles the main screen of the app.
 * Tools are called in the following order :
 * - initSwipeViews() handles the gesture tricks via GestureDetector class
 * - initViewComponents() mainly sets the clickable elements
 * - initActionController() and setupContainer() : in order to separate concerns, View parts are handled by the initActionController()
 * method which calls the MainActionController class that deals with MainActivity Views,
 * especially handles the display of the main screen picture
 * These two methods should be refactored into one
 */

public class MainActivity extends FragmentActivity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;


    public static final String PREFS_PERSONNE = "Preferences_user";
    public static final String PREFS_MORPHO = "Preferences_morpho";
    public static final String PREFS_ORGANES = "Preferences_organes";


    SharedPreferences sharedPreferences;

    public String preference_personne;
    public String preference_morphologie;
    public String preference_organes;


    /**
     * This method calls all the UI methods and then gives hand to  UDPToBitmapDisplayer class.
     * UDPToBitmapDisplayer listens to UDP data, processes them with the help of ScanConversion,
     * and then displays them.
     * Also, this method uses the Config singleton class that provides device-specific constants
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List fragments = new Vector<>();

        fragments.add(Fragment.instantiate(this, SettingsFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, CaptureFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, GalleryFragment.class.getName()));

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);

        //mPager.setCurrentItem(1);


        //sauvegarde préférences




        sharedPreferences = getBaseContext().getSharedPreferences(PREFS_PERSONNE, MODE_PRIVATE);


        if (sharedPreferences.contains(PREFS_PERSONNE)){

            preference_personne = sharedPreferences.getString(PREFS_PERSONNE, null);



            Log.d("pref", preference_personne);
        }






    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void goToMainPage(View view) {


        mPager.setCurrentItem(1);

    }

    public void setPersonnePreference(View view) {


        boolean checked = ((RadioButton) view).isChecked();


        switch (view.getId()) {
            case R.id.btn_personne_man:
                if (checked)
                // Pirates are the best
                {

                    sharedPreferences
                            .edit()
                            .putString(PREFS_PERSONNE, "man")
                            .apply();


                    break;
                }
            case R.id.btn_personne_woman:
                if (checked)
                // Ninjas rule
                {

                    sharedPreferences
                            .edit()
                            .putString(PREFS_PERSONNE, "woman")
                            .apply();


                    break;
                }
            case R.id.btn_personne_baby:
                if (checked) {


                    sharedPreferences
                            .edit()
                            .putString(PREFS_PERSONNE, "baby")
                            .apply();

                    break;
                }

        }

    }

    public void setMorphoPreference(View view) {


        boolean checked = ((RadioButton) view).isChecked();


        switch (view.getId()) {
            case R.id.btn_morpho_l:
                if (checked)
                {
                    sharedPreferences
                            .edit()
                            .putString(PREFS_MORPHO, "l")
                            .apply();
                    break;
                }
            case R.id.btn_morpho_s:
                if (checked)
                {

                    sharedPreferences
                            .edit()
                            .putString(PREFS_MORPHO, "s")
                            .apply();


                    break;
                }
            case R.id.btn_morpho_m:
                if (checked) {
                    sharedPreferences
                            .edit()
                            .putString(PREFS_MORPHO, "m")
                            .apply();

                    break;
                }

        }

    }

    public void setOrganePreference(View view) {


        switch (view.getId()) {
            case R.id.btn_organe_coeur:
                Log.d("LOG", "coeur");


                sharedPreferences
                        .edit()
                        .putString(PREFS_ORGANES, "coeur")
                        .apply();


                break;
            case R.id.btn_organe_ovaire:
                sharedPreferences
                        .edit()
                        .putString(PREFS_ORGANES, "ovaire")
                        .apply();

                Log.d("LOG", "ovaire");
                break;
            case R.id.btn_organe_ventre:

                sharedPreferences
                        .edit()
                        .putString(PREFS_ORGANES, "ventre")
                        .apply();

                Log.d("LOG", "ventre");
                break;
            case R.id.btn_organe_poumon:

                sharedPreferences
                        .edit()
                        .putString(PREFS_ORGANES, "poumon")
                        .apply();

                Log.d("LOG", "poumon");
                break;
        }


    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class MainPageAdapter extends FragmentStatePagerAdapter {
        private final List fragments;

        public MainPageAdapter(FragmentManager fm, List fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }


}
