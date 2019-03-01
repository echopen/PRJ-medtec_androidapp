package com.echopen.asso.echopen.probe_communication;

import android.os.AsyncTask;

import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSendBytesReplyNotification;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSendBytesRequestNotification;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSocketStateNotification;
import com.echopen.asso.echopen.probe_communication.notifications.SocketState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TCPCommandChannel extends AsyncTask<Void, Void, Void> {

    private final String TAG = this.getClass().getSimpleName();
    private Socket mSocket;

    private final String mIp;
    private final int mPort;

    private boolean mSocketIsRunning;

    private final static int MESSAGE_MAX_BUFFER_SIZE = 100;

    public TCPCommandChannel(String iIp, int iPort) {
        mIp = iIp;
        mPort = iPort;
        mSocketIsRunning = true;
        EventBus.getDefault().register(this);
    }

    protected Void doInBackground(Void... Voids) {
        try {
            mSocket = new Socket(mIp, mPort);

            EventBus.getDefault().post(new ProbeCommunicationSocketStateNotification(SocketState.CONNECTED,"TCP"));

            // Task stay awake in background
            while(mSocketIsRunning){
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @brief send request to the echOpen probe and wait for reply
     *
     * @param iRequest request to be sent
     */
    private void sendRequest(byte[] iRequest){
        if(mSocket.isConnected()){
            try {
                OutputStream lOutputStream = mSocket.getOutputStream();
                lOutputStream.write(iRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                DataInputStream lIn = new DataInputStream(mSocket.getInputStream());
                byte[] lReply = new byte[MESSAGE_MAX_BUFFER_SIZE];
                int lMessageSize = lIn.read(lReply);

                EventBus.getDefault().post(new ProbeCommunicationSendBytesReplyNotification(lReply, lMessageSize));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onReply(ProbeCommunicationSendBytesRequestNotification iRequestNotification) {
        sendRequest(iRequestNotification.getRequest());
    }

}
