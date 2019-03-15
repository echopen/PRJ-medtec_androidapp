package com.echopen.asso.echopen.model.Data;


import java.util.ArrayList;

public class ProbeCinematicProvider {

    private ArrayList<ProbeCinematicConfiguration> mProbeCinematicList;

    public ProbeCinematicProvider()
    {
        mProbeCinematicList = new ArrayList<>();
        // add Probe v1 existing cinematic

        mProbeCinematicList.add(new ProbeCinematicLoungerConfiguration(ProbeCinematicEnum.PROBE_V1__3_5MHZ.mName, 10.f, 0.f, 80.f, 16.f, 12.5f, -6.2658f, -10.f));
        mProbeCinematicList.add(new ProbeCinematicLoungerConfiguration(ProbeCinematicEnum.PROBE_V1__5MHZ.mName, 10.f, 0.f, 80.f, 16.f, 12.5f, 6.2658f, 10.f));
        mProbeCinematicList.add(new ProbeCinematicLinearDisplacementConfiguration(ProbeCinematicEnum.PROBE_V1__8MHZ.mName, 10.f, 0.f, 80.f, 11.547f));

    }


    public ProbeCinematicConfiguration getProbeCinematic(String iProbeCinematicName){
        for(ProbeCinematicConfiguration lProbeCinematic : mProbeCinematicList){
            if(lProbeCinematic.mName.equals(iProbeCinematicName)){
                return lProbeCinematic;
            }
        }

        return null;
    }
}
