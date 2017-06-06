package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
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

    protected RenderingContextController mRenderingContextController;

    protected EchographyImageStreamingService mEchographyImageStreamingService; //TODO should remove dependency

    public AbstractDataTask(Activity activity, RenderingContextController iRenderingContextController, EchographyImageStreamingService iEchographyImageStreamingService) {
        this.activity = activity;

        this.mRenderingContextController = iRenderingContextController;

        this.mEchographyImageStreamingService = iEchographyImageStreamingService;
    }

    protected void rawDataPipeline(RenderingContext iCurrentRenderingContext, DeviceConfiguration iDeviceConfiguration, Integer[] iRawImageData) {

        int lNbSamplesPerLine = iDeviceConfiguration.getNbSamplesPerLine();
        int lNbLinesPerImage = iDeviceConfiguration.getNbLinesPerImage();
        //TODO: temporary fake image in scan conversion filter input
        int[] lImageInput = new int[lNbSamplesPerLine * lNbLinesPerImage];
        Timer.init("RenderingPipeline");

        for (int i = 0; i < lNbLinesPerImage * lNbSamplesPerLine; i++) {
            lImageInput[i] = (int) iRawImageData[i];
        }

        RenderScript lRenderscript = RenderScript.create(activity);
        // envelop detection filter
        EnvelopeDetectionRenderscriptFilter lEnvelopDetectionFilter = new EnvelopeDetectionRenderscriptFilter();
        lEnvelopDetectionFilter.setImageInput(lImageInput, lNbSamplesPerLine, lNbLinesPerImage);
        lEnvelopDetectionFilter.applyFilter(lRenderscript, Constants.PreProcParam.TCP_NUM_SAMPLES);
        int[] lEnvelopImageData = lEnvelopDetectionFilter.getImageOutput();
        Timer.logResult("EnvelopDetection");

        ScanConversionRenderscriptFilter lScanConversionFilter = new ScanConversionRenderscriptFilter();
        lScanConversionFilter.setImageInput(lEnvelopImageData);
        lScanConversionFilter.applyFilter(lRenderscript, iDeviceConfiguration);
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

        mEchographyImageStreamingService.emitNewImage(bitmap);
    }
}
