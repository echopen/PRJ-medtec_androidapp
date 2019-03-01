package com.echopen.asso.echopen.probe_communication;

import com.echopen.asso.echopen.probe_communication.commands.CommandType;
import com.echopen.asso.echopen.probe_communication.commands.ErrorType;
import com.echopen.asso.echopen.probe_communication.commands.PatternType;
import com.echopen.asso.echopen.probe_communication.commands.Reply;
import com.echopen.asso.echopen.probe_communication.commands.ReplyForScanningCommand;
import com.echopen.asso.echopen.probe_communication.commands.ReplyForStateCommand;
import com.echopen.asso.echopen.probe_communication.commands.ReplyForTestPatternCommand;
import com.echopen.asso.echopen.probe_communication.commands.Request;
import com.echopen.asso.echopen.probe_communication.commands.RequestForTestPatternCommand;
import com.echopen.asso.echopen.probe_communication.commands.StateMachineState;

import java.nio.ByteBuffer;

public class CommandInterpreter {

    private final static String TAG = CommandInterpreter.class.getSimpleName();

    /**
     * @brief serialize request to byte buffer
     * @param iRequest input request
     *
     * @return byte buffer
     */

    public byte[] serialize(Request iRequest){
         ByteBuffer lBuffer = ByteBuffer.allocate(iRequest.getSize());
         if(iRequest.getCommand() == CommandType.REQUEST_FOR_SCANNING || iRequest.getCommand() == CommandType.REQUEST_FOR_STATE){
             lBuffer.putInt(iRequest.getCommand().mCommandTypeId);
         }
         else if(iRequest.getCommand() == CommandType.REQUEST_FOR_TEST_PATTERN){
             RequestForTestPatternCommand lTestPatRequest = (RequestForTestPatternCommand) iRequest;
             lBuffer.putInt(lTestPatRequest.getCommand().mCommandTypeId);
             lBuffer.putInt(lTestPatRequest.getPatternType().mPatternTypeId);
             lBuffer.putInt(lTestPatRequest.getFrameInterval());
             lBuffer.putInt(lTestPatRequest.getLinePerFrame());
             lBuffer.putInt(lTestPatRequest.getLineInterval());
             lBuffer.putInt(lTestPatRequest.getSamplesPerLine());
             lBuffer.putInt(lTestPatRequest.getBitsPerSample());
         }

         return lBuffer.array();
    }

    /**
     * @brief deserialize byte buffer to reply
     * @param iReply input byte buffer
     *
     * @return reply
     */
    public Reply deserialize(byte[] iReply){

        ByteBuffer lBuffer = ByteBuffer.wrap(iReply);

        CommandType lReplyType = CommandType.fromId(lBuffer.getInt());


        if(lReplyType == CommandType.REQUEST_FOR_SCANNING){
            ErrorType lErrorType = ErrorType.fromId(lBuffer.getInt());
            return new ReplyForScanningCommand(lErrorType);
        }
        else if(lReplyType == CommandType.REQUEST_FOR_TEST_PATTERN){
            ErrorType lErrorType = ErrorType.fromId(lBuffer.getInt());
            return new ReplyForTestPatternCommand(lErrorType);
        }
        else if(lReplyType == CommandType.REQUEST_FOR_STATE){
            ErrorType lErrorType = ErrorType.fromId(lBuffer.getInt());
            float lProbeTemperature = lBuffer.getFloat();
            float lBatteryLevel = lBuffer.getFloat();
            StateMachineState lStateMachineState = StateMachineState.fromId(lBuffer.getInt());
            ErrorType lLastErrorType = ErrorType.fromId(lBuffer.getInt());
            return new ReplyForStateCommand(lErrorType, lProbeTemperature, lBatteryLevel, lStateMachineState, lLastErrorType);
        }

        return new Reply();

    }
}
