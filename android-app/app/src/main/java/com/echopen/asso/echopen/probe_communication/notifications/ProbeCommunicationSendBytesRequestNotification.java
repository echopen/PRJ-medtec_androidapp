package com.echopen.asso.echopen.probe_communication.notifications;

/**
 *
 * @class notification event sent to Application when a new image is generated by ImageStreamingService
 */

public class ProbeCommunicationSendBytesRequestNotification {
    private byte[] mRequest;

    public ProbeCommunicationSendBytesRequestNotification(byte[] iRequest){
        mRequest = iRequest;
    }

    public byte[] getRequest(){
        return mRequest;
    }

}