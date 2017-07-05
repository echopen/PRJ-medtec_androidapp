package com.echopen.asso.echopen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.echopen.asso.echopen.fragments.ScreenSlidePageFragment;

// Probe imports

/**
 * Created by yanis on 04/07/2017.
 */

public class HomeScreenActivity extends AppCompatActivity{
    private static final int NUM_PAGES = 5;
    public FragmentManager fragmentManager;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private float x1;
    private float x2;

    private float y1;
    private float y2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        fragmentManager = getSupportFragmentManager();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSliderPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    /**
     * Start home features
     */
    private void startHome()
    {

    }

    public boolean onTouchEvent(MotionEvent touchevent) {

        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                if ((x1 < x2) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "RIGHT");
                }
                if ((x2 < x1) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "LEFT");
                }
                if ((y1 < y2) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "DOWN");
                }
                if ((y2 < y1) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "UP");
                    /**
                    SettingsFragment settingsFragment = new SettingsFragment();
                    fragmentManager.beginTransaction().replace(R.id.pager, settingsFragment).addToBackStack(null).commit();
                     */
                }
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    private class ScreenSliderPagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSliderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
