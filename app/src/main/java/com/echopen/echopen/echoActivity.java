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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import static android.R.attr.start;


public class echoActivity extends AppCompatActivity{

    final int WRITE_PERMISSION = 1;

    private Button galleryBtn;


    Button degre1Btn ;
    Button degre2Btn ;
    Button degre3Btn ;

    ImageButton hommeBtn;
    ImageButton femmeBtn;
    ImageButton enfantBtn;


    private static final int SELECT_PICTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.content_echo);


        Intent myIntent = getIntent();
        String orientation = myIntent.getStringExtra("orientation");


        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        if(orientation.equals("Left")){
            FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
            params.gravity = (Gravity.RIGHT |  Gravity.BOTTOM);
            fabMenu.setLayoutParams(params);
        }else if(orientation.equals("Right")){
            FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
            params.gravity = (Gravity.LEFT |  Gravity.BOTTOM);
            fabMenu.setLayoutParams(params);
        }

        galleryBtn = (Button) findViewById(R.id.galleryBtn);

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/EchoPen/images/");
                intent.setData(uri);
                intent.setType("image/png");
                startActivityForResult(Intent.createChooser(intent, "Open folder"), SELECT_PICTURE);
            }
        });

        Button record = (Button) findViewById(R.id.record);
//
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    if (ActivityCompat.shouldShowRequestPermissionRationale(echoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    }
                    else {
                        ActivityCompat.requestPermissions(echoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
                    }
                }else{
                    Bitmap bitmap = takeScreenshot();
                    saveBitmap(bitmap);
                }
            }
        });


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

        degre1Btn = (Button) findViewById(R.id.prof1Btn);
        degre2Btn = (Button) findViewById(R.id.prof2Btn);
        degre3Btn = (Button) findViewById(R.id.prof3Btn);

        hommeBtn = (ImageButton) findViewById(R.id.hommeBtn);
        femmeBtn = (ImageButton) findViewById(R.id.femmeBtn);
        enfantBtn = (ImageButton) findViewById(R.id.enfantBtn);


        degre1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProf(degre1Btn);
            }
        });

        degre2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProf(degre2Btn);
            }
        });

        degre3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProf(degre3Btn);
            }
        });


        hommeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSex(hommeBtn);
            }
        });

        femmeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSex(femmeBtn);
            }
        });

        enfantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSex(enfantBtn);
            }
        });

    }


    public void selectProf(Button btn){


        switch (btn.getId()){
            case R.id.prof1Btn:
                degre1Btn.setBackgroundResource(R.drawable.round_button_profondeur_selected);
                degre2Btn.setBackgroundResource(R.drawable.round_button_profondeur);
                degre3Btn.setBackgroundResource(R.drawable.round_button_profondeur);
                break;
            case R.id.prof2Btn:
                degre1Btn.setBackgroundResource(R.drawable.round_button_profondeur);
                degre2Btn.setBackgroundResource(R.drawable.round_button_profondeur_selected);
                degre3Btn.setBackgroundResource(R.drawable.round_button_profondeur);
                break;
            case R.id.prof3Btn:
                degre1Btn.setBackgroundResource(R.drawable.round_button_profondeur);
                degre2Btn.setBackgroundResource(R.drawable.round_button_profondeur);
                degre3Btn.setBackgroundResource(R.drawable.round_button_profondeur_selected);
                break;
        }

    }

    public void selectSex(ImageButton btn){


        switch (btn.getId()){
            case R.id.hommeBtn:
                hommeBtn.setImageResource(R.drawable.ico_men_on_copy);
                femmeBtn.setImageResource(R.drawable.ico_women_copy);
                enfantBtn.setImageResource(R.drawable.ico_child_copy);
                break;
            case R.id.femmeBtn:
                hommeBtn.setImageResource(R.drawable.ico_men_copy);
                femmeBtn.setImageResource(R.drawable.ico_women_on_copy);
                enfantBtn.setImageResource(R.drawable.ico_child_copy);
                break;
            case R.id.enfantBtn:
                hommeBtn.setImageResource(R.drawable.ico_men_copy);
                femmeBtn.setImageResource(R.drawable.ico_women_copy);
                enfantBtn.setImageResource(R.drawable.ico_child_on_copy);
                break;
        }


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

        File folder = new File(Environment.getExternalStorageDirectory()+File.separator+"Pictures/EchoPen/images/");
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




}
