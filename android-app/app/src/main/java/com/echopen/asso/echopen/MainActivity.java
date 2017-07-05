package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;

import static com.echopen.asso.echopen.utils.Constants.Http.LOCAL_IP;
import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_PORT;

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

public class MainActivity extends Activity {

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
        setContentView(R.layout.activity_main);
        //-----------------------------------------------------------------
        final imagesHandler imagesHandler = new imagesHandler(getFilesDir());

        final Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imagesHandler.startRecording();
            }
        });

        final Button btnEnd = (Button) findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imagesHandler.endRecording(1);
            }
        });

        final ImageView image = (ImageView) findViewById(R.id.imageView);

        EchOpenApplication app = (EchOpenApplication) getApplication();
        EchographyImageStreamingService stream = app.getEchographyImageStreamingService();

        EchographyImageVisualisationPresenter presenter = new EchographyImageVisualisationPresenter(stream, new EchographyImageVisualisationContract.View() {
            @Override
            public void refreshImage(final Bitmap iBitmap) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imagesHandler.saveCacheImage(iBitmap);
                            image.setImageBitmap(iBitmap);
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void setPresenter(EchographyImageVisualisationContract.Presenter presenter) {

            }
        });

        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode(LOCAL_IP, REDPITAYA_PORT);

        stream.connect(mode,this);
        presenter.start();
        


        //-----------------------------------------------------------------

    }

    @Override
    protected void onResume(){
        super.onResume();
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
}