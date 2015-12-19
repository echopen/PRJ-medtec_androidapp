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
 * This Class inherits form Displayer class. The latter connects with the UI of the MainActivity
 * handled by the MainActionController class.
 *
 * Specifically
 *
 * @todo: implement a code pattern protocol-agnostic.
 */
public class UDPToBitmapDisplayer extends Displayer {

    /* The MainActivity variable */
    private Activity activity;

    /* The ip to get the incoming UDP data */
    private String ip;

    /* The port on which listening the incoming UDP data */
    private int port;

    /**
     *
     * @param activity, practically the MainActivity
     * @param mainActionController, holds the UI of the MainActivity
     * @param ip, ip to get the incoming UDP data
     * @param port, port on which listening the incoming UDP data
     * @throws IOException
     */
    public UDPToBitmapDisplayer(Activity activity, MainActionController mainActionController, String ip, int port) throws IOException {
        super(activity, mainActionController);
        this.ip = ip;
        this.port = port;
        this.activity = activity;
        getUDPData();
    }

    /**
     * This method does  many things
     *  - listening to the incoming data on $ip:$port
     *  - gives the data to the ScanConversion class in order to get the scan converted data
     *  - transforms the scan converted data into an image via EchoIntImage class
     *  - creates the bitmap from some instance of EchoIntImage
     *  - refreshes the UI with the data in a new thread
     *  This is done via an AsyncTask because you can't play with UDP in the main thread
     * @throws IOException
     */
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
                                ScanConversion scnConv = ScanConversion.getInstance(udpDataArray);
                                scnConv.setData(null);
                                int[] scannedArray = scnConv.getDataFromInterpolation();
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