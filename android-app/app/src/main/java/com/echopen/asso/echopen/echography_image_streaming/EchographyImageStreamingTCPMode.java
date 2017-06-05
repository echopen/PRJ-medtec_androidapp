package com.echopen.asso.echopen.echography_image_streaming;

/**
 * Created by lecoucl on 05/06/17.
 *
 * @class device connection information when connection is relying on TCP/IP protocol
 */
public class EchographyImageStreamingTCPMode extends EchographyImageStreamingMode {
    private String mDeviceIp; /** device Ip */
    private int mDevicePort; /** device port */

    /**
     * @brief constructor
     *
     * @param iDeviceIp to be connected device ip
     * @param iDevicePort to be connected device port
     */
    public EchographyImageStreamingTCPMode(String iDeviceIp, int iDevicePort){
        mDeviceIp = iDeviceIp;
        mDevicePort = iDevicePort;
    }

    /**
     * @brief getter - device ip
     * @return device ip
     */
    public String getDeviceIp(){
        return mDeviceIp;
    }

    /**
     * @brief getter - device port
     * @return device port
     */
    public int getDevicePort(){
        return mDevicePort;
    }

}
