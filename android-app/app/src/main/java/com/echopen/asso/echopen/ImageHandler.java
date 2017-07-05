package com.echopen.asso.echopen;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ImageHandler {

    private File galleryDirectory;

    public ImageHandler( File filesDir){
        this.galleryDirectory = filesDir;
    }

    public boolean saveImage(Bitmap currentBitmap) {

        File galleryDirectory = new File(this.galleryDirectory.toString() + "/");
        if (!galleryDirectory.exists() || !galleryDirectory.isDirectory()) {
            galleryDirectory.mkdir();
        }

        File[] filesInPath = galleryDirectory.listFiles();
        while (this.galleryDirectory.listFiles().length >= 5) {
            String[] filesNames = new String[filesInPath.length];
            for (int i = 0; i < filesInPath.length; i++) {
                filesNames[i] = filesInPath[i].getName();
            }
            String lastFileName = Collections.min(new ArrayList<>(Arrays.asList(filesNames)));
            new File(this.galleryDirectory.toString() + "/" + lastFileName).delete();
        }
        try {
            OutputStream stream = new FileOutputStream(this.galleryDirectory.toString() + "/" + System.currentTimeMillis() + ".png");
            currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("imageSaved","true");
        return true;
    }

}
