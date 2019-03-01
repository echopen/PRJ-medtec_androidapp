package com.echopen.asso.echopen.probe_communication;

import com.echopen.asso.echopen.probe_communication.commands.Reply;
import com.echopen.asso.echopen.probe_communication.commands.Request;

import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSendBytesReplyNotification;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSendBytesRequestNotification;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

public class CommandManager {

    private static final String TAG = CommandManager.class.getSimpleName();
    private CommandInterpreter mCommandInterpreter;

    public CommandManager(CommandInterpreter iCommandInterpreter){
        mCommandInterpreter = iCommandInterpreter;
        EventBus.getDefault().register(this);
    }

    public void sendRequest(Request iRequest){
        byte[] lRequestBytes = mCommandInterpreter.serialize(iRequest);
        EventBus.getDefault().post(new ProbeCommunicationSendBytesRequestNotification(lRequestBytes));
    }

    public void receiveReply(byte[] iReplyBuffer, int iReplySize){
        Reply lReply = mCommandInterpreter.deserialize(Arrays.copyOfRange(iReplyBuffer, 0, iReplySize));
        processReply(lReply);
    }

    public void processReply(Reply iReply){

        // TODO: transmit events to presenters
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onReply(ProbeCommunicationSendBytesReplyNotification iReplyNotification) {
        receiveReply(iReplyNotification.getReply(), iReplyNotification.getReplySize());
    }


}
