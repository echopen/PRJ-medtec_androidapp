package com.echopen.asso.echopen.probe_communication.commands;

public class Request {
    protected CommandType mCommand;
    protected int mSize; // request message size

    public Request(){
        mCommand = CommandType.REQUEST_UNKNOWN;
        mSize = 0;
    }

    public CommandType getCommand(){
        return mCommand;
    }

    public int getSize(){
        return mSize;
    }
}
