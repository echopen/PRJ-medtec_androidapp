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
        lIntensityGainFilter.setImageInput(scannedArray);
        lIntensityGainFilter.applyFilter(iCurrentRenderingContext.getIntensityGain());
        int[] scannedGainArray = lIntensityGainFilter.getImageOutput();

        IntensityToRGBFilter lIntensityToRGBFilter = new IntensityToRGBFilter();
        lIntensityToRGBFilter.setImageInput(scannedGainArray);
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
        Timer.init("RenderingPipeline");

        for (int i = 0; i < Constants.PreProcParam.NUM_LINES_PER_IMAGE * Constants.PreProcParam.NUM_SAMPLES_PER_LINE; i++) {
            lImageInput[i] = (int) iRawImageData[i];
        }

        Timer.logResult("Create Fake Images");
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
        IntensityUniformGainFilter lIntensityGainFilter = new IntensityUniformGainFilter();
        lIntensityGainFilter.setImageInput(lresampledCartesianImage);
        lIntensityGainFilter.applyFilter(iCurrentRenderingContext.getIntensityGain());
        int[] lGainImage = lIntensityGainFilter.getImageOutput();

        Timer.logResult("Uniform Gain");

        IntensityToRGBFilter lIntensityToRGBFilter = new IntensityToRGBFilter();
        lIntensityToRGBFilter.setImageInput(lGainImage);
        lIntensityToRGBFilter.applyFilter(iCurrentRenderingContext.getLookUpTable());
        int colors[] =  lIntensityToRGBFilter.getImageOutput();

        Timer.logResult("Intensity to RGB");

        final Bitmap bitmap = Bitmap.createBitmap(colors, Constants.PreProcParam.N_x, Constants.PreProcParam.N_z, Bitmap.Config.ARGB_8888);
        Timer.logResult("Create Bitmap");

        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActionController.displayMainFrame(bitmap);
                    Timer.logResult("Display Main Frame");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
