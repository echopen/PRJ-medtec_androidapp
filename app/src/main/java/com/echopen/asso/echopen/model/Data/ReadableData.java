package com.echopen.asso.echopen.model.Data;

import com.echopen.asso.echopen.utils.ImageHelper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 */
public class ReadableData extends Data {

    private int[][] udpDataArray;

    private InputStreamReader inputStreamReader;

    public ReadableData(int[][] udpDataArray, Class genClass) {
        super(udpDataArray);
        envelopeData = new int[0];
        this.udpDataArray = udpDataArray;
        setReadableData(genClass);
    }

    public ReadableData(InputStreamReader inputStreamReader, Class genClass) {
        super(inputStreamReader);
        this.inputStreamReader = inputStreamReader;
    }

    private void setReadableData(Class genClass){
        if(int.class.isAssignableFrom(genClass)) {
            setIntReadableData();
        }
        else if(char.class.isAssignableFrom(genClass)) {
            setInputStreamReadable();
        }
        else {
            throw new IllegalArgumentException("This type is not allowed");
        }
    }

    private void setIntReadableData() {
        int row_length = udpDataArray.length;
        int column_length = udpDataArray[0].length;

        for (int i = 0; i < row_length; i++) {
            int[] tmp_data = new int[column_length];
            for (int j = 0; j < column_length; j++) {
                tmp_data[j] = Integer.parseInt(String.valueOf(udpDataArray[i][j]));
            }
            envelopeData = ImageHelper.intArrayConcat(envelopeData, tmp_data);
        }
    }

    private void setInputStreamReadable() {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(inputStreamReader);
            while ((line = br.readLine()) != null) {
                String[] string_tmp_data = line.split(cvsSplitBy);
                int[] char_tmp_data = new int[string_tmp_data.length];
                for (int index = 0; index < string_tmp_data.length; index++) {
                    char_tmp_data[index] = Integer.parseInt(string_tmp_data[index]);
                }
                envelopeData = ImageHelper.intArrayConcat(envelopeData, char_tmp_data);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

