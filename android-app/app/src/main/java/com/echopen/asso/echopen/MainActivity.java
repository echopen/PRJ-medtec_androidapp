package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.utils.Constants;

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

public class MainActivity extends AppCompatActivity implements EchographyImageVisualisationContract.View, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EchographyImageStreamingService mEchographyImageStreamingService;
    private EchographyImageVisualisationContract.Presenter mEchographyImageVisualisationPresenter;
    private ImageView mCaptureButton;
    private ImageView mPregnantWomanButton;
    private ImageView mEndExamButton;
    private ImageView mBatteryButton;
    private ImageView mSelectButton;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;


    private final static float IMAGE_ZOOM_FACTOR = 1.75f;
    private final static float IMAGE_ROTATION_FACTOR = 90.f;
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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mEchographyImageStreamingService = ((EchOpenApplication) getApplication()).getEchographyImageStreamingService();
        mEchographyImageStreamingService.connect(new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT), this);

        this.setPresenter(new EchographyImageVisualisationPresenter(mEchographyImageStreamingService, this));

        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        mCaptureButton = (ImageView) findViewById(R.id.main_button_capture);
        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEchographyImageVisualisationPresenter.toggleFreeze();
            }
        });

        mPregnantWomanButton = (ImageView) findViewById(R.id.main_button_mode);
        mPregnantWomanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("pregnantButton", "PregnantWomanButton Pressed");
            }
        });

        mSelectButton = (ImageView) findViewById(R.id.main_button_select);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("selectButton", "SelectButton Pressed");
            }
        });

        mEndExamButton = (ImageView) findViewById(R.id.main_button_end_exam);
        mEndExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("endExamButton", "EndExamButton Pressed");
            }
        });

        mBatteryButton = (ImageView) findViewById(R.id.main_button_battery);
        mBatteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("batteryButton", "BatteryButton Pressed");
            }
        });


        mEchographyImageStreamingService.getRenderingContextController().setLinearLutSlope(RenderingContext.DEFAULT_LUT_SLOPE);
        mEchographyImageStreamingService.getRenderingContextController().setLinearLutOffset(RenderingContext.DEFAULT_LUT_OFFSET);
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
    public void refreshImage(final Bitmap iBitmap) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "refreshImage");
                ImageView lEchOpenImage = (ImageView) findViewById(R.id.echopenImage);
                lEchOpenImage.setRotation(IMAGE_ROTATION_FACTOR);
                lEchOpenImage.setScaleX(IMAGE_ZOOM_FACTOR);
                lEchOpenImage.setScaleY(IMAGE_ZOOM_FACTOR);
                lEchOpenImage.setImageBitmap(iBitmap);
            }
        });
    }

    @Override
    public void displayFreezeButton() {
        mCaptureButton.setImageResource(R.drawable.btn_capture_freeze);
    }

    @Override
    public void displayUnfreezeButton() {
        mCaptureButton.setImageResource(R.drawable.btn_capture);
    }

    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter iPresenter) {
        mEchographyImageVisualisationPresenter = iPresenter;
        mEchographyImageVisualisationPresenter.start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}