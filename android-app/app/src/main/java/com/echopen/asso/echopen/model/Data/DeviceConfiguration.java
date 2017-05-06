package com.echopen.asso.echopen.model.Data;

/**
 * Created by lecoucl on 15/04/17.
 */
public class DeviceConfiguration {

    private double mR0; // measure initial depth in m
    private double mRf; // measure final depth in m
    private float mProbeSectorAngle; // measure sector angle in degree
    private double mSamplingFrequency; // sampling frequency in Hz
    private int mNbLinesPerImage;
    private int mNbSamplesPerLine;

    public DeviceConfiguration(double iR0, double iRf, float iProbeSectorAngle, double iSamplingFrequency, int iNbLinePerImage, int iNbSamplesPerLine) {
        mR0 = iR0;
        mRf = iRf;
        mProbeSectorAngle = iProbeSectorAngle;
        mSamplingFrequency = iSamplingFrequency;
        mNbLinesPerImage = iNbLinePerImage;
        mNbSamplesPerLine = iNbSamplesPerLine;
    }

    public double getR0(){
        return mR0;
    }

    public double getRf(){
        return mRf;
    }

    public float getProbeSectorAngle(){
        return mProbeSectorAngle;
    }

    public double getSamplingFrequency(){
        return mSamplingFrequency;
    }

    public int getNbSamplesPerLine(){
        return mNbSamplesPerLine;
    }

    public int getNbLinesPerImage(){
        return mNbLinesPerImage;
    }
}
