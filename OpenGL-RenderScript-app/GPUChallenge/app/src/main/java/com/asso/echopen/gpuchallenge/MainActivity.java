package com.asso.echopen.gpuchallenge;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.asso.echopen.gpuchallenge.model.BitmapDisplayer;
import com.asso.echopen.gpuchallenge.model.BitmapDisplayerFactory;
import com.asso.echopen.gpuchallenge.ui.MainActionController;
import com.asso.echopen.gpuchallenge.ui.TextureRenderer;
import com.asso.echopen.gpuchallenge.utils.Config;
import com.asso.echopen.gpuchallenge.utils.OpenGLCheck;

import android.opengl.GLSurfaceView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {

    private MainActionController mainActionController;

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.getInstance(this);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.vMiddle);
        linearLayout.setBackgroundColor(Color.TRANSPARENT);
        initActionController();
        BitmapDisplayerFactory bitmapDisplayerFactory = new BitmapDisplayerFactory();
        fetchData(bitmapDisplayerFactory);

        glSurfaceView = new GLSurfaceView(this);
        if (detectOpenGLES20())
        {
            // Tell the surface view we want to create an OpenGL ES 2.0-compatible
            // context, and set an OpenGL ES 2.0-compatible renderer.
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(new TextureRenderer(this));
        }
        else
        {
            Log.e("MultiTexture", "OpenGL ES 2.0 not supported on device.  Exiting...");
            finish();

        }
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.glView);
        mainLayout.addView(glSurfaceView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    public void initActionController() {
        Activity activity = this;
        mainActionController = new MainActionController(activity);
    }

    public void fetchData(BitmapDisplayerFactory bitmapDisplayerFactory) {
        try {
            BitmapDisplayer bitmapDisplayer = bitmapDisplayerFactory.populateBitmap(
                    this, mainActionController);

                AssetManager assetManager = getResources().getAssets();
                InputStream inputStream = assetManager.open("data/raw_data/data_phantom.csv");
                bitmapDisplayer.readDataFromFile(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // To be dumped if any necessity
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        glSurfaceView.onPause();
    }

    private boolean detectOpenGLES20()
    {
        ActivityManager am =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return (info.reqGlEsVersion >= 0x20000);
    }
}
