package com.echopen.asso.echopen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.echopen.asso.echopen.example.CameraPreview;
import com.echopen.asso.echopen.model.Data.UDPToBitmapDisplayer;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.custom.CustomActivity;
import com.echopen.asso.echopen.ui.FilterDialogFragment;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.SurfaceImage;
import com.echopen.asso.echopen.utils.AppHelper;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    private SurfaceImage surfaceImage;

   /* class that deals with the view of MainActivity */
    private MainActionController mainActionController;

    public GestureDetector gesture;

    /* main UI constants of the app */
    public Constants.Settings setting;

    /** locator of the screenshots or - the runcamera() method that processes it is currently unused
     * for the moment - but it will be plugged again later in the developement
     */
    protected Uri uri;

    static {
        System.loadLibrary("JNIProcessor");
    }

    native private boolean compileKernels();

    private void copyFile(final String f) {
        InputStream in;
        try {
            in = getAssets().open(f);
            final File of = new File(getDir("execdir",MODE_WORLD_WRITEABLE), f);
            final OutputStream out = new FileOutputStream(of);

            final byte b[] = new byte[65535];
            int sz = 0;
            while ((sz = in.read(b)) > 0) {
                out.write(b, 0, sz);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

        copyFile("scan_conversion_kernel.cl");

        Log.i("TAGGY", "Kernel compilation beginning");

        try{
            if( compileKernels() == false )
                Log.i("TAGGY","Kernel compilation failed");
            else
                Log.i("TAGGY","Kernel compilation succedeed");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        surfaceImage = new SurfaceImage(this.getBaseContext(), this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.echo_image_preview);
        preview.addView(surfaceImage);

        Log.d("TAGG", "Hello");
        try {
            UDPToBitmapDisplayer udpData = new UDPToBitmapDisplayer(this, mainActionController, Constants.Http.REDPITAYA_UDP_IP, Constants.Http.REDPITAYA_UDP_PORT);
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

        //setClickToFilter(R.id.vMiddle);

        applyBgTheme(findViewById(R.id.vTop));
        applyBgTheme(findViewById(R.id.vBottom));
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

    public void goBackFromFragment(int display) {
        this.display = display;
        setupContainer();
    }

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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //chooseCamera(v);
        //runCamera();
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

    private void runCamera() {
        if (display == setting.DISPLAY_PHOTO) {
            Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            uri = AppHelper.getFileUri(MainActivity.this, setting.MEDIA_TYPE_IMAGE);
            if (uri == null)
                AppHelper.getErrorStorageMessage(MainActivity.this);
            else {
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(photoIntent, setting.TAKE_PHOTO);
            }
        } else if (display == setting.DISPLAY_VIDEO) {
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            uri = AppHelper.getFileUri(MainActivity.this, setting.MEDIA_TYPE_VIDEO);
            if (uri == null)
                AppHelper.getErrorStorageMessage(MainActivity.this);
            else {
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 0 = lowest res
                startActivityForResult(videoIntent, setting.TAKE_VIDEO_REQUEST);
            }
        }
    }

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
