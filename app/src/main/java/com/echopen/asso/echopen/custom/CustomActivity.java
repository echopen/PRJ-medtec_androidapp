package com.echopen.asso.echopen.custom;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.utils.TouchEffect;

/**
 * Common class to all activities
 */
public class CustomActivity extends FragmentActivity implements OnClickListener
{

    private static final String THEME = "appTheme";

    public static final int THEME_BLUE = R.drawable.theme_blue;

    protected int theme;

    public static final TouchEffect TOUCH = new TouchEffect();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        theme = getAppTheme();
        if (getActionBar() != null)
            setupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void applyBgTheme(View v)
    {
        v.setBackgroundResource(theme);
    }

    protected void setupActionBar()
    {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(theme));
    }

    protected void saveAppTheme(int theme)
    {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putInt(THEME, theme).commit();
    }

    protected int getAppTheme()
    {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt(
                THEME, THEME_BLUE);
    }

    public View setTouchNClick(int id)
    {

        View v = setClick(id);
        v.setOnTouchListener(TOUCH);
        return v;
    }


    public View setClick(int id)
    {

        View v = findViewById(id);
        v.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v)
    {
        //todo to be implemented
    }
}
