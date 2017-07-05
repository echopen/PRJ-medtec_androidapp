package com.echopen.asso.echopen;

import android.graphics.Bitmap;

import java.io.File;

public class ImageHandler {

public File filesDir;

    public ImageHandler( File filesDir){
        this.filesDir = filesDir;
    }

    public File saveImage(Bitmap currentBitmap) {

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

}
