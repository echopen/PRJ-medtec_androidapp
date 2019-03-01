package com.echopen.asso.echopen.probe_communication.notifications;

public class ProbeCommunicationSendBytesReplyNotification {
    private byte[] mReply;
    private int mReplySize;

    public ProbeCommunicationSendBytesReplyNotification(byte[] iReply, int iReplySize){
        mReply = iReply;
        mReplySize = iReplySize;
    }

    public byte[] getReply(){
        return mReply;
    }

    public int getReplySize(){return mReplySize; }

}
