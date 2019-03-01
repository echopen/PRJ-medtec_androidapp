package com.echopen.asso.echopen.probe_communication.commands;

public class ReplyForStateCommand extends Reply{

    protected float mInternalProbeTemperature;
    protected float mBatteryLevel;
    protected StateMachineState mStateMachineState;
    protected ErrorType mLastError;

    public ReplyForStateCommand(ErrorType iError, float iInternalProbeTemperature, float iBatteryLevel, StateMachineState iStateMachineState, ErrorType iLastError){
        mCommand = CommandType.REQUEST_FOR_STATE;
        mError = iError;
        mInternalProbeTemperature = iInternalProbeTemperature;
        mBatteryLevel = iBatteryLevel;
        mStateMachineState = iStateMachineState;
        mLastError = iLastError;
    }
}
