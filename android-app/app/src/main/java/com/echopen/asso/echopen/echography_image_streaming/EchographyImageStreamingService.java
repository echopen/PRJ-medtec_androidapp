package com.echopen.asso.echopen.echography_image_streaming;

import android.app.Activity;
import android.graphics.Bitmap;

import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingConnectionType;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_streaming.notifications.EchographyImageStreamingNotification;
import com.echopen.asso.echopen.model.Data.ProcessTCPTask;
import com.echopen.asso.echopen.ui.RenderingContextController;

import java.util.Observable;

/**
 * Created by lecoucl on 05/06/17.
 *
 * @class service that connects to device, receives raw data, builds echography images and streams it
 * to echopen application
 */
public class EchographyImageStreamingService extends Observable {

    private final String TAG = this.getClass().getSimpleName();

    private EchographyImageStreamingMode mMode; /* streaming connection information */
    private RenderingContextController mRenderingContextController; /* rendering context controller */

    /**
     * @param iRenderingContextController rendering context controller
     * @brief constructor
     */
    public EchographyImageStreamingService(RenderingContextController iRenderingContextController) {
        mMode = null;
        mRenderingContextController = iRenderingContextController;
    }

    /**
     * @param iMode     device connection information
     * @param iActivity ideally to be removed
     * @brief connect service to device
     */
    public void connect(EchographyImageStreamingMode iMode, Activity iActivity) {
        if (iMode.getConnectionType() == EchographyImageStreamingConnectionType.Local) {
            // TODO:to be plugged
        } else if (iMode.getConnectionType() == EchographyImageStreamingConnectionType.Connected_to_device_TCP_protocol) {
            EchographyImageStreamingTCPMode lTCPMode = (EchographyImageStreamingTCPMode) iMode;
            mMode = lTCPMode;
            // start TCP receiver + Image builder thread
            new ProcessTCPTask(iActivity, mRenderingContextController, this, lTCPMode.getDeviceIp(), lTCPMode.getDevicePort()).execute();
        }
    }

    /**
     * @brief disconnect service from device
     */
    public void disconnect() {
        mMode = null;
    }

    /**
     * @param iImage bitmap image generated
     * @brief emit a new image notification event
     */
    public void emitNewImage(Bitmap iImage) {
        this.setChanged();
        this.notifyObservers(new EchographyImageStreamingNotification(iImage));
    }

    /**
     * @return rendering context controller
     * @brief getter for rendering context controller
     */
    public RenderingContextController getRenderingContextController() {
        return mRenderingContextController;
    }
}
