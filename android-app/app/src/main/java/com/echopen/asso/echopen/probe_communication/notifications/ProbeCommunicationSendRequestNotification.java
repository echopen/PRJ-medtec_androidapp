package com.echopen.asso.echopen.probe_communication.notifications;

import com.echopen.asso.echopen.probe_communication.commands.Request;

public class ProbeCommunicationSendRequestNotification {
    private Request mRequest;

    public ProbeCommunicationSendRequestNotification(Request iRequest){
        mRequest = iRequest;
    }

    public Request getRequest(){
        return mRequest;
    }

}
