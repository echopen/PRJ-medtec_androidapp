package com.echopen.asso.echopen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class imagesHandler {
    private File cacheDirectory;
    private File clientsDirectory;

    private long recordStart;

    public imagesHandler(File directory) {
        this.cacheDirectory = new File(directory.toString() + "/cache/");
        if (!this.cacheDirectory.exists() || !this.cacheDirectory.isDirectory()) {
            this.cacheDirectory.mkdir();
        }

        this.clientsDirectory = new File(directory.toString() + "/clients/");
        if (!this.clientsDirectory.exists() || !this.clientsDirectory.isDirectory()) {
            this.clientsDirectory.mkdir();
        }
    }

    public boolean saveCacheImage(Bitmap image) {
        File[] filesInPath;

        while (this.cacheDirectory.listFiles().length >= 100) {
            filesInPath = this.cacheDirectory.listFiles();
            String[] filesNames = new String[filesInPath.length];
            for (int i = 0; i < filesInPath.length; i++) {
                filesNames[i] = filesInPath[i].getName();
            }

            String lastFileName = Collections.min(new ArrayList<>(Arrays.asList(filesNames)));
            new File(this.cacheDirectory.toString() + "/" + lastFileName).delete();
        }
        try {
            OutputStream stream = new FileOutputStream(this.cacheDirectory.toString() + "/" + System.currentTimeMillis() + ".png");
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Bitmap[] getCachedImages() {
        File[] files = this.cacheDirectory.listFiles();
        Bitmap[] images = new Bitmap[files.length];
        Arrays.sort(files);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        for (int i = 0; i < images.length; i++) {
            try {
                images[i] = BitmapFactory.decodeStream(new FileInputStream(files[i]), null, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    public File saveImage(int clientId) {
        File clientDirectory = new File(this.clientsDirectory.toString() + "/" + clientId + "/");
        if (!clientDirectory.exists() || !clientDirectory.isDirectory()) {
            clientDirectory.mkdir();
        }

        File[] filesInPath = clientDirectory.listFiles();
        String[] cachedFilesNames = new String[filesInPath.length];
        for (int i = 0; i < filesInPath.length; i++) {
            cachedFilesNames[i] = filesInPath[i].getName();
        }

        File lastCachedFile = new File(clientDirectory + Collections.max(new ArrayList<>(Arrays.asList(cachedFilesNames))));
        File savedFile = new File(clientDirectory.toString() + System.currentTimeMillis() + ".png");

        try {
            FileUtils.copyFile(lastCachedFile, savedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return savedFile;
    }

    public void startRecording() {
        this.recordStart = System.currentTimeMillis();
    }

    public void endRecording(int clientId) {
        long recordEnd = System.currentTimeMillis();
        File[] allFiles = this.cacheDirectory.listFiles();
        ArrayList<Bitmap> images = new ArrayList<>();

        String fileName;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        for (int i = 0; i < allFiles.length; i++) {
            fileName = allFiles[i].getName().split("\\.")[0];
            if (Long.parseLong(fileName) >= this.recordStart && Long.parseLong(fileName) <= recordEnd) {
                try {
                    images.add(BitmapFactory.decodeStream(new FileInputStream(allFiles[i]), null, options));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        File clientDirectory = new File(this.clientsDirectory.toString() + "/" + clientId + "/records/" + System.currentTimeMillis() + "/");
        if (!clientDirectory.exists() || !clientDirectory.isDirectory()) {
            clientDirectory.mkdir();
        }

        OutputStream stream;

        for (int i = 0; i < images.size(); i++) {
            try {
                stream = new FileOutputStream(clientDirectory.toString() + "/" + i);
                images.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
