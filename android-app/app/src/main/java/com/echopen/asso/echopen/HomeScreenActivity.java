package com.echopen.asso.echopen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echopen.asso.echopen.fragments.DashboardFragment;
import com.echopen.asso.echopen.fragments.DocumentFragment;
import com.echopen.asso.echopen.fragments.EchoFragment;
import com.echopen.asso.echopen.fragments.HelpFragment;
import com.echopen.asso.echopen.fragments.SettingsFragment;

import java.util.Date;

// Import echopen
// Probe import

public class HomeScreenActivity extends AppCompatActivity{
    public FragmentManager fragmentManager;

    private boolean center;
    private boolean top;
    private boolean bottom;
    private boolean left;
    private boolean right;

    private boolean action;
    private float x1;
    private float x2;

    private float y1;
    private float y2;

    private boolean burstFireFolderCreated = false;

    private Date burstFireFolderDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        fragmentManager = getSupportFragmentManager();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        DashboardFragment dashboardFragment = new DashboardFragment();
        fragmentManager.beginTransaction().replace(R.id.pager, dashboardFragment).commit();

        right = false;
        left = false;
        bottom = false;
        center = true;
        top = false;
        getSupportActionBar().hide();
    }
    
    @Override
    public void onBackPressed(){
        DashboardFragment dashboardFragment = new DashboardFragment();
        fragmentManager.beginTransaction().replace(R.id.pager, dashboardFragment).commit();
        bottom = false;
        top = false;
        center = true;
        action = false;
        left = false;
        right = false;
    }
  
    /**
     * Swipe motion event
     * @param touchevent
     * @return
     */
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                action = true;
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                Log.d("swipe", "swipe");
                if ( !left && center && action && (x1 < x2) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "RIGHT");
                    HelpFragment helpFragment = new HelpFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.pager, helpFragment).addToBackStack(null).commit();
                    center = false;
                    action = false;
                    left = true;
                    top = false;
                    right = false;
                    bottom = false;

                    break;
                } else if( right && !center && action && (x1 < x2) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "RIGHT");
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.pager, dashboardFragment).addToBackStack(null).commit();
                    center = true;
                    action = false;
                    left = false;
                    right = false;
                    bottom = false;
                    top = false;
                    break;
                }
                if ( !right && center && action && (x2 < x1) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "LEFT");
                    DocumentFragment documentFragment = new DocumentFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    fragmentTransaction.replace(R.id.pager, documentFragment).addToBackStack(null).commit();

                    left = false;
                    right = true;
                    center = false;
                    action = false;
                    top = false;
                    bottom = false;
                    break;
                } else if ( left && !center && action && (x2 < x1) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "LEFT");
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    fragmentTransaction.replace(R.id.pager, dashboardFragment).addToBackStack(null).commit();

                    left = false;
                    right = false;
                    center = true;
                    action = false;
                    top = false;
                    bottom = false;
                    break;
                }
                if ( !top && center && action && (y1 < y2) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "DOWN");
                    EchoFragment echoFragment = new EchoFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);
                    fragmentTransaction.replace(R.id.pager, echoFragment).addToBackStack(null).commit();

                    bottom = false;
                    top = true;
                    center = false;
                    action = false;
                    left = false;
                    right = false;
                    break;
                } else if ( bottom && !center && action && (y1 < y2) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "DOWN");
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);
                    fragmentTransaction.replace(R.id.pager, dashboardFragment).addToBackStack(null).commit();

                    bottom = false;
                    center = true;
                    action = false;
                    top = false;
                    left = false;
                    right = false;
                    break;
                }
                if ( !bottom && center && action && (y2 < y1) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "UP");
                    SettingsFragment settingsFragment = new SettingsFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
                    fragmentTransaction.replace(R.id.pager, settingsFragment).addToBackStack(null).commit();

                    top = false;
                    bottom = true;
                    center = false;
                    action = false;
                    left = false;
                    right = false;
                    break;
                } else if ( top && !center && action && (y2 < y1) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "UP");
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
                    fragmentTransaction.replace(R.id.pager, dashboardFragment).addToBackStack(null).commit();

                    top = false;
                    bottom = false;
                    center = true;
                    action = false;
                    left = false;
                    right = false;
                    break;
                }

            }
        }
        return false;
    }
}
