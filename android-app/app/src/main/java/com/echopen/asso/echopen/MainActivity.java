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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;

import static android.widget.Toast.LENGTH_SHORT;
import static com.echopen.asso.echopen.utils.Constants.Http.LOCAL_IP;
import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_PORT;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

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

        final GestureDetector gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.d("tap","OnDoubleTap"+e);

                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d("tap","onSingleTapConfirmed"+e);

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d("tap","onLongPress"+e);
            }


        });

        final GestureDetector gdOnView = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDown(MotionEvent e) {
                Log.d("log", "test"+ e);
                return true;
            }

            boolean toClose = false;
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.d("tap", "OnDoubleTap" + e);
                if (e.getAction() == MotionEvent.ACTION_DOWN && !toClose) {
                    Log.d("tap", "toClose if " + toClose);
                    View seekBarH = (View) findViewById(R.id.seekBarH);
                    seekBarH.setVisibility(View.VISIBLE);
                    View freqText = (View) findViewById(R.id.frequence);
                    freqText.setVisibility(View.VISIBLE);

                    View seekBarV = (View) findViewById(R.id.seekBarV);
                    seekBarV.setVisibility(View.VISIBLE);
                    View gainText = (View) findViewById(R.id.gain);
                    gainText.setVisibility(View.VISIBLE);
                    toClose = true;
                } else if (e.getAction() == MotionEvent.ACTION_DOWN && toClose) {
                    Log.d("tap", "toClose else " + toClose);
                    View seekBarH = (View) findViewById(R.id.seekBarH);
                    seekBarH.setVisibility(View.INVISIBLE);
                    View freqText = (View) findViewById(R.id.frequence);
                    freqText.setVisibility(View.INVISIBLE);


                    View seekBarV = (View) findViewById(R.id.seekBarV);
                    seekBarV.setVisibility(View.INVISIBLE);
                    View gainText = (View) findViewById(R.id.gain);
                    gainText.setVisibility(View.INVISIBLE);

                    toClose = false;
                }

                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d("tap","onSingleTapConfirmed"+e);

                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY) {

                float v = e1.getX() - e2.getX();
                Log.d("tap", "onFling e2 "+ v );

                //si taille de la barre + start event > taille window : barre stop border screen

                if (abs(e1.getX() - e2.getX()) > 300 && abs(e1.getY() - e2.getY()) < 300) {
                    //event horizontal
                    Log.d("tap", "onFling horizontal ");
                    //graduation 3, fréquence

                }

                if (abs(e1.getX() - e2.getX()) < 300 && abs(e1.getY() - e2.getY()) > 300) {
                    //event vertical
                    Log.d("tap", "onFling vertical ");
                    //graduation 4, gain
                }

                return true;
            }


        });

        View mainView = (View) findViewById(R.id.main);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gdOnView.onTouchEvent(event);
            }
        });


        Button mainButton = (Button) findViewById(R.id.button2);
        mainButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gd.onTouchEvent(event);
            }
        });

        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode(LOCAL_IP, REDPITAYA_PORT);

        stream.connect(mode,this);
        presenter.start();
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
}
