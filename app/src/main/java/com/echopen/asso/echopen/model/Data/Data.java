package com.echopen.asso.echopen.model.Data;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class Data
{

    protected String title;

    protected String description;

    protected int image;

    protected int[]  envelopeData;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDesc()
    {
        return description;
    }


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

    public int[] getEnvelopeData() {
        return envelopeData;
    }

    public void setEnvelopeData(int[] envelopeData) {
        this.envelopeData = envelopeData;
    }

    public Data(int[][] udpDataArray) {
    }

    public Data(InputStreamReader inputStreamReader) {
    }
}
