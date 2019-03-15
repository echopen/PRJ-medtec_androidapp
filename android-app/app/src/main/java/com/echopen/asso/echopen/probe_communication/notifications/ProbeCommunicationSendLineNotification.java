package com.echopen.asso.echopen.probe_communication.notifications;

import com.echopen.asso.echopen.probe_communication.commands.LineData;

public class ProbeCommunicationSendLineNotification {
    private LineData mLineData;

    public ProbeCommunicationSendLineNotification(LineData iLineData){
        mLineData = iLineData;
    }

    public LineData getLineData(){
        return mLineData;
    }
}
