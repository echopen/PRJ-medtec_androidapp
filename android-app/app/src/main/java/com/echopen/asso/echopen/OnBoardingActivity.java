package com.echopen.asso.echopen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.echopen.asso.echopen.fragments.onboarding.Step01Fragment;
import com.echopen.asso.echopen.fragments.onboarding.Step02Fragment;
import com.echopen.asso.echopen.fragments.onboarding.Step03Fragment;

import java.util.List;
import java.util.Vector;

public class OnBoardingActivity extends FragmentActivity {
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_on_boarding);

        List fragments = new Vector<>();

        fragments.add(Fragment.instantiate(this, Step01Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Step02Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Step03Fragment.class.getName()));

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);
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
