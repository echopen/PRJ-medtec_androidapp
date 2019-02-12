package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.filters.GreyLevelLinearLookUpTable;
import com.echopen.asso.echopen.filters.IntensityUniformGainFilter;
import com.echopen.asso.echopen.filters.IntensityToRGBFilter;
import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.filters.ScanConversionRenderscriptFilter;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.Timer;

import java.util.Arrays;

/**
 * Core class of data collecting routes. Whether the protocol is chosen to be TCP, UDP or fetching data from local,
 * the dedicated classes inherits from @this
 */
abstract public class AbstractDataTask extends AsyncTask<Void, Void, Void> {

    private final String TAG = this.getClass().getSimpleName();

    protected Activity activity;

    protected RenderingContextController mRenderingContextController;
    protected ProbeCinematicProvider mProbeCinematicProvider;

    protected EchographyImageStreamingService mEchographyImageStreamingService; //TODO should remove dependency

    public AbstractDataTask(Activity activity, RenderingContextController iRenderingContextController, ProbeCinematicProvider iProbeCinematicProvider, EchographyImageStreamingService iEchographyImageStreamingService) {
        this.activity = activity;

        this.mRenderingContextController = iRenderingContextController;
        this.mProbeCinematicProvider = iProbeCinematicProvider;

        this.mEchographyImageStreamingService = iEchographyImageStreamingService;

    }

    protected void rawDataPipeline(RenderingContext iCurrentRenderingContext, DeviceConfiguration iDeviceConfiguration, ProbeCinematicConfiguration iProbeCinematic, Integer[] iRawImageData) {

        int lNbSamplesPerLine = iDeviceConfiguration.getNbSamplesPerLine();
        int lNbLinesPerImage = iDeviceConfiguration.getNbLinesPerImage();

        if(iProbeCinematic instanceof ProbeCinematicLoungerConfiguration)
        {
            ProbeCinematicLoungerConfiguration lProbeLoungerCinematic = (ProbeCinematicLoungerConfiguration) iProbeCinematic;
            prepareRenderingContext(iDeviceConfiguration.getNbLinesPerImage(), iDeviceConfiguration.getNbSamplesPerLine(), iDeviceConfiguration.getProbeSectorAngle(), iDeviceConfiguration.getR0(), iDeviceConfiguration.getRf(), Constants.PreProcParam.N_x, Constants.PreProcParam.N_y,
            lProbeLoungerCinematic.mNh0, lProbeLoungerCinematic.mNhp, lProbeLoungerCinematic.mNsr, lProbeLoungerCinematic.mNdx, lProbeLoungerCinematic.mRb, lProbeLoungerCinematic.mL1, lProbeLoungerCinematic.mTr1,
            Constants.PreProcParam.SPEED_OF_ACOUSTIC_WAVE, iDeviceConfiguration.getDecimation(), iDeviceConfiguration.getEchoDelay());
        }
        else{
            return;
        }
        Timer.init("RenderingPipeline");

        int[] lImageInput = new int[lNbSamplesPerLine * lNbLinesPerImage];
        Arrays.fill(lImageInput, 0);
        for (int i = 0; i < lNbLinesPerImage; i++) {
            for(int j = 0; j < lNbSamplesPerLine; j++){
                lImageInput[i * lNbSamplesPerLine + j] = iRawImageData[i * lNbSamplesPerLine + j];
            }
        }

        int colors [] = render(lImageInput, ((GreyLevelLinearLookUpTable)iCurrentRenderingContext.getLookUpTable()).getSlope(), ((GreyLevelLinearLookUpTable)iCurrentRenderingContext.getLookUpTable()).getOffset() );


        final Bitmap bitmap = Bitmap.createBitmap(colors, Constants.PreProcParam.N_x, Constants.PreProcParam.N_y, Bitmap.Config.ARGB_8888);
        Timer.logResult("Create Bitmap");

        mEchographyImageStreamingService.emitNewImage(bitmap);
    }

    static {
        System.loadLibrary("renderingCpp");
    }

    public native void prepareRenderingContext(int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, float nh0, float nhp, float nsr, float ndx, float Rb, float l1, float tr1, float nspeed, float ndec, float ndelay);
    public native int[] render(int[] iImageInput, double linearQuantificationSlope, double linearQuantificationOffset);

}
