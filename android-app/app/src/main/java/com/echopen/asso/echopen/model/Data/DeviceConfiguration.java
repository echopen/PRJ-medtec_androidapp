package com.echopen.asso.echopen.model.Data;

/**
 * Created by lecoucl on 15/04/17.
 */
public class DeviceConfiguration {

    private float mR0; // measure initial depth in m
    private float mRf; // measure final depth in m
    private float mProbeSectorAngle; // measure sector angle in degree
    private int mNbLinesPerImage;
    private int mNbSamplesPerLine;
    private String mProbeCinematicName;
    private float mEchoDelay; // echo delay measurement in milliseconds
    private float mDecimation; // echo decimation

    public DeviceConfiguration(float iR0, float iRf, float iProbeSectorAngle, int iNbLinePerImage, int iNbSamplesPerLine, String iProbeCinematicName, float iEchoDelay, float iDecimation) {
        mR0 = iR0;
        mRf = iRf;
        mProbeSectorAngle = iProbeSectorAngle;
        mNbLinesPerImage = iNbLinePerImage;
        mNbSamplesPerLine = iNbSamplesPerLine;
        mProbeCinematicName = iProbeCinematicName;
        mEchoDelay = iEchoDelay;
        mDecimation = iDecimation;
    }

    public float getR0(){
        return mR0;
    }

    public float getRf(){
        return mRf;
    }

    public float getProbeSectorAngle(){
        return mProbeSectorAngle;
    }

    public int getNbSamplesPerLine(){
        return mNbSamplesPerLine;
    }

    public int getNbLinesPerImage(){
        return mNbLinesPerImage;
    }

    public String getProbeCinematicName() {
        return mProbeCinematicName;
    }

    public float getDecimation(){
        return mDecimation;
    }

    public float getEchoDelay(){
        return mEchoDelay;
    }
}
