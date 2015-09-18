package com.echopen.asso.echopen.preproc;

import java.util.ArrayList;

/**
 * Created by mehdibenchoufi on 16/09/15.
 */
public class ScanConversion {

    private int numPixels;

    private int numWeightPixels;

    private ArrayList<Integer> weight = new ArrayList<Integer>(numWeightPixels);

    private ArrayList<Integer> indexData = new ArrayList<Integer>(numPixels);

    private ArrayList<Integer> indexImg = new ArrayList<Integer>(numPixels);

    public int getNumPixels() {
        return numPixels;
    }

    public void setNumPixels(int numPixels) {
        this.numPixels = numPixels;
    }

    public int getNumWeightPixels() {
        return numWeightPixels;
    }

    public void setNumWeightPixels(int numWeightPixels) {
        this.numWeightPixels = numWeightPixels;
    }

    public ArrayList<Integer> getWeight() {
        return weight;
    }

    public void setWeight(ArrayList<Integer> weight) {
        this.weight = weight;
    }


    public ArrayList<Integer> getIndexImg() {
        return indexImg;
    }

    public void setIndexImg(ArrayList<Integer> indexImg) {
        this.indexImg = indexImg;
    }

    public ArrayList<Integer> getIndexData() {
        return indexData;
    }

    public void setIndexData(ArrayList<Integer> indexData) {
        this.indexData = indexData;
    }

    public ScanConversion(int numPixels) {
        this.numPixels = numPixels;
    }


}
