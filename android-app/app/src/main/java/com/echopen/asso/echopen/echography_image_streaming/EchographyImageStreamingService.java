package com.echopen.asso.echopen.echography_image_streaming;

import android.app.Activity;
import android.graphics.Bitmap;

import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingConnectionType;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_streaming.notifications.EchographyImageStreamingNotification;
import com.echopen.asso.echopen.model.Data.ProbeCinematicProvider;
import com.echopen.asso.echopen.model.Data.ProcessTCPTask;
import com.echopen.asso.echopen.ui.RenderingContextController;

import org.greenrobot.eventbus.EventBus;

import java.util.Observable;

/**
 * Created by lecoucl on 05/06/17.
 *
 * @class service that connects to device, receives raw data, builds echography images and streams it
 * to echopen application
 */
public class EchographyImageStreamingService extends Observable{

    private final String TAG = this.getClass().getSimpleName();

    private EchographyImageStreamingMode mMode; /* streaming connection information */
    private RenderingContextController mRenderingContextController; /* rendering context controller */
    private ProbeCinematicProvider mProbeCinematicProvider; /* probe cinematic provider */

    /**
     * @brief constructor
     *
     * @param iRenderingContextController rendering context controller
     */
    public EchographyImageStreamingService(RenderingContextController iRenderingContextController, ProbeCinematicProvider iProbeCinematicProvider){
        mMode = null;
        mRenderingContextController = iRenderingContextController;
        mProbeCinematicProvider = iProbeCinematicProvider;
    }

    /**
     * @brief connect service to device
     *
     * @param iMode device connection information
     *
     */
    public void connect(EchographyImageStreamingMode iMode){
        if(iMode.getConnectionType() == EchographyImageStreamingConnectionType.Local){
            // TODO:to be plugged
        }
        else if(iMode.getConnectionType() == EchographyImageStreamingConnectionType.Connected_to_device_TCP_protocol){
            EchographyImageStreamingTCPMode lTCPMode = (EchographyImageStreamingTCPMode) iMode;
            mMode = lTCPMode;
            // start TCP receiver + Image builder thread
            new ProcessTCPTask(mRenderingContextController, mProbeCinematicProvider, this, lTCPMode.getDeviceIp(), lTCPMode.getDevicePort()).execute();
        }
    }

    /**
     * @brief disconnect service from device
     *
     */
    public void disconnect(){
        mMode = null;
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
}
