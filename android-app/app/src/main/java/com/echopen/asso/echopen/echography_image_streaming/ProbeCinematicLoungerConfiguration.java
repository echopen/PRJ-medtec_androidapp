package com.echopen.asso.echopen.echography_image_streaming;


import com.echopen.asso.echopen.echography_image_streaming.ProbeCinematicConfiguration;

public class ProbeCinematicLoungerConfiguration extends ProbeCinematicConfiguration {

    // TODO: add sketch from probe cinematic with associated distance
    public float mNh0; //rotation axis / chariot distance in mm
    public float mNhp; // transducer surface / probe nose distance in mm
    public float mNsr; // sampling rate in MHz
    public float mNdx; // chariot translation length in mm
    public float mRb; //rotation axis / transudcer surface distance in mm
    public float mL1; // lounger axis / transducer center distance in mm
    public float mTr1; // lounger angle axis / transducer axis in mm

    ProbeCinematicLoungerConfiguration(String iName, float iNh0, float iNhp, float iNsr, float iNdx, float iRb, float iL1, float iTr1){
        super(iName);
        mNh0 = iNh0;
        mNhp = iNhp;
        mNsr = iNsr;
        mNdx = iNdx;
        mRb = iRb;
        mL1 = iL1;
        mTr1 = iTr1;
    }
}
