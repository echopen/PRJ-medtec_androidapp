package com.echopen.asso.echopen.probe_communication.commands;

public enum ErrorType {
    ERR_NOERROR(0),                       // No error returned.
    ERR_UNKNOWN (-1),                     // Unspecified error.
    ERR_NVS_FLASH_INIT(-2),               // Internal flash memory initialization error.
    ERR_WIFI_START_AP(-3),                // WIFI start in AP mode error.
    ERR_SYSTEM_ERROR(-4),                 // System error. Creation of tasks, malloc, semaphore,... etc.
    ERR_REQUEST_INVALID_STATE(-5),        // Error, the received request is invalid in this state.
    ERR_BIT_NOT_SET(-6),                  // Error, the bit was not set.
    ERR_UNKNOWN_CONF_TYPE(-7),            // Error, configuration type unknown.
    ERR_MOTOR_INIT(-8),                   // Error, mechanical origin not reached within the time limit.
    ERR_REQUEST_INVALID_PARAM(-9),        // Error, the received request contains invalid parameters.
    ERR_NOT_IMPL(-10),                    // Function not yet implemented.
    ERR_REQUEST_UNKNOWN(-11),             // Error, the received request is unknown.
    ERR_RTOS(-12),                        // Error, intern FreeRTOS error (memory alloc, task creation, ...).
    ERR_REQUEST_SIZE(-13);

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

        return ERR_UNKNOWN;
    }
}
