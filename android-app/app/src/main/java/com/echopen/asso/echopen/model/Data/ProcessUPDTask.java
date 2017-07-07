package com.echopen.asso.echopen.model.Data;

import android.app.Activity;

import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.ImageHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by loic on 19/12/15.
 * This class catches the flow of data streaing through UDP protocol and then throws the data
 * to AbstrctDataTask class to refresh UI.
 */
public class ProcessUPDTask extends AbstractDataTask {

    private DatagramSocket s;

    private final int port;

    public ProcessUPDTask(Activity activity, RenderingContextController iRenderingContextController, int port) {
        super(activity, iRenderingContextController, null);
        this.port = port;
    }

    /**
     * A Socket is opened once for all
     * Get the UDP data, converts it via the ScanConversion class, create a bitmap
     * refreshes the UI with the data in a new thread with the help_activity of runOnUiThread() method
     * @param Voids, void argument
     * @return Void
     */
    @Override
    protected Void doInBackground(Void... Voids) {
        int udpDataCounterColumn = 0;
        int udpDataCounterRow = 0;

        int[][] udpDataArray = new int[Constants.PreProcParam.UDP_IMG_DATA][Constants.PreProcParam.UDP_NUM_SAMPLES];
        try {
            try {
                s = new DatagramSocket(port);
                s.setSoTimeout(3000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    byte[] message = new byte[Constants.PreProcParam.UDP_NUM_SAMPLES];
                    DatagramPacket p = new DatagramPacket(message, message.length);
                    try {
                        s.receive(p);
                        byte[] byteArray = p.getData();
                        int[] intArray = ImageHelper.convert(byteArray);
                        System.arraycopy(intArray, 0, udpDataArray[udpDataCounterRow], udpDataCounterColumn * intArray.length, intArray.length);
                        udpDataCounterColumn++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (udpDataCounterColumn >= Constants.PreProcParam.UDP_NUM_UDP_PACKET_CHUNKS) {
                        udpDataCounterRow++;
                        udpDataCounterColumn = 0;
                    }
                    if (udpDataCounterRow >= Constants.PreProcParam.UDP_IMG_DATA) {
                        RenderingContext lCurrentRenderingContext = mRenderingContextController.getCurrentRenderingContext();

                        ScanConversion scnConv = ScanConversion.getInstance(udpDataArray);
                        scnConv.setUdpData();
                        //refreshUI(scnConv, lCurrentRenderingContext);
                        udpDataCounterRow = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}