package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.echopen.asso.echopen.model.EchoImage.EchoIntImage;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.ImageHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by loic on 19/12/15.
 */
public class ProcessUPDTask extends AbstractDataTask {

    private DatagramSocket s;

    private int port;

    public ProcessUPDTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion, int port) throws IOException {
        super(activity, mainActionController, scanConversion);
        this.port = port;
    }

    /**
     * A Socket is opened once for all
     * Get the UDP data, converts it via the ScanConversion class, create a bitmap
     * refreshes the UI with the data in a new thread with the help of runOnUiThread() method
     * @param Voids
     * @return
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
                        Log.d("this is the line number ", String.valueOf(udpDataCounterRow));
                    }
                    if (udpDataCounterRow >= Constants.PreProcParam.UDP_IMG_DATA) {
                        ScanConversion scnConv = ScanConversion.getInstance(udpDataArray);
                        scnConv.setUdpData();
                        refreshUI(scnConv);
                        udpDataCounterRow = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute() {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}