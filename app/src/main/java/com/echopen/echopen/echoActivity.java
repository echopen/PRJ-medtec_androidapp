package com.echopen.echopen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.echopen.echopen.lib.OnSwipeTouchListener;
import com.github.clans.fab.FloatingActionMenu;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.screenDensity;


public class echoActivity extends AppCompatActivity{

    final int WRITE_PERMISSION = 1;

    private static final int CAST_PERMISSION_CODE = 22;
    private DisplayMetrics mDisplayMetrics;
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mProjectionManager;
    private VirtualDisplay mVirtualDisplay;
    private MediaRecorder mMediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.content_echo);

        Button record = (Button) findViewById(R.id.record);
//
//        record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(echoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                    }
//                    else {
//                        ActivityCompat.requestPermissions(echoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
//                    }
//                }else{
//                    Bitmap bitmap = takeScreenshot();
//                    saveBitmap(bitmap);
//                }
//            }
//        });


        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(echoActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(echoActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(echoActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(echoActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(echoActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });

//        FloatingActionMenu fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        record.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

          }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Bitmap bitmap = takeScreenshot();
                    saveBitmap(bitmap);
                } else {
                    Toast.makeText(getApplicationContext(),"VEUILLEZ AUTORISER L'APP",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }



    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {

        File folder = new File(Environment.getExternalStorageDirectory()+File.separator+"EchoPen/images/");
        try {
            if (!folder.exists()) {
                if (!folder.mkdirs())
                    Toast.makeText(getApplicationContext(), "ERROR WHILE CREATING FOLDER !", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e("FOLDER ERROR ",e.getMessage());
        }
        try {
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            File imagePath = new File(folder+"/"+ts+".png");
            FileOutputStream fos;
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            Toast.makeText(getApplicationContext(),"File Saved !",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }





    private void startRecording() {
        // If mMediaProjection is null that means we didn't get a context, lets ask the user
        if (mMediaProjection == null) {
            // This asks for user permissions to capture the screen
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), CAST_PERMISSION_CODE);
            return;
        }
//        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    private void stopRecording() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
        }
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
        }
        prepareRecording();
    }

    public String getCurSysDate() {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
    }

    private void prepareRecording() {
        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final String directory = Environment.getExternalStorageDirectory() + File.separator + "Recordings";
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "Failed to get External Storage", Toast.LENGTH_SHORT).show();
            return;
        }
        final File folder = new File(directory);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        String filePath;
        if (success) {
            String videoName = ("capture_" + getCurSysDate() + ".mp4");
            filePath = directory + File.separator + videoName;
        } else {
            Toast.makeText(this, "Failed to create Recordings directory", Toast.LENGTH_SHORT).show();
            return;
        }

        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(width, height);
        mMediaRecorder.setOutputFile(filePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CAST_PERMISSION_CODE) {
            // Where did we get this request from ? -_-
            Log.w("ERRORRRRR : ", "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Screen Cast Permission Denied :(", Toast.LENGTH_SHORT).show();
            return;
        }
        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        // TODO Register a callback that will listen onStop and release & prepare the recorder for next recording
        // mMediaProjection.registerCallback(callback, null);
        mVirtualDisplay = getVirtualDisplay();
        mMediaRecorder.start();
    }

    private VirtualDisplay getVirtualDisplay() {
//        screenDensity = mDisplayMetrics.densityDpi;
        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;

        return mMediaProjection.createVirtualDisplay(this.getClass().getSimpleName(),
                width, height, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null /*Handler*/);
    }


}
