package com.echopen.asso.echopen.probe_communication.notifications;

public class ProbeCommunicationSendBytesRequestNotification {
    private byte[] mRequest;

    public ProbeCommunicationSendBytesRequestNotification(byte[] iRequest){
        mRequest = iRequest;
    }

    public byte[] getRequest(){
        return mRequest;
    }

}
