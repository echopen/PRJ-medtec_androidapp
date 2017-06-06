package com.echopen.asso.echopen.model.Data;

/**
 * This class takes as formal input the pixels data coming from hardware, and then, after processing it, refreshes the UI.
 * More precisely, it gets the data from TCP protocol and send it to ScanConversion algorithm class.
 * After the data is processed, it gets it back and displays the final image through the main UI thread.
 */

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Debug;
import android.util.Log;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.filters.EnvelopDetectionFilter;
import com.echopen.asso.echopen.filters.RenderingContext;
import com.echopen.asso.echopen.preproc.ScanConversion;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.utils.Constants;
import com.parse.gdata.Preconditions;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.net.Socket;

import java.util.Arrays;
import java.util.Observable;

import static com.echopen.asso.echopen.utils.Constants.PreProcParam.TCP_IMG_DATA;
import static com.echopen.asso.echopen.utils.Constants.PreProcParam.TCP_NUM_SAMPLES;

public class ProcessTCPTask extends AbstractDataTask {

    private final String TAG = this.getClass().getSimpleName();
    private Socket s;

    private final String ip;

    private final int port;

    private DataInputStream dataInputStream;

    public ProcessTCPTask(Activity activity, RenderingContextController iRenderingContextController, EchographyImageStreamingService iEchographyImageStreamingService, String ip, int port) {
        super(activity, iRenderingContextController, iEchographyImageStreamingService);
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

            while (true) {
                try {
                    RenderingContext lCurrentRenderingContext = mRenderingContextController.getCurrentRenderingContext();

                    alignDataStream(stream, lDeviceConfiguration);
                    lRawImageData = getRawImageData(stream, lDeviceConfiguration);

                    rawDataPipeline(lCurrentRenderingContext, lDeviceConfiguration, lRawImageData);
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

    private Integer[] getRawImageDataFromLocal() {
        AssetManager assetManager = activity.getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("data/raw_data/data_simu.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader isReader = new InputStreamReader(inputStream);
        Data data = new Data(isReader);
        return data.getEnvelopeIntegerData();
    }

    private Integer[] getRawImageData(InputStream iStream, DeviceConfiguration iDeviceConfiguration) throws IOException{
        // device config is temporary stored in the app
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
        byte[] lConfig = new byte[6];

        dataInputStream = new DataInputStream(iStream);
        dataInputStream.readFully(lConfig);

        double lR0 = ((lConfig[0] + 256) % 256) * 0.001; // in mm
        double lRf = ((lConfig[1] + 256) % 256) * 0.001; // in mm
        int lDecimation = (lConfig[2] + 256) % 256; //

        double lSamplingFrequency = Constants.PreProcParam.ADC_FREQUENCY_CLOCK / lDecimation; // in Hz
        int lNbLinePerImage = (lConfig[3] + 256) % 256;
        float lProbeSectorAngle = (lConfig[4] + 256) % 256; // in degree
        byte lMode = lConfig[5]; // data format - 0- raw data value stored on 2 bytes
                                 //               1- envelop data value stored on 1 byte

        int lNbSamplesPerLine = (int) (2 * (lRf - lR0) * Constants.PreProcParam.ADC_FREQUENCY_CLOCK / (Constants.PreProcParam.SPEED_OF_ACOUSTIC_WAVE * lDecimation));

        Log.d(TAG, "R0 " + lR0 + "Rf " + lRf + "Decimation " + lDecimation + "SamplingFrequency " + lSamplingFrequency + "NbLinePerImage "+ lNbLinePerImage + "Probe Sector Angle " + lProbeSectorAngle + "Mode " + lMode + "NbSamplePerLine " + lNbSamplesPerLine);
        return new DeviceConfiguration(lR0, lRf, lProbeSectorAngle, lSamplingFrequency, lNbLinePerImage, lNbSamplesPerLine);
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
