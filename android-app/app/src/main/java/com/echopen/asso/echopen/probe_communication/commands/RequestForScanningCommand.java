package com.echopen.asso.echopen.probe_communication.commands;

public class RequestForScanningCommand extends Request {

    public RequestForScanningCommand(){
        mCommand = CommandType.REQUEST_FOR_SCANNING;
        mSize = 4;
    }
}

