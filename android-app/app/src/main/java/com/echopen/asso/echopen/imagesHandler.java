package com.echopen.asso.echopen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class imagesHandler {
    private File cacheDirectory;
    private File savedDirectory;

    private long recordStart;

    public imagesHandler(File directory) {
        // Creates the cached image directory if not already created
        this.cacheDirectory = new File(directory.toString() + "/cache/");
        if (!this.cacheDirectory.exists() || !this.cacheDirectory.isDirectory()) {
            this.cacheDirectory.mkdir();
        }

        // Creates the saved image directory if not already created
        this.savedDirectory = new File(directory.toString() + "/saved/");
        if (!this.savedDirectory.exists() || !this.savedDirectory.isDirectory()) {
            this.savedDirectory.mkdir();
        }
    }

    public boolean saveCacheImage(Bitmap image) {
        File[] filesInPath;

        // Only keep 100 images at any time
        while (this.cacheDirectory.listFiles().length >= 100) {
            filesInPath = this.cacheDirectory.listFiles();
            String[] filesNames = new String[filesInPath.length];
            for (int i = 0; i < filesInPath.length; i++) {
                filesNames[i] = filesInPath[i].getName();
            }

            String lastFileName = Collections.min(new ArrayList<>(Arrays.asList(filesNames)));
            new File(this.cacheDirectory.toString() + "/" + lastFileName).delete();
        }

        // Save the image
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

    public void saveImage(Bitmap image) {
        try {
            OutputStream stream = new FileOutputStream(this.savedDirectory.toString() + "/" + System.currentTimeMillis() + ".png");
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRecording() {
        this.recordStart = System.currentTimeMillis();
    }

    public void endRecording() {
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

        File clientDirectory = new File(this.savedDirectory.toString() + "/records/" + System.currentTimeMillis() + "/");
        clientDirectory.mkdir();

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

    public Bitmap getLastCachedImage() {
        File[] filesInPath = this.cacheDirectory.listFiles();
        String[] filesNames = new String[filesInPath.length];
        for (int i = 0; i < filesInPath.length; i++) {
            filesNames[i] = filesInPath[i].getName();
        }
        String lastFileName = Collections.max(new ArrayList<>(Arrays.asList(filesNames)));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        File image = new File(this.cacheDirectory.toString() + "/" + lastFileName);

        try {
            return BitmapFactory.decodeStream(new FileInputStream(image), null, options);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public ArrayList<String> listFiles() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Calendar calendar = Calendar.getInstance();
        String[] listRaw = this.savedDirectory.list();
        ArrayList<String> list = new ArrayList<>();
        Arrays.sort(listRaw);

        for (int i = 0; i < listRaw.length; i++) {
            calendar.setTimeInMillis(Long.parseLong(listRaw[i].split("\\.")[0]));
            list.add(formatter.format(calendar.getTime()));
        }
        Collections.reverse(list);
        return list;
    }

    public Bitmap getSavedImage(int position) {
        String[] listRaw = this.savedDirectory.list();
        Arrays.sort(listRaw);

        String fileName = listRaw[position];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        File image = new File(this.savedDirectory.toString() + "/" + fileName);
        try {
            return BitmapFactory.decodeStream(new FileInputStream(image), null, options);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void deleteImage(int position) {
        String[] listRaw = this.savedDirectory.list();
        Arrays.sort(listRaw);

        String fileName = listRaw[position];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        File image = new File(this.savedDirectory.toString() + "/" + fileName);
        image.delete();
    }

    public Bitmap[] getImagesForFreeze() {
        Bitmap[] images = new Bitmap[5];
        Bitmap[] allImages = this.getCachedImages();

        int allImagesLength = allImages.length;

        images[0] = allImages[allImagesLength - 2];
        images[1] = allImages[allImagesLength - 3];
        images[2] = allImages[allImagesLength - 4];
        images[3] = allImages[allImagesLength - 5];
        images[4] = allImages[allImagesLength - 6];

        return images;
    }
}
