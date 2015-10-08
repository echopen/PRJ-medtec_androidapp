package com.echopen.asso.echopen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.echopen.asso.echopen.model.Data;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.custom.CustomActivity;
import com.echopen.asso.echopen.ui.CameraFragment;
import com.echopen.asso.echopen.ui.FilterDialogFragment;
import com.echopen.asso.echopen.ui.FilterFragment;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.utils.AppHelper;
import com.echopen.asso.echopen.utils.Constants;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends CustomActivity implements AbstractActionActivity {

    private int display;

    private MainActionController mainActionController;

    private ScanConversion scanConversion;

    public GestureDetector gesture;

    public Constants.Settings setting;

    protected Uri uri;

    static {
        System.loadLibrary(Constants.JNI_SETTINGS.LOCAL_MODULE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        initSwipeViews();
        initViewComponents();
        initActionController();
        setupContainer();
    }

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

        setClickToFilter(R.id.vMiddle);

        applyBgTheme(findViewById(R.id.vTop));
        applyBgTheme(findViewById(R.id.vBottom));
    }

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

        Fragment f;
        if (display != setting.DISPLAY_FILTER)
            f = new CameraFragment();
        else
            f = new FilterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, f).commit();

        if (display == setting.DISPLAY_VIDEO) {
            mainActionController.displayVideo();
        } else {
            mainActionController.displayImages();

            if (display == setting.DISPLAY_PHOTO) {
                mainActionController.displayPhoto();
            } else {
                mainActionController.displayOtherImage();
            }

            AssetManager assetManager = getResources().getAssets();
            try {
                InputStream inputStream = assetManager.open("data/data_kydney.csv");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                scanConversion = new ScanConversion(inputStreamReader);
                //scanConversion.compute_interpolation();

                Data data = new Data(inputStreamReader);
                char[] envelope_data = data.getEnvelopeData();
                byte[] byte_envelope_data = new String(envelope_data).getBytes();
                double[] test = scanConversion.frameFromJNI(byte_envelope_data, Constants.PreProcParam.getLoadIntegerConstants(), Constants.PreProcParam.getLoadDoubleConstants());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Bitmap bitmap = scanConversion.getBitmap();
            //mainActionController.displayMainFrame(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        chooseCamera(v);
        runCamera();
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(this, MainActivity.class));
    }
}
