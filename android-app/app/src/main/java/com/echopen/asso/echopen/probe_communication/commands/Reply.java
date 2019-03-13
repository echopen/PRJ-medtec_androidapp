package com.echopen.asso.echopen.probe_communication.commands;

public class Reply {
    protected CommandType mCommand;
    protected ErrorType mError;
    protected int mSize; // reply message size in 32bits word
    protected int mDuration; // execution duration in microseconds

    public Reply(){
        mCommand = CommandType.REQUEST_UNKNOWN;
        mSize = 1;
        mDuration = 0;
    }

    public Reply(int iSize, int iDuration, ErrorType iError){
        mSize = iSize;
        mDuration = iDuration;
        mError = iError;
    }

    public CommandType getCommand(){
        return mCommand;
    }

    public ErrorType getError(){
        return mError;
    }

    public int getSize(){ return mSize; }

    public int getDuration() { return mDuration; }

    public String toString(){
        return mCommand + " " + mSize + " " + mError + " " + mDuration + " microseconds";
    }
}
