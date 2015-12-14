package com.echopen.asso.echopen.model.Data;

import java.io.*;

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
     * Constructor whcich takes as argument the incoming simulated data. Data are gathered from
     * a csv file stored in the assets folder
     * @param inputStreamReader
     */
    public Data(InputStreamReader inputStreamReader) {
    }
}
