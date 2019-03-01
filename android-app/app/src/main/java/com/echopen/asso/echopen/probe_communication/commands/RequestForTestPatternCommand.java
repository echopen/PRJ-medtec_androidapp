package com.echopen.asso.echopen.probe_communication.commands;

public class RequestForTestPatternCommand extends Request {

    private PatternType mPatternType;
    private int mFrameInterval; //in micro seconds
    private int mLinePerFrame;
    private int mLineInterval; //in micro seconds
    private int mSamplesPerLine;
    private int mBitsPerSample;

    public RequestForTestPatternCommand(PatternType iPatternType, int iFrameInterval, int iLinePerFrame, int iLineInterval, int iSamplesPerLine, int iBitsPerSample){
        mCommand = CommandType.REQUEST_FOR_TEST_PATTERN;
        mSize = 28;
        mPatternType = iPatternType;
        mFrameInterval = iFrameInterval;
        mLinePerFrame = iLinePerFrame;
        mLineInterval = iLineInterval;
        mSamplesPerLine = iSamplesPerLine;
        mBitsPerSample = iBitsPerSample;
    }

    public PatternType getPatternType(){
        return mPatternType;
    }

    public int getFrameInterval(){
        return mFrameInterval;
    }

    public int getLinePerFrame(){
        return mLinePerFrame;
    }

    public int getLineInterval(){
        return mLineInterval;
    }

    public int getSamplesPerLine(){
        return mSamplesPerLine;
    }

    public int getBitsPerSample(){
        return mBitsPerSample;
    }
}

