package com.echopen.asso.echopen;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ImageHandler {

    private int clientId;
    private File galleryDirectory;

    public ImageHandler( File filesDir, int clientId){
        this.galleryDirectory = filesDir;
        this.clientId = clientId;
    }

    public boolean saveImage(Bitmap currentBitmap) {

        File clientDirectory = new File(this.galleryDirectory.toString() +"/"+ this.clientId +"/");
        if (!clientDirectory.exists() || !clientDirectory.isDirectory()) {
            clientDirectory.mkdir();
        }

        File[] filesInPath = clientDirectory.listFiles();

        while (clientDirectory.listFiles().length >= 5) {

            String[] filesNames = new String[filesInPath.length];
            for (int i = 0; i < filesInPath.length; i++) {
                filesNames[i] = filesInPath[i].getName();
            }

            String lastFileName = Collections.min(new ArrayList<>(Arrays.asList(filesNames)));
            new File(this.galleryDirectory.toString() + "/" + lastFileName).delete();
        }

        try {
            OutputStream stream = new FileOutputStream(this.galleryDirectory.toString() + "/"+ this.clientId + "/" + System.currentTimeMillis() + ".png");
            currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
