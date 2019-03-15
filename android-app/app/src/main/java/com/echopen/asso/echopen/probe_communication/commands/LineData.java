package com.echopen.asso.echopen.probe_communication.commands;

public class LineData{
    private int mPrevLineTs; // timestamp in microseconds
    private int mPrevLineCalculationDuration; // timestamp in microseconds
    private int mPrevLineSendDuration; // timestamp in microseconds
    private int mNbSamples;
    private int mLineIndex;
    private int mChannelIndex;
    private int mFrame;
    private int[] mData;

    public LineData(int iPrevLineTs, int iPrevLineCalculationDuration, int iPrevLineSendDuration, int iNbSamples, int iLineIndex, int iChannelIndex, int iFrame, int[] iDataSamples) {
        mPrevLineTs = iPrevLineTs;
        mPrevLineCalculationDuration = iPrevLineCalculationDuration;
        mPrevLineSendDuration = iPrevLineSendDuration;
        mNbSamples = iNbSamples;
        mLineIndex = iLineIndex;
        mChannelIndex = iChannelIndex;
        mFrame = iFrame;
        mData = iDataSamples;
    }

    public String toString(){
        String lStr;
        lStr = "Data Line " + mPrevLineTs + " microseconds, calc duration " + mPrevLineCalculationDuration + " microseconds, send duration " + mPrevLineSendDuration+ " microseconds " +  mNbSamples + " " + mLineIndex + " " + mChannelIndex + " " + mFrame + "\n";
        for (int i = 0; i < mNbSamples; i ++){
            lStr += mData[i] + " ";
        }

        return lStr;
    }

    public int getNbSamples(){
        return mNbSamples;
    }

    public int getLineIndex(){
        return mLineIndex;
    }

    public int getChannelIndex(){
        return mChannelIndex;
    }

    public int getFrame(){
        return mFrame;
    }

    public int[] getData(){
        return mData;
    }
}

