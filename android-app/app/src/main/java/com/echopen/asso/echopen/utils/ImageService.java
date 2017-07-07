package com.echopen.asso.echopen.utils;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.bdd.Image;
import com.echopen.asso.echopen.bdd.ImageDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageService {

    public static String saveToInternalStorage(Context c, Activity a, Bitmap iBitmap) {

        //Open Database and Load ImageDAO
        ImageDAO imageDAO = new ImageDAO(c);
        imageDAO.open();

        ContextWrapper cw = new ContextWrapper(a.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        Long tsLong = System.currentTimeMillis() / 1000;
        String imgName = tsLong.toString() + ".jpg";
        // Create imageDir
        File mypath = new File(directory, imgName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            iBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // Save the directory path with name Image
            Image img = new Image();
            img.setImgName(directory.getAbsolutePath() + "/" + imgName);
            imageDAO.add(img);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" + imgName;
    }

    public static Bitmap loadImageFromStorage(String path) {
        try {
            File f = new File(path);
            if (f.exists()) {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                return b;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
