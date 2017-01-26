package com.echopen.asso.echopen.model.Data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class holds the metadata of the images and the pixel int array data
 * The metadata is being designed by the team, so we put here some fake categories, as title, description...
 */
public class Data
{

    /* fake metadata for image, title */
    protected String title;

    /* fake metadata for image, description */
    protected String description;

    protected int image;

    /* int array holding the pixel array data */
    protected int[]  envelopeData;

    /**
     *
     * @return title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return description
     */
    public String getDesc()
    {
        return description;
    }


    /**
     * @param description
     */
    public void setDesc(String description)
    {
        this.description = description;
    }


    public int getImage()
    {
        return image;
    }


    public void setImage(int image)
    {
        this.image = image;
    }

    /**
     * @return envelopeData, pixel array data
     */
    public int[] getEnvelopeData() {
        return envelopeData;
    }

    /**
     * @param envelopeData
     */
    public void setEnvelopeData(int[] envelopeData) {
        this.envelopeData = envelopeData;
    }

    /**
     * Constructor with the incoming UDP data as an argument
     * @param udpDataArray
     */
    public Data(int[][] udpDataArray) {
    }

    /**
     * Constructor which takes as argument the incoming simulated data. Data are gathered from
     * a csv file stored in the assets folder
     * @param inputStreamReader
     */
    public Data(InputStreamReader inputStreamReader) {
        envelopeData = new int[0];
        setCharData(inputStreamReader);
    }

    private void setCharData(InputStreamReader inputStreamReader){
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(inputStreamReader);
            while ((line = br.readLine()) != null) {
                String[] string_tmp_data = line.split(cvsSplitBy);
                int[] char_tmp_data = new int[string_tmp_data.length];
                for (int index = 0; index < string_tmp_data.length; index++) {
                    char_tmp_data[index] = Integer.parseInt(string_tmp_data[index]);
                }
                envelopeData = Data.objArrayConcat(envelopeData, char_tmp_data);
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

    /**
     *  this method can be useful if we have data to be concatenated in horizontal way
     */
    public static int[] objArrayConcat(int[] o1, int[] o2)
    {
        int[] ret = new int[o1.length + o2.length];

        System.arraycopy(o1, 0, ret, 0, o1.length);
        System.arraycopy(o2, 0, ret, o1.length, o2.length);

        return ret;
    }
}

