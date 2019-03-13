package com.echopen.asso.echopen.probe_communication.commands;

public class ReplyForTestPatternCommand extends Reply{

    public ReplyForTestPatternCommand(int iSize, int iDuration, ErrorType iError){
        super(iSize, iDuration, iError);
        mCommand = CommandType.REQUEST_FOR_TEST_PATTERN;
    }

}
