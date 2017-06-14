package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.echopen.asso.echopen.custom.CustomActivity;
import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.model.Data.BitmapDisplayer;
import com.echopen.asso.echopen.model.Data.BitmapDisplayerFactory;
import com.echopen.asso.echopen.model.Synchronizer;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.ui.ConstantDialogFragment;
import com.echopen.asso.echopen.ui.DrawView;
import com.echopen.asso.echopen.ui.FilterDialogFragment;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.ui.RulerView;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.Timer;
import com.echopen.asso.echopen.utils.UIParams;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

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

public class MainActivity extends CustomActivity implements AbstractActionActivity, EchographyImageVisualisationContract.View {

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

    private EchographyImageStreamingService mEchographyImageStreamingService;
    private RenderingContextController mRenderingContextController;

    private EchographyImageVisualisationContract.Presenter mEchographyImageVisualisationPresenter;

    private SeekBar mSeekBarLinearLutOffset;
    private TextView mTextViewLinearLutOffset;
    private LinearLayout mLayoutLinearLutOffset;

    private SeekBar mSeekBarLinearLutSlope;
    private TextView mTextViewLinearLutSlope;
    private LinearLayout mLayoutLinearLutSlope;

    private SeekBar mSeekBarExponentialLutAlpha;
    private TextView mTextViewExponentialLutAlpha;
    private LinearLayout mLayoutExponentialLutAlpha;

    private SeekBar mSeekBarGain;
    private TextView mTextViewGain;

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

        mEchographyImageStreamingService = ((EchOpenApplication) this.getApplication() ).getEchographyImageStreamingService();
        mRenderingContextController = mEchographyImageStreamingService.getRenderingContextController();

        mEchographyImageVisualisationPresenter = new EchographyImageVisualisationPresenter(mEchographyImageStreamingService, this);
        this.setPresenter(mEchographyImageVisualisationPresenter);

        initSwipeViews();
        initActionController();
        initViewComponents();

        initImageManipulationViewComponents();
        
        setupContainer();

        UIParams.setParam1(Constants.SeekBarParam.SEEK_BAR_SCALE);
        UIParams.setParam2(Constants.SeekBarParam.SEEK_BAR_ROTATE);
        UIParams.setParam3(Constants.SeekBarParam.SEEK_BAR_HORIZONTAL);
        UIParams.setParam4(Constants.SeekBarParam.SEEK_BAR_VERTICAL);

        mTextViewGain = (TextView) findViewById(R.id.textGain);
        mSeekBarGain = (SeekBar) findViewById(R.id.seekBarGain);
        mSeekBarGain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double lGain = progress * 3.0 / 100;
                mTextViewGain.setText(lGain + " dB");
                mRenderingContextController.setIntensityGain(lGain);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        mEchographyImageVisualisationPresenter.start();
    }

    private void initImageManipulationViewComponents() {
         mLayoutLinearLutOffset = (LinearLayout) findViewById(R.id.layoutLinearLutOffset);
         mTextViewLinearLutOffset = (TextView) findViewById(R.id.textLinearLutOffset);
         mSeekBarLinearLutOffset = (SeekBar) findViewById(R.id.seekBarLinearLutOffset);
         mSeekBarLinearLutOffset.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double lOffset = progress * 256 / 100;
                mTextViewLinearLutOffset.setText(new DecimalFormat("#.00").format(lOffset));
                mRenderingContextController.setLinearLutOffset(lOffset);
            }
        });

        mLayoutLinearLutSlope = (LinearLayout) findViewById(R.id.layoutLinearLutSlope);
        mTextViewLinearLutSlope = (TextView) findViewById(R.id.textLinearLutSlope);
        mSeekBarLinearLutSlope = (SeekBar) findViewById(R.id.seekBarLinearLutSlope);

        mSeekBarLinearLutSlope.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double lSlope = (progress - 50) * 1.0 / 10 + 1;
                mTextViewLinearLutSlope.setText(new DecimalFormat("#.00").format(lSlope));
                mRenderingContextController.setLinearLutSlope(lSlope);
            }
        });

        mLayoutExponentialLutAlpha = (LinearLayout) findViewById(R.id.layoutExponentialLutAlpha);
        mTextViewExponentialLutAlpha = (TextView) findViewById(R.id.textExponentialLutAlpha);
        mSeekBarExponentialLutAlpha = (SeekBar) findViewById(R.id.seekBarExponentialLutAlpha);
        mSeekBarExponentialLutAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double lAlpha = 0.001 * progress;
                mTextViewExponentialLutAlpha.setText(new DecimalFormat("#.00").format(lAlpha));
                mRenderingContextController.setExponentialLutAlpha(lAlpha);
            }
        });

        Spinner lDropdownLut = (Spinner)findViewById(R.id.dropdownLut);
        String[] lLutItems = new String[]{"Linear Lut", "Exponential Lut"};
        ArrayAdapter<String> lLutItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lLutItems);
        lDropdownLut.setAdapter(lLutItemsAdapter);

        lDropdownLut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        // linear lut
                        selectLinearLut();
                        break;
                    case 1:
                        // exponential lut
                        selectExponentialLut();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lDropdownLut.setSelection(0);
    }

    private void selectLinearLut(){
        mSeekBarLinearLutOffset.setProgress(0);
        mSeekBarLinearLutSlope.setProgress(50);
        mLayoutLinearLutOffset.setVisibility(View.VISIBLE);
        mLayoutLinearLutSlope.setVisibility(View.VISIBLE);

        mLayoutExponentialLutAlpha.setVisibility(View.GONE);
    }

    private void selectExponentialLut(){

        mLayoutLinearLutOffset.setVisibility(View.GONE);
        mLayoutLinearLutSlope.setVisibility(View.GONE);

        mSeekBarExponentialLutAlpha.setProgress(0);
        mLayoutExponentialLutAlpha.setVisibility(View.VISIBLE);
    }

    public void fetchData(BitmapDisplayerFactory bitmapDisplayerFactory) {
        try {
            BitmapDisplayer bitmapDisplayer = bitmapDisplayerFactory.populateBitmap(
            this, mainActionController, mRenderingContextController, Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT);

            if (UDP_ACQUISITION) {
                bitmapDisplayer.readDataFromUDP();
            } else if (TCP_ACQUISITION) {
                //bitmapDisplayer.readDataFromTCP();
                EchographyImageStreamingTCPMode lTCPMode = new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT);
                mEchographyImageStreamingService.connect(lTCPMode, this);
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

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        try{
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DrawView echoImage = (DrawView) findViewById(R.id.echo);
                    echoImage.setImageBitmap(iBitmap);
                    echoImage.setColorFilter(Config.colorMatrixColorFilter);
                    Timer.logResult("Display Bitmap");
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter iPresenter) {
        mEchographyImageVisualisationPresenter = iPresenter;
    }
}