package com.echopen.asso.echopen.model;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class Data
{

    private String title;

    private String description;

    private int image;

    private char[]  envelopeData;

    public Data(InputStreamReader inputStreamReader)
    {
        this.title = title;
        this.description = description;
        envelopeData = new char[0];
        setCharData(inputStreamReader);
    }


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

    public char[] getEnvelopeData() {
        return envelopeData;
    }

    public void setEnvelopeData(char[] envelopeData) {
        this.envelopeData = envelopeData;
    }

    private void setCharData(InputStreamReader inputStreamReader){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(inputStreamReader);
            while ((line = br.readLine()) != null) {
                String[] string_tmp_data = line.split(cvsSplitBy);
                char[] char_tmp_data = new char[string_tmp_data.length];
                for (int index = 0; index < string_tmp_data.length; index++) {
                    char_tmp_data[index] = (char) Integer.parseInt(string_tmp_data[index]);
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

    /* this method can be useful if we have data to be concatenated in horizontal way */

    public static char[] objArrayConcat(char[] o1, char[] o2)
    {
        char[] ret = new char[o1.length + o2.length];

        System.arraycopy(o1, 0, ret, 0, o1.length);
        System.arraycopy(o2, 0, ret, o1.length, o2.length);

        return ret;
    }
}
