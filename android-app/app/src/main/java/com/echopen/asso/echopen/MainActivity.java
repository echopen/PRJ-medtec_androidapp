package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.echopen.asso.echopen.bdd.DAOBase;
import com.echopen.asso.echopen.bdd.DatabaseHandler;
import com.echopen.asso.echopen.bdd.Image;
import com.echopen.asso.echopen.bdd.ImageDAO;
import com.echopen.asso.echopen.fragments.CaptureFragment;
import com.echopen.asso.echopen.fragments.GalleryFragment;
import com.echopen.asso.echopen.fragments.SettingsFragment;

import java.util.List;
import java.util.Vector;


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
    }


    @Override
    protected void onResume() {
        super.onResume();
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
