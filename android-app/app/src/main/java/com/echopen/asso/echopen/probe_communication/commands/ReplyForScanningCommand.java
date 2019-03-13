package com.echopen.asso.echopen.probe_communication.commands;

public class ReplyForScanningCommand extends Reply{

    public ReplyForScanningCommand(int iSize, int iDuration, ErrorType iError){
        super(iSize, iDuration, iError);
        mCommand = CommandType.REQUEST_FOR_SCANNING;
    }
}
