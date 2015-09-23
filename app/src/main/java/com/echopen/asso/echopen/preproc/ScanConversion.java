package com.echopen.asso.echopen.preproc;

import com.echopen.asso.echopen.utils.Constants;

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

    public void make_tables(){
        byte[] data = new byte[1];
        int[] int_constants = Constants.PreProcParam.getLoadIntegerConstants();
        float[] float_constants = Constants.PreProcParam.getLoadFloatConstants();
        frameFromJNI(data, int_constants, float_constants);
    }

    public native void frameFromJNI(byte[] data, int[] int_constants, float[] float_constants );
}
