package com.echopen.asso.echopen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.echopen.asso.echopen.custom.CustomActivity;
import com.echopen.asso.echopen.model.Data.BitmapDisplayer;
import com.echopen.asso.echopen.model.Data.BitmapDisplayerFactory;
import com.echopen.asso.echopen.model.Synchronizer;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.ui.ConstantDialogFragment;
import com.echopen.asso.echopen.ui.FilterDialogFragment;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RulerView;
import com.echopen.asso.echopen.ui.onViewUpdateListener;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.UIParams;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.io.InputStream;

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

public class MainActivity extends CustomActivity implements AbstractActionActivity {

    static {
        // If you use OpenCV 2.4, System.loadLibrary("opencv_java")
        System.loadLibrary("opencv_java3");
    }

    /* integer constant that switch whether the photo or the video is on */
    private int display;
    /* class that deals with the view of MainActivity */
    private MainActionController mainActionController;
    public GestureDetector gesture;
    /* Ruler enables get the overview of body scales */
    public RulerView rulerView;
    /* Setter for rulerView */
    public void setRulerView(RulerView rulerView) {
        this.rulerView = rulerView;
    }
    /* constant setting the process via local, UDP or TCP */
    public static boolean LOCAL_ACQUISITION = true;

    public static boolean TCP_ACQUISITION = false;

    public static boolean UDP_ACQUISITION = false;

    /**
     * This method calls all the UI methods and then gives hand to  UDPToBitmapDisplayer class.
     * UDPToBitmapDisplayer listens to UDP data, processes them with the help of ScanConversion,
     * and then displays them.
     * Also, this method uses the Config singleton class that provides device-specific constants
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* loading config constants in singleton Class */
        Config.getInstance(this);
        Config.singletonConfig.getWidth();
        /* aiming to synchronize views : when operator drags lines to measure distance between points,
         the measure is synchronized with a TextView */
        Synchronizer.getInstance(this);

        setContentView(R.layout.activity_main);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.vMiddle);
        linearLayout.setBackgroundColor(Color.TRANSPARENT);

        initSwipeViews();
        initActionController();
        initViewComponents();
        setupContainer();

        UIParams.setParam1(Constants.SeekBarParam.SEEK_BAR_SCALE);
        UIParams.setParam2(Constants.SeekBarParam.SEEK_BAR_ROTATE);
        UIParams.setParam3(Constants.SeekBarParam.SEEK_BAR_HORIZONTAL);
        UIParams.setParam4(Constants.SeekBarParam.SEEK_BAR_VERTICAL);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                UIParams.setParam1(progress);
                Log.d("value of 1 ", String.valueOf(progress));
            }
        });
        final SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                UIParams.setParam2(progress);
                Log.d("value of 2 ", String.valueOf(progress));
            }
        });
        final SeekBar seekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                UIParams.setParam3(progress);
                Log.d("value of 3 ", String.valueOf(progress));
            }
        });
    }

    public void fetchData(BitmapDisplayerFactory bitmapDisplayerFactory) {
        OpenCVLoader.initDebug();
        try {
            BitmapDisplayer bitmapDisplayer = bitmapDisplayerFactory.populateBitmap(
            this, mainActionController, Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT);

            if (UDP_ACQUISITION) {
                bitmapDisplayer.readDataFromUDP();
            } else if (TCP_ACQUISITION) {
                bitmapDisplayer.readDataFromTCP();
            } else {
                AssetManager assetManager = getResources().getAssets();
                InputStream inputStream = assetManager.open("data/raw_data/data_phantom.csv");
                bitmapDisplayer.readDataFromFile(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * initActionController() is used to separate concerns
    * MainActionController sets the main UI elements and handles the display of the main screen picture
    * @param no params
    * */
    public void initActionController() {
        Activity activity = this;
        mainActionController = new MainActionController(activity);
    }

    /*
    * Set the clickable elements and applies theme.
    * For the moment, users can't set the theme
    * @param no param
    * */
    public void initViewComponents() {
        setTouchNClick(R.id.btnCapture);
        setTouchNClick(R.id.btnEffect);
        setTouchNClick(R.id.tabBrightness);
        setTouchNClick(R.id.tabGrid);
        setTouchNClick(R.id.tabSetting);
        setTouchNClick(R.id.tabSuffle);
        setTouchNClick(R.id.tabTime);

        setClick(R.id.btn1);
        setClick(R.id.btn2);
        setClick(R.id.btn3);
        setClick(R.id.btn4);
        setClick(R.id.btn5);

        mainActionController.setTransparentTextView();

        initProtocolChoice();
        //setClickToFilter(R.id.vMiddle);

        /* Ruler is used to show the centimetric scale of the ultrasound image */
        /*rulerView = (RulerView) findViewById(R.id.ruler);
        rulerView.setStartingPoint(70);
        rulerView.setUpdateListener(new onViewUpdateListener() {
            @Override
            public void onViewUpdate(float result) {
                *//* when needed, this function can update the View, for example to support scroll effect
                updating the view *//*
            }
        });*/
    }

    private void initProtocolChoice() {
        FragmentManager manager = getFragmentManager();
        ConstantDialogFragment constantDialogFragment = new ConstantDialogFragment();
        constantDialogFragment.show(manager, "fragment_edit_name");
    }


    /**
     * Sets a click listener on the LinearLayout that wraps the main screen picture
     *
     * @param mainframe, the LinearLayout's id that wraps the main screen picture
     */
    private void setClickToFilter(int mainframe) {
        final View mainFrame = findViewById(mainframe);
        mainFrame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
                filterDialogFragment.show(manager, "fragment_edit_name");
            }
        });
    }

    /**
     * Initiates the swipe view
     */
    private void initSwipeViews() {
        /*
        gesture = new GestureDetector(this, gestureListener);

        OnTouchListener otl = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        };
        findViewById(R.id.content_frame).setOnTouchListener(otl);
        */
    }

    /**
     * Deals with the gesture switch whether the photo or the video is chosen by the user
     * In each case, setupContainer() is called
     */
    private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
        @Override
        public boolean onFling(android.view.MotionEvent e1,
                               android.view.MotionEvent e2, float velocityX, float velocityY) {

            if (Math.abs(e1.getY() - e2.getY()) > Constants.Settings.SWIPE_MAX_OFF_PATH)
                return false;
            else {
                try {
                    if (e1.getX() - e2.getX() > Constants.Settings.SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > Constants.Settings.SWIPE_THRESHOLD_VELOCITY) {
                        if (display != Constants.Settings.DISPLAY_VIDEO) {
                            display++;
                            setupContainer();
                        }

                    } else if (e2.getX() - e1.getX() > Constants.Settings.SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > Constants.Settings.SWIPE_THRESHOLD_VELOCITY) {
                        if (display != Constants.Settings.DISPLAY_PHOTO) {
                            display--;
                            setupContainer();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

        }
    };

    /**
     * Prepare the call to setupContainer() that switch fragments  whether the photo or the video is chosen by the user
     *
     * @param display, integer constant that switch whether the photo or the video is on
     */
    public void goBackFromFragment(int display) {
        this.display = display;
        setupContainer();
    }

    /**
     * Switch fragments  whether the photo or the video is chosen by the user
     */
    private void setupContainer() {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }

        mainActionController.displayImages();

        if (display == Constants.Settings.DISPLAY_PHOTO) {
            mainActionController.displayPhoto();
        } else {
            mainActionController.displayOtherImage();
        }
    }

    /**
     * Handles clickable View that enable to choose camera or to start a new intent
     *
     * @param v, the clickable View
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /**
     * @param item, MenuItem instance
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        startActivity(new Intent(this, MainActivity.class));
    }
}