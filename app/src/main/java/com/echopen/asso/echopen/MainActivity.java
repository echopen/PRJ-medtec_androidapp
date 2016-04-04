package com.echopen.asso.echopen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.echopen.asso.echopen.model.Data.BitmapDisplayer;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.custom.CustomActivity;
import com.echopen.asso.echopen.ui.ConstantDialogFragment;
import com.echopen.asso.echopen.ui.FilterDialogFragment;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.Constants;

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

    /* integer constant that switch wether the photo or the video is on */
    private int display;

    /* class that deals with the view of MainActivity */
    private MainActionController mainActionController;

    public GestureDetector gesture;

    /* main UI constants of the app */
    public Constants.Settings setting;

    /* constant setting the process via UDP or TCP - @todo : the user should choose the desired way -
     should implement a dedicated check button */

    public static boolean  LOCAL_ACQUISITION = true;

    public static boolean TCP_ACQUISITION = false;

    public static boolean UDP_ACQUISITION = false;

    /** locator of the screenshots or - the runcamera() method that processes it is currently unused
    * for the moment - but it will be plugged again later in the developement
    */
    protected Uri uri;

    /** This method calls all the UI methods and then gives hand to  UDPToBitmapDisplayer class.
    * UDPToBitmapDisplayer listens to UDP data, processes them with the help of ScanConversion,
    * and then displays them.
    * Also, this method uses the Config singleton class that provides device-specific constants
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.vMiddle);
        linearLayout.setBackgroundColor(Color.TRANSPARENT);

        initSwipeViews();
        initViewComponents();
        initActionController();
        setupContainer();

        Config.getInstance(this);

        try {
            BitmapDisplayer bitmapDisplayer = new BitmapDisplayer(this, mainActionController, Constants.Http.REDPITAYA_UDP_IP, Constants.Http.REDPITAYA_UDP_PORT);

            if (UDP_ACQUISITION) {
                bitmapDisplayer.readDataFromUDP();
            }
            else if(TCP_ACQUISITION) {
                bitmapDisplayer.readDataFromTCP();
            }
            else {
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
    private void initViewComponents() {
        setTouchNClick(R.id.btnCapture);
        setTouchNClick(R.id.btnEffect);
        setTouchNClick(R.id.btnPic);
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

        initProtocolChoice();
        setClickToFilter(R.id.vMiddle);

        applyBgTheme(findViewById(R.id.vTop));
        applyBgTheme(findViewById(R.id.vBottom));
    }

    private void initProtocolChoice() {
        FragmentManager manager = getFragmentManager();
        ConstantDialogFragment constantDialogFragment = new ConstantDialogFragment();
        constantDialogFragment.show(manager, "fragment_edit_name");
    }


    /**
     * Sets a click listener on the LinearLayout that wraps the main screen picture
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
     * Initiates the swip view
     */
    private void initSwipeViews() {
        gesture = new GestureDetector(this, gestureListner);

        OnTouchListener otl = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        };
        findViewById(R.id.content_frame).setOnTouchListener(otl);
    }

    /**
     * Deals with the gesture switch wether the photo or the video is choosen by the user
     * In each case, setupContainer() is called
     */
    private SimpleOnGestureListener gestureListner = new SimpleOnGestureListener() {
        @Override
        public boolean onFling(android.view.MotionEvent e1,
                               android.view.MotionEvent e2, float velocityX, float velocityY) {

            if (Math.abs(e1.getY() - e2.getY()) > setting.SWIPE_MAX_OFF_PATH)
                return false;
            else {
                try {
                    if (e1.getX() - e2.getX() > setting.SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > setting.SWIPE_THRESHOLD_VELOCITY) {
                        if (display != setting.DISPLAY_VIDEO) {
                            display++;
                            setupContainer();
                        }

                    } else if (e2.getX() - e1.getX() > setting.SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > setting.SWIPE_THRESHOLD_VELOCITY) {
                        if (display != setting.DISPLAY_PHOTO) {
                            display--;
                            setupContainer();
                        }
                    }
                } catch (Exception e) {
                }
                return true;
            }

        }
    };

    /**
    * Prepare the call to setupContainer() that switch fragments  wether the photo or the video is choosen by the user
    * @param display, integer constant that switch wether the photo or the video is on
    */
    public void goBackFromFragment(int display) {
        this.display = display;
        setupContainer();
    }

    /**
     * Switch fragments  wether the photo or the video is choosen by the user
     */
    private void setupContainer() {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }

        /*Fragment f;
        f = new FilterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, f).commit();*/

        mainActionController.displayImages();

        if (display == setting.DISPLAY_PHOTO) {
            mainActionController.displayPhoto();
        } else {
            mainActionController.displayOtherImage();
        }
    }

    /**
     * Handles clickable View that enable to choose camera or to start a new intent
     * @param v, the clickable View
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        chooseCamera(v);
    }

    /**
     * Choose which camera to use with the help of clickable View
     * Starts the Setting activity, if the setting button is clicked
     * @param v, the clickable View
     */
    private void chooseCamera(View v) {
        if (display != setting.DISPLAY_PHOTO
                && (v.getId() == R.id.btnPic || v.getId() == R.id.btn1)) {
            display = setting.DISPLAY_PHOTO;
            setupContainer();
        } else if (v.getId() == R.id.btn2) {
            if (display == setting.DISPLAY_VIDEO) {
                display = setting.DISPLAY_FILTER;
                setupContainer();
            } else if (display == setting.DISPLAY_FILTER) {
                display = setting.DISPLAY_PHOTO;
                setupContainer();
            }
        } else if (v.getId() == R.id.btn4) {
            if (display == setting.DISPLAY_PHOTO) {
                display = setting.DISPLAY_FILTER;
                setupContainer();
            } else if (display == setting.DISPLAY_FILTER) {
                display = setting.DISPLAY_VIDEO;
                setupContainer();
            }
        } else if (v.getId() == R.id.btn5 && display == setting.DISPLAY_PHOTO) {
            display = setting.DISPLAY_VIDEO;
            setupContainer();
        } else if (display != setting.DISPLAY_FILTER && v.getId() == R.id.btnEffect) {
            display = setting.DISPLAY_FILTER;
            setupContainer();
        } else if (v.getId() == R.id.btnCapture) {
            if (display == setting.DISPLAY_FILTER) {
                display = setting.DISPLAY_PHOTO;
                setupContainer();
            }
        } else if (v.getId() == R.id.tabSetting) {
            startActivity(new Intent(this, Settings.class));
        }
    }

    /**
     * @param item, MenuItem instance
     * @return
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
     * @param requestCode
     * @param resultCode
     * @param data, Intent instance
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(this, MainActivity.class));
    }
}