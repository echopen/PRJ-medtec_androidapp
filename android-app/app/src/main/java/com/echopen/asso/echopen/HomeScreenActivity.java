package com.echopen.asso.echopen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

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

        getSupportActionBar().hide();
    }
    
    @Override
    public void onBackPressed(){
        DashboardFragment dashboardFragment = new DashboardFragment();
        fragmentManager.beginTransaction().replace(R.id.pager, dashboardFragment).commit();
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

                if ( action && (x1 < x2) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "RIGHT");
                    HelpFragment helpFragment = new HelpFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.pager, helpFragment).addToBackStack(null).commit();
                    touchevent.setAction(MotionEvent.ACTION_CANCEL);

                    action = false;
                    break;
                }
                if ( action && (x2 < x1) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "LEFT");
                    DocumentFragment documentFragment = new DocumentFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    fragmentTransaction.replace(R.id.pager, documentFragment).addToBackStack(null).commit();

                    action = false;
                    break;
                }
                if ( action && (y1 < y2) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "DOWN");
                    EchoFragment echoFragment = new EchoFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);
                    fragmentTransaction.replace(R.id.pager, echoFragment).addToBackStack(null).commit();

                    action = false;
                    break;
                }
                if ( action && (y2 < y1) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "UP");
                    SettingsFragment settingsFragment = new SettingsFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
                    fragmentTransaction.replace(R.id.pager, settingsFragment).addToBackStack(null).commit();

                    action = false;
                    break;
                }

            }
        }
        return true;
    }
}
