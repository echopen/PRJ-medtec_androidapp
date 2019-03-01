package com.echopen.asso.echopen.probe_communication.commands;

public class ReplyForTestPatternCommand extends Reply{

    public ReplyForTestPatternCommand(ErrorType iError){
        mCommand = CommandType.REQUEST_FOR_TEST_PATTERN;
        mError = iError;
    }

}
