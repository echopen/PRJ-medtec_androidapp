package com.echopen.asso.echopen.probe_communication.commands;

public class Reply {
    protected CommandType mCommand;
    protected ErrorType mError;

    public Reply(){
        mCommand = CommandType.REQUEST_UNKNOWN;
    }
}
