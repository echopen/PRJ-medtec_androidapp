package com.echopen.asso.echopen.probe_communication.commands;

public enum PatternType {
    PAT_BLACK(0),      // all sample values at 0
    PAT_GRAY(1),       // all sample  values at 1023 (2^10)
    PAT_WHITE(2),      // all sample values at 2047(2^11) - values are stored on 12bits in the probe
    PAT_GRID(3);

    PatternType(int iPatternTypeId){
        mPatternTypeId = iPatternTypeId;
    }

    public final int mPatternTypeId;

    public static final PatternType fromId(int iId){
        for (PatternType lPattern : PatternType.values()) {
            if(lPattern.mPatternTypeId == iId){
                return lPattern;
            }
        }

        return PAT_BLACK;
    }
}
