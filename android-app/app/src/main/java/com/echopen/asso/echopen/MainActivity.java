package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-----------------------------------------------------------------

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ArchivesFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = ScannerFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = PatientFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ScannerFragment.newInstance());
        transaction.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    boolean firstTouch = false;
    long time = System.currentTimeMillis();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.tapOnView(event.getAction());
        return true;
    }

    public boolean tapOnView(int ev) {
        if (ev == MotionEvent.ACTION_DOWN) {

            if(firstTouch && (System.currentTimeMillis() - time) <= 400) {
                //set action to write annotations
                Log.d("tap on view"," second tap ");
                firstTouch = false;

                return false;

            } else {
                firstTouch = true;
                time = System.currentTimeMillis();
                Log.d("tap on view"," First Tap time  "+time);

                return false;
            }
        }
        return true;
    }

    /**
         * Following the doc https://developer.android.com/intl/ko/training/basics/intents/result.html,
         * onActivityResult is “Called when an activity you launched exits, giving you the requestCode you started it with,
         * the resultCode it returned, and any additional data from it.”,
         * See more here : https://stackoverflow.com/questions/20114485/use-onactivityresult-android
         *
         * @param requestCode, integer argument that identifies your request
         * @param resultCode, to get its values, check RESULT_CANCELED, RESULT_OK here https://developer.android.com/reference/android/app/Activity.html#RESULT_OK
         * @param data,       Intent instance
         */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.tapOnView (motionEvent.getAction());
        return false;
    }
}
