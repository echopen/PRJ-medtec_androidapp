package com.echopen.asso.echopen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.animation.ObjectAnimator;
import android.view.animation.Animation;


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

public class MainActivity extends Activity implements EchographyImageVisualisationContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EchographyImageStreamingService mEchographyImageStreamingService;
    private EchographyImageVisualisationContract.Presenter mEchographyImageVisualisationPresenter;
    private ImageView mCaptureButton;
    private ImageView mPregnantWomanButton;
    private ImageView mEndExamButton;
    private ImageView mBatteryButton;
    private ImageView mSelectButton;
    private ImageView mCaptureShadow;
    private Long then;
    private RotateAnimation rotate_animation_capture;

    private final static float IMAGE_ZOOM_FACTOR = 1.75f;
    private final static float IMAGE_ROTATION_FACTOR = 90.f;
    /**
     * This method calls all the UI methods and then gives hand to  UDPToBitmapDisplayer class.
     * UDPToBitmapDisplayer listens to UDP data, processes them with the help of ScanConversion,
     * and then displays them.
     * Also, this method uses the Config singleton class that provides device-specific constants
     */
    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mEchographyImageStreamingService = ((EchOpenApplication) getApplication()).getEchographyImageStreamingService();
        mEchographyImageStreamingService.connect(new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT), this);

        this.setPresenter(new EchographyImageVisualisationPresenter(mEchographyImageStreamingService, this));

        setContentView(R.layout.activity_main);





        mCaptureButton = (ImageView) findViewById(R.id.main_button_capture);
        mCaptureShadow = (ImageView) findViewById(R.id.main_button_shadow);
        mCaptureShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("captureButton", "Short Press");


            }
        });
        mCaptureShadow.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v,final MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    then = System.currentTimeMillis();

                    rotate_animation_capture = new RotateAnimation(0,144 ,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.6f);

                    rotate_animation_capture.setDuration(5000);
                    //rotate.setRepeatCount(Animation.INFINITE);
                    mCaptureShadow.clearAnimation();
                    mCaptureShadow.setAnimation(rotate_animation_capture);

                }








                else if(event.getAction() == MotionEvent.ACTION_UP){
                    if(((Long) System.currentTimeMillis() - then) > 5000){
                        Log.d("mcaptureButton", "Long Press");

                        //rotate.cancel();
                        return true;
                    }
                    else {

                        rotate_animation_capture.cancel();
                        rotate_animation_capture= null;

                    }
                }
                return false;
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
}