package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

import com.echopen.asso.echopen.filters.EnvelopeDetectionRenderscriptFilter;
import com.echopen.asso.echopen.filters.IntensityUniformGainFilter;
import com.echopen.asso.echopen.filters.IntensityToRGBFilter;
import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.filters.ScanConversionRenderscriptFilter;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.Timer;

/**
 * Core class of data collecting routes. Whether the protocol is chosen to be TCP, UDP or fetching data from local,
 * the dedicated classes inherits from @this
 */
abstract public class AbstractDataTask extends AsyncTask<Void, Void, Void> {

    private final String TAG = this.getClass().getSimpleName();

    protected Activity activity;

    protected ScanConversion scanconversion;

    protected MainActionController mainActionController;

    protected RenderingContextController mRenderingContextController;

    public AbstractDataTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion, RenderingContextController iRenderingContextController) {
        this.scanconversion = scanConversion;
        this.activity = activity;
        this.mainActionController = mainActionController;

        this.mRenderingContextController = iRenderingContextController;
    }

    protected void refreshUI(ScanConversion scanconversion, RenderingContext iCurrentRenderingContext) {
        int[] scannedArray = scanconversion.getDataFromInterpolation();

        IntensityUniformGainFilter lIntensityGainFilter = new IntensityUniformGainFilter();
        lIntensityGainFilter.setImageInput(scannedArray, scannedArray.length);
        lIntensityGainFilter.applyFilter(iCurrentRenderingContext.getIntensityGain());
        int[] scannedGainArray = lIntensityGainFilter.getImageOutput();

        IntensityToRGBFilter lIntensityToRGBFilter = new IntensityToRGBFilter();
        lIntensityToRGBFilter.setImageInput(scannedGainArray, scannedGainArray.length);
        lIntensityToRGBFilter.applyFilter(iCurrentRenderingContext.getLookUpTable());
        int colors[] =  lIntensityToRGBFilter.getImageOutput();

        //Arrays.fill(colors, 0, 4*scannedArray.length, Color.WHITE);
        final Bitmap bitmap = Bitmap.createBitmap(colors, 512*Constants.PreProcParam.SCALE_IMG_FACTOR, 512/Constants.PreProcParam.SCALE_IMG_FACTOR, Bitmap.Config.ARGB_8888);
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActionController.displayMainFrame(bitmap);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void rawDataPipeline(ScanConversion scanconversion,RenderingContext iCurrentRenderingContext, Integer[] iRawImageData) {

        //TODO: temporary fake image in scan conversion filter input
        int[] lImageInput = new int[Constants.PreProcParam.NUM_SAMPLES_PER_LINE * Constants.PreProcParam.NUM_LINES_PER_IMAGE];

        double r1 = Math.random();
        double r2 = Math.random();
        for (int i = 0; i < Constants.PreProcParam.NUM_LINES_PER_IMAGE; i++) {
            for (int j = 0; j < Constants.PreProcParam.NUM_SAMPLES_PER_LINE; j++) {
               /* if (j % 6 == 0 || j % 6 == 1 || j % 6 == 2) {
                    lImageInput[i * Constants.PreProcParam.NUM_SAMPLES_PER_LINE + j] = (int) (240 * Math.random());
                } else {
                    lImageInput[i * Constants.PreProcParam.NUM_SAMPLES_PER_LINE + j] = 0;
                }*/
                lImageInput[i * Constants.PreProcParam.NUM_SAMPLES_PER_LINE + j ] = (int) (Math.random() * 250);
            }
        }

        Timer.init("RenderingPipeline");
        RenderScript lRenderscript = RenderScript.create(activity);
        // envelop detection filter
        EnvelopeDetectionRenderscriptFilter lEnvelopDetectionFilter = new EnvelopeDetectionRenderscriptFilter();
        lEnvelopDetectionFilter.setImageInput(lImageInput, Constants.PreProcParam.NUM_SAMPLES_PER_LINE, Constants.PreProcParam.NUM_LINES_PER_IMAGE);
        lEnvelopDetectionFilter.applyFilter(lRenderscript, Constants.PreProcParam.TCP_NUM_SAMPLES);
        int[] lEnvelopImageData = lEnvelopDetectionFilter.getImageOutput();
        Timer.logResult("EnvelopDetection");

        ScanConversionRenderscriptFilter lScanConversionFilter = new ScanConversionRenderscriptFilter();
        lScanConversionFilter.setImageInput(lEnvelopImageData);
        lScanConversionFilter.applyFilter(lRenderscript);
        int[] lresampledCartesianImage = lScanConversionFilter.getImageOutput();

        Timer.logResult("ScanConversion");
        //TODO: filters has to be improve to support 16bit data values
        /*IntensityUniformGainFilter lIntensityGainFilter = new IntensityUniformGainFilter();
        lIntensityGainFilter.setImageInput(scannedArray, scannedArray.length);
        lIntensityGainFilter.applyFilter(iCurrentRenderingContext.getIntensityGain());
        int[] scannedGainArray = lIntensityGainFilter.getImageOutput();

        IntensityToRGBFilter lIntensityToRGBFilter = new IntensityToRGBFilter();
        lIntensityToRGBFilter.setImageInput(scannedGainArray, scannedGainArray.length);
        lIntensityToRGBFilter.applyFilter(iCurrentRenderingContext.getLookUpTable());
        int colors[] =  lIntensityToRGBFilter.getImageOutput();*/

        // TODO: remove image threshold on 8 bits
        for (int i = 0; i < lresampledCartesianImage.length; i++) {
            if(lresampledCartesianImage[i]>255)
                lresampledCartesianImage[i] = 255;
        }

        int colors[] = new int[Constants.PreProcParam.N_x * Constants.PreProcParam.N_z];
        for(int i = 0; i < lresampledCartesianImage.length; i++){
            colors[i] = lresampledCartesianImage[i] | lresampledCartesianImage[i] << 8 | lresampledCartesianImage[i] << 16 | 0xFF000000;
        }
        // end Remove
        Timer.logResult("RGBToIntensity");

        final Bitmap bitmap = Bitmap.createBitmap(colors, Constants.PreProcParam.N_x, Constants.PreProcParam.N_z, Bitmap.Config.ARGB_8888);
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActionController.displayMainFrame(bitmap);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
