package com.echopen.asso.echopen.custom;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.echopen.asso.echopen.utils.TouchEffect;

/**
 * Common class to all activities
 * setupActionBar() sets the action bar
 */
public class CustomActivity extends FragmentActivity implements OnClickListener {


    private static final TouchEffect TOUCH = new TouchEffect();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* keep the activity awake is important for the medical operator */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getActionBar() != null)
            setupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public View setTouchNClick(int id) {
        View v = setClick(id);
        v.setOnTouchListener(TOUCH);
        return v;
    }

    public View setClick(int id) {
        View v = findViewById(id);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        //todo to be implemented
    }
}
