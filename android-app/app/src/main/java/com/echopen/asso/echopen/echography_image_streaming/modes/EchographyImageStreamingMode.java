package com.echopen.asso.echopen.echography_image_streaming.modes;

/**
 * Created by clecoued on 05/06/17.
 *
 * @class provide connection type and extra connection information in order to pair with device
 */

public abstract class EchographyImageStreamingMode {
    protected EchographyImageStreamingConnectionType mConnectionType; /* device connection type */

    /**
     * @brief getter for connection type
     *
     * @return device connection type
     */
    public EchographyImageStreamingConnectionType getConnectionType(){
        return mConnectionType;
    }

    /**
     * @brief setter for connection type
     *
     * @param iConnectionType device connection type
     */
    public void setConnectionType(EchographyImageStreamingConnectionType iConnectionType){
        mConnectionType = iConnectionType;
    }
}
