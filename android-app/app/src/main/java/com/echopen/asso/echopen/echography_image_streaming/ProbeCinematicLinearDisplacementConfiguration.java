package com.echopen.asso.echopen.echography_image_streaming;


import com.echopen.asso.echopen.echography_image_streaming.ProbeCinematicConfiguration;

public class ProbeCinematicLinearDisplacementConfiguration extends ProbeCinematicConfiguration {

    // TODO: add sketch from probe cinematic with associated distance
    public float mNh0; //rotation axis / chariot distance in mm
    public float mNhp; // transducer surface / probe nose distance in mm
    public float mNsr; // sampling rate in MHz
    public float mNdx; // chariot translation length in mm

    ProbeCinematicLinearDisplacementConfiguration(String iName, float iNh0, float iNhp, float iNsr, float iNdx) {
        super(iName);
        mNh0 = iNh0;
        mNhp = iNhp;
        mNsr = iNsr;
        mNdx = iNdx;
    }
}
