package com.echopen.asso.echopen.probe_communication.commands;

public class RequestForStatusCommand extends Request {

    public RequestForStatusCommand(){
        mCommand = CommandType.REQUEST_FOR_STATUS;
        mSize = 2;
    }
}

