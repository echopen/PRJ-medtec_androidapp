package com.echopen.asso.echopen.model.Data;

/**
 * This class takes as formal input the pixels data coming from hardware, and then, after processing it, refreshes the UI.
 * More precisely, it gets the data from TCP protocol and send it to ScanConversion algorithm class.
 * After the data is processed, it gets it back and displays the final image through the main UI thread.
 */

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationWifiNotification;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationWifiNotificationState;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.utils.Constants;
import com.thanosfisherman.wifiutils.WifiUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.net.Socket;

import java.util.Arrays;

public class ProcessTCPTask extends AbstractDataTask {

    private final String TAG = this.getClass().getSimpleName();
    private Socket s;

    private final String ip;

    private final int port;

    private DataInputStream dataInputStream;

    public ProcessTCPTask( RenderingContextController iRenderingContextController,ProbeCinematicProvider iProbeCinematicProvider, EchographyImageStreamingService iEchographyImageStreamingService, String ip, int port) {
        super(iRenderingContextController, iProbeCinematicProvider, iEchographyImageStreamingService);
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
            Integer[] lRawImageData;

            DeviceConfiguration lDeviceConfiguration = getDeviceConfiguration(stream);
            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(ProbeCommunicationWifiNotificationState.WIFI_START_SCANNING));
            while (true) {
                try {
                    RenderingContext lCurrentRenderingContext = mRenderingContextController.getCurrentRenderingContext();

                    alignDataStream(stream, lDeviceConfiguration);
                    lRawImageData = getRawImageData(stream, lDeviceConfiguration);

                    ProbeCinematicConfiguration lProbeCinematic = mProbeCinematicProvider.getProbeCinematic(lDeviceConfiguration.getProbeCinematicName());
                    rawDataPipeline(lCurrentRenderingContext, lDeviceConfiguration, lProbeCinematic, lRawImageData);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void alignDataStream(InputStream iStream, DeviceConfiguration iDeviceConfiguration) throws IOException {
        Integer lNbSamplesPerLine = iDeviceConfiguration.getNbSamplesPerLine();
        Integer lSampleSize = Constants.PreProcParam.NUM_BYTES_PER_SAMPLE;

        // 2 bytes for line index + 2 bytes * nb of samples per line
        byte[] lLineData = new byte[lNbSamplesPerLine * lSampleSize + lSampleSize];

        dataInputStream = new DataInputStream(iStream);

        Integer lLineNumber = 0;
        Integer lPreviousLineNumber = 0;

        // wait for data stream to be aligned on an image start (first line in image)
        while(!(lLineNumber ==  1 && lPreviousLineNumber == 2)) {
            dataInputStream.readFully(lLineData);

            lPreviousLineNumber = lLineNumber;
            lLineNumber = (lLineData[1] << 8) | (lLineData[0] & 0x00ff);
        }
    }

    private Integer[] getRawImageData(InputStream iStream, DeviceConfiguration iDeviceConfiguration) throws IOException{
        Integer lNbSamplesPerLine = iDeviceConfiguration.getNbSamplesPerLine();
        Integer lNbLinesPerImage = iDeviceConfiguration.getNbLinesPerImage();
        Integer lSampleSize = Constants.PreProcParam.NUM_BYTES_PER_SAMPLE;

        Integer[] lRawImageData = new Integer[lNbLinesPerImage * lNbSamplesPerLine];
        Arrays.fill(lRawImageData, 0, lNbLinesPerImage * lNbSamplesPerLine, 0);
        // 2 bytes for line index + 2 bytes * nb of samples per line
        byte[] lLineData = new byte[lNbSamplesPerLine * lSampleSize + lSampleSize];
        Integer[] lAliasedLineData;
        Integer lLineOffset;

        dataInputStream = new DataInputStream(iStream);

        Integer lLineNumber = 0;

        for(Integer i = 0; i < lNbLinesPerImage; i++){
            dataInputStream.readFully(lLineData);
            lLineNumber = (lLineData[1] << 8) | (lLineData[0] & 0x00ff);
            lAliasedLineData = new Integer[lNbSamplesPerLine];
            for(Integer j = 1; j <= lNbSamplesPerLine; j++)
            {
                lAliasedLineData[(j- 1)] = (lLineData[2*j + 1] << 8) | (lLineData[2*j] & 0x00ff);
            }
            lLineOffset = (lLineNumber - 1) * lNbSamplesPerLine;

            System.arraycopy(lAliasedLineData, 0, lRawImageData, lLineOffset , lAliasedLineData.length);
        }

        return lRawImageData;
    }

    private DeviceConfiguration getDeviceConfiguration(InputStream iStream) throws IOException{
        dataInputStream = new DataInputStream(iStream);

        float lR0 = dataInputStream.readFloat() * 0.001f; // in mm
        float lRf = dataInputStream.readFloat() * 0.001f; // in mm
        float lDecimation = dataInputStream.readFloat(); //

        float lSamplingFrequency = (float) (Constants.PreProcParam.ADC_FREQUENCY_CLOCK / lDecimation); // in Hz
        short lNbLinePerImage = dataInputStream.readShort();
        float lProbeSectorAngle = dataInputStream.readFloat();// in degree
        byte lMode =  dataInputStream.readByte(); // data format - 0- raw data value stored on 2 bytes
                                 //               1- envelop data value stored on 1 byte

        int lProbeNameLength = dataInputStream.readInt();
        byte[] lProbeCinematicNameBytes = new byte[lProbeNameLength];
        dataInputStream.read(lProbeCinematicNameBytes, 0, lProbeNameLength);
        float lEchoDelay = dataInputStream.readFloat();
        String lProbeCinematicName =  new String(lProbeCinematicNameBytes);
        int lNbSamplesPerLine = (int) (2.0 * (lRf - lR0) * Constants.PreProcParam.ADC_FREQUENCY_CLOCK / (Constants.PreProcParam.SPEED_OF_ACOUSTIC_WAVE * lDecimation));
;
        Log.d(TAG, "R0 " + lR0 + "Rf " + lRf + "Decimation " + lDecimation + "SamplingFrequency " + lSamplingFrequency + "NbLinePerImage "+ lNbLinePerImage + "Probe Sector Angle " + lProbeSectorAngle + "Mode " + lMode + "NbSamplePerLine " + lNbSamplesPerLine + "Probe Cinematic " + lProbeCinematicName + "Echo Delay " + lEchoDelay);
        return new DeviceConfiguration(lR0, lRf, lProbeSectorAngle, lSamplingFrequency, lNbLinePerImage, lNbSamplesPerLine, lProbeCinematicName, lEchoDelay, lDecimation);
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
