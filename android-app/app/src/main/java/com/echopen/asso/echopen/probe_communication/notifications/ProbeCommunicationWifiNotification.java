package com.echopen.asso.echopen.probe_communication.notifications;


public class ProbeCommunicationWifiNotification {
    private WifiState mState;

    public ProbeCommunicationWifiNotification(WifiState iState){
        mState = iState;
    }

    public WifiState getState(){
        return mState;
    }

}
