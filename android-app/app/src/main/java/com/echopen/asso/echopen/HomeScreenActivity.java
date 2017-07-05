package com.echopen.asso.echopen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.echopen.asso.echopen.fragments.DashboardFragment;
import com.echopen.asso.echopen.fragments.DocumentFragment;
import com.echopen.asso.echopen.fragments.EchoFragment;
import com.echopen.asso.echopen.fragments.HelpFragment;
import com.echopen.asso.echopen.fragments.SettingsFragment;

/**
 * Created by yanis on 04/07/2017.
 */

public class HomeScreenActivity extends AppCompatActivity{
    public FragmentManager fragmentManager;

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

        DashboardFragment dashboardFragment = new DashboardFragment();
        fragmentManager.beginTransaction().replace(R.id.pager, dashboardFragment).commit();
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
                    HelpFragment helpFragment = new HelpFragment();
                    fragmentManager.beginTransaction().replace(R.id.pager, helpFragment).addToBackStack(null).commit();
                }
                if ((x2 < x1) && ( Math.abs(x2-x1) > Math.abs(y2-y1))) {
                    Log.d("Swipe", "LEFT");
                    DocumentFragment documentFragment = new DocumentFragment();
                    fragmentManager.beginTransaction().replace(R.id.pager, documentFragment).addToBackStack(null).commit();
                }
                if ((y1 < y2) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "DOWN");
                    EchoFragment echoFragment = new EchoFragment();
                    fragmentManager.beginTransaction().replace(R.id.pager, echoFragment).addToBackStack(null).commit();
                }
                if ((y2 < y1) && ( Math.abs(y2-y1) > Math.abs(x2-x1))) {
                    Log.d("Swipe", "UP");
                    SettingsFragment settingsFragment = new SettingsFragment();
                    fragmentManager.beginTransaction().replace(R.id.pager, settingsFragment).addToBackStack(null).commit();
                }
            }
        }
        return true;
    }
}
