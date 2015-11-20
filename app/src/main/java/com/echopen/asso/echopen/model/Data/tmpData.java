package com.echopen.asso.echopen.model.Data;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class tmpData {
    private String title;
    private String description;
    private char[] envelopeData;
    private Activity activity;
    private InputStreamReader inputStreamReader;


    public tmpData(String title, String description, String file, Activity activity) {
        this.title = title;
        this.description = description;
        this.envelopeData = new char[0];
        this.activity = activity;

        AssetManager assetManager = activity.getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open("data/raw_data/data_kydney.csv");
            inputStreamReader = new InputStreamReader(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setCharData();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return this.description;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public char[] getEnvelopeData() {
        return this.envelopeData;
    }

    public void setImage(char[] envelopeData) {
        this.envelopeData = envelopeData;
    }

    private void setCharData() {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String dir = (new File(".")).getAbsolutePath();

        try {
            char[] char_tmp_data;
            try {
                for(br = new BufferedReader(inputStreamReader);
                    (line = br.readLine()) != null;
                    this.envelopeData = objArrayConcat(this.envelopeData, char_tmp_data
                    )) {
                    String[] e = line.split(cvsSplitBy);
                    char_tmp_data = new char[e.length];

                    for(int index = 0; index < e.length; ++index) {
                        char_tmp_data[index] = (char)Integer.parseInt(e[index]);
                    }
                }
            } catch (FileNotFoundException var20) {
                var20.printStackTrace();
            } catch (IOException var21) {
                var21.printStackTrace();
            }
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException var19) {
                    var19.printStackTrace();
                }
            }

        }

    }

    public static char[] objArrayConcat(char[] o1, char[] o2) {
        char[] ret = new char[o1.length + o2.length];
        System.arraycopy(o1, 0, ret, 0, o1.length);
        System.arraycopy(o2, 0, ret, o1.length, o2.length);
        return ret;
    }
}
