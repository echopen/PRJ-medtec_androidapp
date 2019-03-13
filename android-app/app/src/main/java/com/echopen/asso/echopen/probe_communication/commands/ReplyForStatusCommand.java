package com.echopen.asso.echopen.probe_communication.commands;

public class ReplyForStatusCommand extends Reply{

    protected int mInternalProbeTemperature;  // Internal temperature of the probe in m°C
    protected int mBatteryLevel; // Battery level in %
    protected StateMachineState mStateMachineState;
    protected ErrorType mLastError;

    public ReplyForStatusCommand(int iSize, int iDuration, ErrorType iError, int iInternalProbeTemperature, int iBatteryLevel, StateMachineState iStateMachineState, ErrorType iLastError){
        super(iSize, iDuration, iError);
        mCommand = CommandType.REQUEST_FOR_STATUS;
        mInternalProbeTemperature = iInternalProbeTemperature;
        mBatteryLevel = iBatteryLevel;
        mStateMachineState = iStateMachineState;
        mLastError = iLastError;
    }

    public String toString(){
        return super.toString() + " " + mInternalProbeTemperature + " " + mBatteryLevel + " " + mStateMachineState + " " + mLastError;
    }
}
