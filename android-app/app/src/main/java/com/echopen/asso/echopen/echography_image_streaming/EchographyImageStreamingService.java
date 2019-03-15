package com.echopen.asso.echopen.echography_image_streaming;

import android.graphics.Bitmap;

import com.echopen.asso.echopen.echography_image_streaming.notifications.EchographyImageStreamingNotification;
import com.echopen.asso.echopen.filters.GreyLevelLinearLookUpTable;
import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.probe_communication.commands.LineData;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSendLineNotification;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationWifiNotification;
import com.echopen.asso.echopen.probe_communication.notifications.WifiState;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.Timer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Observable;
import java.util.concurrent.Semaphore;

/**
 *
 * @class service that connects to device, receives raw data, builds echography images and streams it
 * to echopen application
 */
public class EchographyImageStreamingService extends Observable{

    private final String TAG = this.getClass().getSimpleName();

    private RenderingContextController mRenderingContextController; /* rendering context controller */
    private ProbeCinematicProvider mProbeCinematicProvider; /* probe cinematic provider */
    private DeviceConfiguration mDeviceConfiguration; /* probe cinematic provider */

    private int mCurrentFrame;
    private int[] mCurrentImageBuffer;

    private Semaphore mDataBufferSemaphore;
    /**
     * @brief constructor
     *
     * @param iRenderingContextController rendering context controller
     */
    public EchographyImageStreamingService(RenderingContextController iRenderingContextController, ProbeCinematicProvider iProbeCinematicProvider){
        mRenderingContextController = iRenderingContextController;
        mProbeCinematicProvider = iProbeCinematicProvider;

        //TODO: move Device configuration declaration at the reception of a command
        mDeviceConfiguration = new DeviceConfiguration(10.f * 0.001f, 180.f * 0.001f, 57.f,  100, 800, "PROBE_V1__3_5MHZ", 0, 27);
        mCurrentFrame = -1;
        mCurrentImageBuffer = new int[mDeviceConfiguration.getNbLinesPerImage() * mDeviceConfiguration.getNbSamplesPerLine()];
        mDataBufferSemaphore = new Semaphore(1);

        EventBus.getDefault().register(this);
    }

    protected void rawDataPipeline(RenderingContext iCurrentRenderingContext, DeviceConfiguration iDeviceConfiguration, ProbeCinematicConfiguration iProbeCinematic, int[] iRawImageData) {

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
        int colors [] = render(iRawImageData, ((GreyLevelLinearLookUpTable)iCurrentRenderingContext.getLookUpTable()).getSlope(), ((GreyLevelLinearLookUpTable)iCurrentRenderingContext.getLookUpTable()).getOffset() );
        final Bitmap bitmap = Bitmap.createBitmap(colors, Constants.PreProcParam.N_x, Constants.PreProcParam.N_y, Bitmap.Config.ARGB_8888);
        Timer.logResult("Create Bitmap");

        emitNewImage(bitmap);
    }
    /**
     * @brief emit a new image notification event
     *
     * @param iImage bitmap image generated
     */
    public void emitNewImage(Bitmap iImage){
        EventBus.getDefault().post(new EchographyImageStreamingNotification(iImage));
    }

    /**
     * @brief getter for rendering context controller
     *
     * @return rendering context controller
     */
    public RenderingContextController getRenderingContextController(){
        return mRenderingContextController;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onLineReceived(ProbeCommunicationSendLineNotification iLineNotification) throws InterruptedException {
        LineData lLineData = iLineNotification.getLineData();


        if(mCurrentFrame == -1){
            mCurrentFrame = lLineData.getFrame();
            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(WifiState.WIFI_START_SCANNING));
        }
        else if(mCurrentFrame == lLineData.getFrame()){
            // secure concurrent access on raw data buffer during new line insertion
            mDataBufferSemaphore.acquire();
            System.arraycopy(lLineData.getData(), 0, mCurrentImageBuffer, (lLineData.getLineIndex() - 1) * lLineData.getNbSamples(), lLineData.getNbSamples());
            mDataBufferSemaphore.release();
        }
        else if((mCurrentFrame + 1) == lLineData.getFrame()){
            mCurrentFrame = lLineData.getFrame();

            // secure concurrent acces on raw data buffer during buffer copy
            mDataBufferSemaphore.acquire();
            int[] lCurrentImage = Arrays.copyOfRange(mCurrentImageBuffer, 0, mDeviceConfiguration.getNbLinesPerImage() * mDeviceConfiguration.getNbSamplesPerLine());
            mCurrentImageBuffer = new int[mDeviceConfiguration.getNbLinesPerImage() * mDeviceConfiguration.getNbSamplesPerLine()];
            System.arraycopy(lLineData.getData(), 0, mCurrentImageBuffer, (lLineData.getLineIndex() - 1) * lLineData.getNbSamples(), lLineData.getNbSamples());
            mDataBufferSemaphore.release();

            ProbeCinematicConfiguration lProbeCinematic = mProbeCinematicProvider.getProbeCinematic(mDeviceConfiguration.getProbeCinematicName());
            rawDataPipeline(mRenderingContextController.getCurrentRenderingContext(), mDeviceConfiguration, lProbeCinematic, lCurrentImage);
        }
    }

    static {
        System.loadLibrary("renderingCpp");
    }

    public native void prepareRenderingContext(int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, float nh0, float nhp, float nsr, float ndx, float Rb, float l1, float tr1, float nspeed, float ndec, float ndelay);
    public native int[] render(int[] iImageInput, double linearQuantificationSlope, double linearQuantificationOffset);
}
