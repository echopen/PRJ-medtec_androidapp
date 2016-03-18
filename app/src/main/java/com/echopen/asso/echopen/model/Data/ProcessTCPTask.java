package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.util.Log;

import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.utils.Constants;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    protected Void doInBackground(Void... Voids) {
        InputStream stream;
        int rows = Constants.PreProcParam.NUM_SAMPLES;
        int cols = Constants.PreProcParam.NUM_IMG_DATA;
        byte[] message0 = new byte[rows*cols];

        /*while(true){
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    if (j < 512)
                        message0[i*rows +j] = (byte) 255;
                    else if (j>= 512)
                        message0[i*rows +j] = (byte) 140;
                }
            }
            ScanConversion scnConv0 = ScanConversion.getInstance(message0);
            scnConv0.setTcpData();
            refreshUI(scnConv0);
        }*/
        try {
            s = new Socket(ip, port);
            stream = s.getInputStream();
            int num_lines = 128;
            int num_data = rows;
            byte[] message = new byte[num_lines*num_data];

            while (true) {
                try {
                    message = deepInsidePacket(rows +1, stream);
                    ScanConversion scnConv = ScanConversion.getInstance(message);
                    scnConv.setTcpData();
                    refreshUI(scnConv);
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
        int rows = Constants.PreProcParam.NUM_SAMPLES;

        while(buffer[0] !=1){
            stream.read(buffer);
        }
        return getDeepInsidePacket(rows+1, buffer, stream);
    }

    private byte[] getDeepInsidePacket(int len, byte[] buffer, InputStream stream) throws IOException {
        int rows = Constants.PreProcParam.NUM_SAMPLES;
        byte[] tmpBuffer = new byte[rows+1];
        byte[] finalBuffer = new byte[128 * rows];

        System.arraycopy(buffer, 1, finalBuffer, 0, rows);
        for(int i = 0;i<127;i++) {
            stream.read(tmpBuffer);
            System.arraycopy(tmpBuffer, 1, finalBuffer, i * rows, rows);
        }
        //FileOutputStream out = new FileOutputStream("e/Users/mehdibenchoufi/echopen-file");
        //out.write(finalBuffer);
        //out.close();
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

        while(totalLenRead < len) {
            byte[] tmpBuffer = new byte[len - totalLenRead];
            lenRead = stream.read(tmpBuffer);
            System.arraycopy(tmpBuffer, 0, buffer, totalLenRead, lenRead);
            totalLenRead += lenRead;
        }
        return buffer;
    }
}
