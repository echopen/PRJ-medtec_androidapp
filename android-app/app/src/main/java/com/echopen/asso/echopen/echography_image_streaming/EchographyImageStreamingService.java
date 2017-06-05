package com.echopen.asso.echopen.echography_image_streaming;

import android.app.Activity;

import com.echopen.asso.echopen.model.Data.ProcessTCPTask;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;

/**
 * Created by lecoucl on 05/06/17.
 *
 * @class service that connects to device, receives raw data, builds echography images and streams it
 * to echopen application
 */
public class EchographyImageStreamingService {

    EchographyImageStreamingMode mMode; /* streaming connection information */
    RenderingContextController mRenderingContextController; /* rendering context controller */

    /**
     * @brief constructor
     * @param iRenderingContextController
     */
    public EchographyImageStreamingService(RenderingContextController iRenderingContextController){
        mMode = null;
        mRenderingContextController = iRenderingContextController;
    }

    /**
     * @brief connect service to device
     *
     * @param iMode device connection information
     *
     * @param iActivity to be removed
     * @param mainActionController to be removed
     */
    public void connect(EchographyImageStreamingMode iMode, Activity iActivity, MainActionController mainActionController){
        if(iMode.getConnectionType() == EchographyImageStreamingConnectionType.Local){
            // TODO:to be plugged
        }
        else if(iMode.getConnectionType() == EchographyImageStreamingConnectionType.Connected_to_device_TCP_protocol){
            EchographyImageStreamingTCPMode lTCPMode = (EchographyImageStreamingTCPMode) iMode;
            new ProcessTCPTask(iActivity, mainActionController, mRenderingContextController, lTCPMode.getDeviceIp(), lTCPMode.getDevicePort()).execute();
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
     * @brief getter for rendering context controller
     *
     * @return rendering context controller
     */
    public RenderingContextController getRenderingContextController(){
        return mRenderingContextController;
    }
}
