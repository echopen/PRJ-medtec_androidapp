package com.echopen.asso.echopen.model.Data;

import android.app.Activity;
import android.util.Log;

import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
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
        int msgLen;
        byte[] msgLenBytes;
        InputStream stream;
        byte[] message0 = new byte[128*1024];

        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 1024; j++) {
                    message0[i*1024 +j] = (byte) 250;
            }
        }
        ScanConversion scnConv0 = ScanConversion.getInstance(message0);
        scnConv0.setTcpData();
        refreshUI(scnConv0);

        try {
            s = new Socket(ip, port);
            stream = s.getInputStream();
            int num_lines = 128;
            int num_data = 2048;
            int check_line =0;
            byte[] message = new byte[num_lines*num_data];
            byte[] tmpBuffer = new byte[num_data];

            while (true) {
                try {
                    //msgLenBytes = readBytes(4, stream);
                    //msgLen = java.nio.ByteBuffer.wrap(msgLenBytes).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
                    //message = readBytes(msgLen, stream);

                    message = deepInsidePacket(2049, stream);
                   /* while (check_line < 128) {
                        tmpBuffer = getReadLineBytes(2049, stream);
                        System.arraycopy(tmpBuffer, 0, message, check_line * num_data, num_data - 1);
                        check_line++;
                    }*/
                    ScanConversion scnConv = ScanConversion.getInstance(message);
                    scnConv.setTcpData();
                    refreshUI(scnConv);
                    message = new byte[num_lines*num_data];
                    //check_line = 0;
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

        while(buffer[0] !=1){
            stream.read(buffer);
        }
        return getDeepInsidePacket(2049, buffer, stream);
    }

    private byte[] getDeepInsidePacket(int len, byte[] buffer, InputStream stream) throws IOException {
        byte[] tmpBuffer = new byte[2049];
        byte[] finalBuffer = new byte[128 * 2048];

        System.arraycopy(buffer, 1, finalBuffer, 0, 2048);
        for(int i = 0;i<127;i++) {
            stream.read(tmpBuffer);
            System.arraycopy(tmpBuffer, 1, finalBuffer, i * 2048, 2048);
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

        while(totalLenRead < len) {
            byte[] tmpBuffer = new byte[len - totalLenRead];
            lenRead = stream.read(tmpBuffer);
            System.arraycopy(tmpBuffer, 0, buffer, totalLenRead, lenRead);
            totalLenRead += lenRead;
        }
        return buffer;
    }
}
