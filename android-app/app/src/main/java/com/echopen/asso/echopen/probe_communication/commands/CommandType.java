package com.echopen.asso.echopen.probe_communication.commands;

public enum CommandType {
    REQUEST_UNKNOWN(0),
    REQUEST_FOR_SCANNING(1),
    REQUEST_FOR_STATUS(2),
    REQUEST_GET_CONFIG(3),
    REQUEST_SET_CONFIG(4),
    REQUEST_POWER_OFF(5),
    REQUEST_STOP_SCANNING(6),
    REQUEST_FOR_TEST_PATTERN(7),
    REQUEST_STOP_TEST_PATTERN(8),
    REQUEST_FOR_DIAGNOSTIC(9);

    CommandType(int iCommandTypeId){
        mCommandTypeId = iCommandTypeId;
    }

    public final int mCommandTypeId;

    public static final CommandType fromId(int iId){
        for (CommandType lCommand : CommandType.values()) {
            if(lCommand.mCommandTypeId == iId){
                return lCommand;
            }
        }

        return REQUEST_UNKNOWN;
    }
}
