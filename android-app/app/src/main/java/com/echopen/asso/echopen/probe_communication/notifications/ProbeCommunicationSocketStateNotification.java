package com.echopen.asso.echopen.probe_communication.notifications;


public class ProbeCommunicationSocketStateNotification {
    private SocketState mSocketState;
    private String mSocketName;

    public ProbeCommunicationSocketStateNotification(SocketState iSocketState, String iSocketName){
        mSocketState = iSocketState;
        mSocketName = iSocketName;
    }

    public SocketState getSocketState(){
        return mSocketState;
    }

    public String getSocketName(){ return mSocketName; }

}
