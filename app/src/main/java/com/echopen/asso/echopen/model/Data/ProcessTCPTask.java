package com.echopen.asso.echopen.model.Data;

/**
 * This class takes as formal input the pixels data coming from hardware, and then, after processing it, refreshes the UI.
 * More precisely, it gets the data from TCP protocol and send it to ScanConversion algorithm class.
 * After the data is processed, it gets it back and displays the final image through the main UI thread.
 */

import android.app.Activity;

import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.utils.Constants;
import com.parse.gdata.Preconditions;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.Socket;

public class ProcessTCPTask extends AbstractDataTask {

    private Socket s;

    private final String ip;

    private final int port;

    private DataInputStream dataInputStream;

    public ProcessTCPTask(Activity activity, MainActionController mainActionController, RenderingContextController iRenderingContextController, ScanConversion scanConversion, String ip, int port) {
        super(activity, mainActionController, scanConversion, iRenderingContextController);
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
            //checkStreamIsNotEmpty(stream);

            byte[] message;

            while (true) {
                try {
                    RenderingContext lCurrentRenderingContext = mRenderingContextController.getCurrentRenderingContext();
                    message = deepInsidePacket(rows +1, stream);
                    ScanConversion scanConversion = ScanConversion.getInstance(message);
                    scanConversion.setTcpData();

                    refreshUI(scanConversion, lCurrentRenderingContext);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
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
        int start_line = Constants.PreProcParam.NUM_IMG_DATA;

        dataInputStream = new DataInputStream(stream);

        while(buffer[0] != start_line/2 + 1){
            dataInputStream.readFully(buffer);
        }
        return getDeepInsidePacket(buffer, stream);
    }

    private byte[] getDeepInsidePacket(byte[] buffer, InputStream stream) throws IOException {
        int rows = Constants.PreProcParam.NUM_SAMPLES;
        int cols = Constants.PreProcParam.NUM_IMG_DATA;
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

    /**
     * Permits to check the InputStream is empty or not
     * Please note that only the returned InputStream must be consummed.
     *
     * see:
     * http://stackoverflow.com/questions/1524299/how-can-i-check-if-an-inputstream-is-empty-without-reading-from-it
     *
     * @param inputStream
     * @return
     */
    private static InputStream checkStreamIsNotEmpty(InputStream inputStream) throws IOException {
        Preconditions.checkArgument(inputStream != null, "The InputStream is mandatory");
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
        int b;
        b = pushbackInputStream.read();
        if ( b == -1 ) {
            pushbackInputStream = null;
            checkStreamIsNotEmpty(inputStream);
        }
        pushbackInputStream.unread(b);
        return pushbackInputStream;
    }
}
