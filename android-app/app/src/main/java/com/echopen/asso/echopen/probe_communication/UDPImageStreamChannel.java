package com.echopen.asso.echopen.probe_communication;

import android.os.AsyncTask;
import android.util.Log;

import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSocketStateNotification;
import com.echopen.asso.echopen.probe_communication.notifications.SocketState;

import org.greenrobot.eventbus.EventBus;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPImageStreamChannel extends AsyncTask<Void, Void, Void> {

    private final String TAG = this.getClass().getSimpleName();
    private DatagramSocket mSocket;

    private final int mPort;
    private boolean mSocketIsRunning;

    private final static int MESSAGE_MAX_BUFFER_SIZE = 16000;

    public UDPImageStreamChannel(int iPort) {
        mPort = iPort;
        mSocketIsRunning = true;
    }

    @Override
    protected Void doInBackground(Void... Voids) {

        try {
            mSocket = new DatagramSocket(mPort);
            mSocket.setSoTimeout(0);
            EventBus.getDefault().post(new ProbeCommunicationSocketStateNotification(SocketState.CONNECTED,"UDP"));

            // Task stay awake in background
            while(mSocketIsRunning){
                byte[] lMessage = new byte[MESSAGE_MAX_BUFFER_SIZE];
                DatagramPacket lPacket = new DatagramPacket(lMessage, lMessage.length);
                mSocket.receive(lPacket);
                Log.d(TAG, "Packet - " + lPacket.getData());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
