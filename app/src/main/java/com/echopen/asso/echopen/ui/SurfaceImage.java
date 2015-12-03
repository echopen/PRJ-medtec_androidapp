package com.echopen.asso.echopen.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.echopen.asso.echopen.example.Constants;
import com.echopen.asso.echopen.preproc.ScanConversion;

/**
 * Created by mehdibenchoufi on 10/11/15.
 */
public class SurfaceImage extends SurfaceView implements SurfaceHolder.Callback{

    private Bitmap 	mBackBuffer;
    private int[]	mImgDims;
    private Paint 	mPaint;
    private Rect	mSrcRect;
    private Rect	mTrgtRect;
    private Activity activity;

    public SurfaceImage(Context context, Activity activity) {
        super(context);
        this.activity = activity;
        getHolder().addCallback(this);
        setWillNotDraw(false);
        setDimensions();
    }

    private void setDimensions(){
        mImgDims 	= new int[3];
        mImgDims[0] = Constants.MAX_DISP_IMG_WIDTH;
        mImgDims[1] = Constants.MAX_DISP_IMG_HEIGHT;
        mImgDims[2] = 4;
        mSrcRect	= new Rect(0,0,mImgDims[0],mImgDims[1]);
    }

    public void surfaceCreated(SurfaceHolder pHolder) {
        try {
            mTrgtRect	= pHolder.getSurfaceFrame();
            mBackBuffer = Bitmap.createBitmap(Constants.MAX_DISP_IMG_WIDTH,
                    Constants.MAX_DISP_IMG_HEIGHT, Bitmap.Config.ARGB_8888);
            ScanConversion scanConversion = new ScanConversion(activity, mBackBuffer);
        }
        catch (Exception e) {
            Log.d("TAG", e.getMessage());
        }
    }

    public void surfaceChanged(SurfaceHolder pHolder, int pFormat, int pW, int pH) {

        if (pHolder.getSurface() == null) {
            Log.d("TAG","No proper holder");
            return;
        }
        try {
            Log.d("TAG","No proper holder");
        } catch (Exception e) {
            Log.d("TAG","some message");
            return;
        }

        try {
            Log.d("TAG","some message");
        } catch (Exception e){
            Log.d("TAG", "@SurfaceChanged Error : " + e.getMessage());
        }
    }

    public void onPreviewFrame() {
        try {
            //
        } catch(Exception e) {
            Log.d("TAG", e.getMessage());
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas pCanvas) {
        if( mBackBuffer!=null ) {
            Log.d("TAGGYS", String.valueOf(mBackBuffer.getPixel(0,0)));
            Log.d("TAGGYS", String.valueOf(mBackBuffer.getPixel(0,1)));
            Log.d("TAGGYS", String.valueOf(mBackBuffer.getPixel(0,2)));
            Log.d("TAGGYS", String.valueOf(mBackBuffer.getPixel(0,3)));
            pCanvas.drawBitmap(mBackBuffer, mSrcRect, mTrgtRect, null);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (true) {
            // some checks and then BackBuffering;
        }
    }
}
