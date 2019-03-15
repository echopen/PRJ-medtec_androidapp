package com.echopen.asso.echopen.probe_communication;

import android.os.AsyncTask;

import com.echopen.asso.echopen.probe_communication.commands.LineData;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSendLineNotification;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSocketStateNotification;
import com.echopen.asso.echopen.probe_communication.notifications.SocketState;

import org.greenrobot.eventbus.EventBus;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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
                LineData lLineData = parsePacket(lPacket.getData());
                EventBus.getDefault().post(new ProbeCommunicationSendLineNotification(lLineData));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private LineData parsePacket(byte[] iPacket){
        ByteBuffer lBb = ByteBuffer.wrap(iPacket);
        int lPrevLineTs = lBb.getInt();
        int lPrevLineCalculationDuration = lBb.getInt();
        int lPrevLineSendDuration = lBb.getInt();

        byte[] lNbSamplesBytes = new byte[4];
        lNbSamplesBytes[0] = iPacket[12];
        lNbSamplesBytes[1] = iPacket[13];
        lNbSamplesBytes[2] = iPacket[14];

        lNbSamplesBytes[3] = 0;
        int lNbSamples = ByteBuffer.wrap(lNbSamplesBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        int lLineIndex = (int) iPacket[15];
        int lChannelIndex = (int) iPacket[16];
        int lFrame = (int) iPacket[17];

        byte [] u8_data = Arrays.copyOfRange(iPacket, 18, 2*lNbSamples + 18);// samples received as a 8bits array even if sample coded on 8/12/16 bits

        int[] lDataSamples = new int[lNbSamples];
        for(int i = 0; i < lNbSamples; i++)
        {
            lDataSamples[i] = (int) (u8_data[2*i] << 8) | (u8_data[2*i + 1] & 0x00ff);
        }

        return new LineData(lPrevLineTs, lPrevLineCalculationDuration, lPrevLineSendDuration,  lNbSamples, lLineIndex, lChannelIndex, lFrame, lDataSamples);
    }

}
