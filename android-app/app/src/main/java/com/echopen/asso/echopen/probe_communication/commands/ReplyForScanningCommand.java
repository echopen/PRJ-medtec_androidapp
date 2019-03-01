package com.echopen.asso.echopen.probe_communication.commands;

public class ReplyForScanningCommand extends Reply{

    public ReplyForScanningCommand(ErrorType iError){
        mCommand = CommandType.REQUEST_FOR_SCANNING;
        mError = iError;
    }
}
