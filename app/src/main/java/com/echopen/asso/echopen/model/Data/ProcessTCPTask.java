package com.echopen.asso.echopen.model.Data;

/**
 * This class takes as formal input the pixels data coming from hardware, and then, after processing it, refreshes the UI.
 * More precisely, it gets the data from TCP protocol and send it to ScanConversion algorithm class.
 * After the data is processed, it gets it back and displays the final image through the main UI thread.
 */

import android.app.Activity;

import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.utils.Constants;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ProcessTCPTask extends AbstractDataTask {
    private Socket s;
    private String ip;
    private int port;

    private DataInputStream dataInputStream;

    public ProcessTCPTask(Activity activity, MainActionController mainActionController, ScanConversion scanConversion, String ip, int port) throws IOException {
        super(activity, mainActionController, scanConversion);
        this.ip = ip;
        this.port = port;
    }

    protected Void doInBackground(Void... Voids) {
        InputStream stream;
        int rows = Constants.PreProcParam.NUM_SAMPLES;
        //int cols = Constants.PreProcParam.NUM_IMG_DATA;

        try {
            s = new Socket(ip, port);
            stream = s.getInputStream();
            byte[] message;

            while (true) {
                try {
                    message = deepInsidePacket(rows +1, stream);
                    ScanConversion scanConversion = ScanConversion.getInstance(message);
                    scanConversion.setTcpData();
                    refreshUI(scanConversion);
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
        //int rows = Constants.PreProcParam.NUM_SAMPLES;
        int start_line = Constants.PreProcParam.NUM_LINES;

        DataInputStream dataInputStream = new DataInputStream(stream);

        while(buffer[0] != start_line/2 + 1){
            dataInputStream.readFully(buffer);
        }
        return getDeepInsidePacket(buffer, stream);
    }

    private byte[] getDeepInsidePacket(byte[] buffer, InputStream stream) throws IOException {
        int rows = Constants.PreProcParam.NUM_SAMPLES;
        int cols = Constants.PreProcParam.NUM_LINES;
        byte[] tmpBuffer = new byte[rows+1];
        byte[] finalBuffer = new byte[cols * rows];
        int count_lines = 0;
        int half_count_lines = 0;

        System.arraycopy(buffer, 1, finalBuffer, cols/2 * rows, rows);
        dataInputStream = new DataInputStream(stream);

        while(true) {
            dataInputStream.readFully(tmpBuffer);
            System.arraycopy(tmpBuffer, 1, finalBuffer, (cols / 2 + 1 + count_lines) * rows, rows);
            count_lines++;
            if (count_lines == 31) {
                while (true) {
                    dataInputStream.readFully(tmpBuffer);
                    System.arraycopy(tmpBuffer, 1, finalBuffer, (31 - half_count_lines) * rows, rows);
                    half_count_lines++;
                    if (half_count_lines == 32) {
                        break;
                    }
                }
                break;
            }
        }
        return finalBuffer;
    }
}
