package com.echopen.asso.echopen.probe_communication.commands;

public enum StateMachineState {
        STATE_POWER_OFF(1),
        STATE_OFF_LINE(2),
        STATE_STANDBY(3),
        STATE_START_OF_SCANNING(4),
        STATE_SCANNING(5),
        STATE_TEST_PATTERN(6),
        STATE_ERROR_MODE(7);

    StateMachineState(int iStateMachineStateId){
        mStateMachineStateId = iStateMachineStateId;
    }

    public final int mStateMachineStateId;

    public static final StateMachineState fromId(int iId){
        for (StateMachineState lStateMachineState : StateMachineState.values()) {
            if(lStateMachineState.mStateMachineStateId == iId){
                return lStateMachineState;
            }
        }

        return STATE_ERROR_MODE;
    }
}
