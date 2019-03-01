package com.echopen.asso.echopen.probe_communication.commands;

public class RequestForStateCommand extends Request {

    public RequestForStateCommand(){
        mCommand = CommandType.REQUEST_FOR_STATE;
        mSize = 4;
    }
}

