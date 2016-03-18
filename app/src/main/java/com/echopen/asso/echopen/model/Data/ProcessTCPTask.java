package com.echopen.asso.echopen.model.Data;

/**
 * Created by mehdibenchoufi on 07/03/16.
 */

import android.app.Activity;
import android.util.Log;

import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

public class ProcessTCPTask extends AbstractDataTask {
    private Socket s;

    private String ip;
    private int port;

    public ProcessTCPTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion, String ip, int port) throws IOException {
        super(activity, mainActionController, scanConversion);
        this.ip = ip;
        this.port = port;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    protected Void doInBackground(Void... Voids) {
        InputStream stream;

        try {

            /*byte[] message0 = new byte[128 * 1024];

            for (int i = 0; i < 128; i++) {
                for (int j = 0; j < 1024; j++) {
                    message0[i * 1024 + j] = (byte) 250;
                }
            }
            long time = System.nanoTime();
            ScanConversion scnConv0 = ScanConversion.getInstance(message0);
            long completedIn = System.nanoTime() - time;
            //Log.d("this is time 1 ", String.valueOf(completedIn));

            time = System.nanoTime();
            scnConv0.setTcpData();
            completedIn = System.nanoTime() - time;
            //Log.d("this is time 2 ", String.valueOf(completedIn));

            //time = System.currentTimeMillis();
            for (int i = 0; i < 50; i++) {
                time = System.nanoTime();
                refreshUI(scnConv0);
                completedIn = System.nanoTime() - time;
                Log.d("this is wouau time", String.valueOf(completedIn));
            }*/
            //completedIn = System.currentTimeMillis() - time;
            //Log.d("this is time 3", String.valueOf(completedIn));

            s = new Socket(ip, port);
            stream = s.getInputStream();
            int num_lines = 128;
            int num_data = 1024;
            byte[] message = new byte[128 * 1024];

            while (true) {
                try {
                    long time = System.nanoTime();
                    message = deepInsidePacket(1025, stream);
                    long completedIn = System.nanoTime() - time;
                    //Log.d("this is tcp time 1", String.valueOf(completedIn));

                    time = System.nanoTime();
                    ScanConversion scnConv = ScanConversion.getInstance(message);
                    completedIn = System.nanoTime() - time;
                    //Log.d("this is tcp time 3", String.valueOf(completedIn));

                    time = System.nanoTime();
                    scnConv.setTcpData();
                    completedIn = System.nanoTime() - time;
                    //Log.d("this is tcp time 4", String.valueOf(completedIn));

                    time = System.nanoTime();
                    refreshUI(scnConv);
                    completedIn = System.nanoTime() - time;
                    //Log.d("this is tcp time 5", String.valueOf(completedIn));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] deepInsidePacket(int len, InputStream stream) throws IOException {
        byte[] buffer = new byte[len];

        while (buffer[0] != 1) {
            stream.read(buffer);
        }
        return getDeepInsidePacket(1025, buffer, stream);
    }

    private byte[] getDeepInsidePacket(int len, byte[] buffer, InputStream stream) throws IOException {
        byte[] tmpBuffer = new byte[1025];
        byte[] finalBuffer = new byte[128 * 1024];

        System.arraycopy(buffer, 1, finalBuffer, 0, 1024);
        for (int i = 0; i < 127; i++) {
            stream.read(tmpBuffer);
            System.arraycopy(tmpBuffer, 1, finalBuffer, i * 1024, 1024);
        }
        return finalBuffer;
    }

    private byte[] getReadLineBytes(int len, InputStream stream) throws IOException {
        byte[] buffer = new byte[len];
        stream.read(buffer);
        byte[] filteredByteArray = Arrays.copyOfRange(buffer, 1, buffer.length - 1);
        return filteredByteArray;
    }

    private byte[] readBytes(int len, InputStream stream) throws IOException {
        byte[] buffer = new byte[len];
        int totalLenRead = 0;
        int lenRead = 0;

        while (totalLenRead < len) {
            byte[] tmpBuffer = new byte[len - totalLenRead];
            lenRead = stream.read(tmpBuffer);
            System.arraycopy(tmpBuffer, 0, buffer, totalLenRead, lenRead);
            totalLenRead += lenRead;
        }
        return buffer;
    }
}
