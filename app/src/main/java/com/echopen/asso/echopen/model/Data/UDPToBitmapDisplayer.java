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
 * Created by mehdibenchoufi on 09/10/15.
 */
public class UDPToBitmapDisplayer extends Displayer {

    private Activity activity;

    private String ip;

    private int port;

    public UDPToBitmapDisplayer(Activity activity, MainActionController mainActionController, String ip, int port) throws IOException {
        super(activity, mainActionController);
        this.ip = ip;
        this.port = port;
        this.activity = activity;
        getUDPData();
    }

    private void getUDPData() throws IOException {

        class ProcessUPDTask extends AsyncTask<Void, Void, Void> {

            private Activity activity;

            private ScanConversion scanconversion;

            private DatagramSocket s;

            private int port;

            public ProcessUPDTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion, int port) throws IOException {
                this.scanconversion = scanConversion;
                this.activity = activity;
                this.port = port;
            }

            @Override
            protected Void doInBackground(Void... Voids) {
                int udpDataCounterColumn = 0;
                int udpDataCounterRow = 0;

                int[][] udpDataArray = new int[Constants.PreProcParam.tmp_NUM_LINES][Constants.PreProcParam.tmp_SAMPLING_POINTS];
                try {
                    try {
                        s = new DatagramSocket(port);
                        s.setSoTimeout(3000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    while (true) {
                        try {
                            byte[] message = new byte[Constants.PreProcParam.tmp_SAMPLING_POINTS];
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
                            if (udpDataCounterColumn >= Constants.PreProcParam.tmp_NUM_UDP_PACKET_CHUNKS) {
                                udpDataCounterRow++;
                                udpDataCounterColumn = 0;
                                Log.d("this is the line number ", String.valueOf(udpDataCounterRow));
                            }
                            if (udpDataCounterRow >= Constants.PreProcParam.tmp_NUM_LINES) {
                                int[] scannedArray = ScanConversion.getInstance(udpDataArray).getDataFromInterpolation();
                                EchoIntImage echoImage = new EchoIntImage(scannedArray);
                                udpDataCounterRow = 0;
                                final Bitmap bitmap = echoImage.createImage();
                                try {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mainActionController.displayMainFrame(bitmap);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
        new ProcessUPDTask(activity, mainActionController, scanConversion, port).execute();
    }
}
