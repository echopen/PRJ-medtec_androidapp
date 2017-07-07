package com.echopen.asso.echopen.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ImageDAO extends DAOBase {
    public static final String TABLE_NAME = "images";
    public static final String KEY = "id";
    public static final String IMG_NAME = "imgName";
    public static final String SETTINGS = "settings";
    public static final String NOTE = "note";

    public static final String IMG_TABLE_CREATE =
            "CREATE TABLE images (id INTEGER PRIMARY KEY AUTOINCREMENT, imgName TEXT, settings TEXT, note TEXT);";

    public static final String IMG_TABLE_DROP =
            "DROP TABLE IF EXISTS images;";

    public ImageDAO(Context pContext) {
        super(pContext);
    }

    public void add(Image i) {
        ContentValues values = new ContentValues();
        values.put(ImageDAO.IMG_NAME, i.getImgName());
        values.put(ImageDAO.SETTINGS, i.getSettings());
        values.put(ImageDAO.NOTE, i.getNote());
        mDb.insert(ImageDAO.TABLE_NAME, null, values);
    }

    public void delete(Image i) {
        mDb.delete(TABLE_NAME, KEY + "= ?", new String[]{String.valueOf(i.getId())});
    }

    public void update(Image i) {
        //
    }

    public void get(Image i) {
        Cursor c = mDb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY + " = ?",
                new String[]{String.valueOf(i.getId())});
    }

    public Image getLastImg() {
        Cursor c = mDb.rawQuery("SELECT * FROM " + TABLE_NAME + " LIMIT 1", null);
        Image image = new Image();
        if (c.moveToFirst()) {
            image.setId(c.getLong(0));
            image.setImgName(c.getString(1));
            image.setSettings(c.getString(2));
            image.setNote(c.getString(3));
        }
        return image;
    }

    public List<Image> getAll() {
        List<Image> imageList = new ArrayList<Image>();
        Cursor c = mDb.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Image image = new Image();
                image.setId(c.getLong(0));
                image.setImgName(c.getString(1));
                image.setSettings(c.getString(2));
                image.setNote(c.getString(3));
                imageList.add(image);
            } while (c.moveToNext());
        }

        return imageList;
    }
}
