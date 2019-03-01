package com.echopen.asso.echopen.probe_communication.commands;

public enum ErrorType {

    ERR_NO_ERROR(0),
    ERR_NVS_FLASH_INIT(-1),
    ERR_WIFI_START_AP(-2),
    ERR_SYSTEM_ERROR(-3),
    ERR_REQUEST_INVALID_STATE(-4),
    ERR_BIT_NOT_SET(-5),
    ERR_UNKNOWN_CONF_TYPE(-6),
    ERR_ORIGIN_NOT_FOUND(-7),
    ERR_REQUEST_INVALID_PARAMETER(-8);

    ErrorType(int iErrorTypeId){
        mErrorTypeId = iErrorTypeId;
    }

    public final int mErrorTypeId;

    public static final ErrorType fromId(int iId){
        for (ErrorType lError : ErrorType.values()) {
            if(lError.mErrorTypeId == iId){
                return lError;
            }
        }

        return ERR_ORIGIN_NOT_FOUND;
    }
}
