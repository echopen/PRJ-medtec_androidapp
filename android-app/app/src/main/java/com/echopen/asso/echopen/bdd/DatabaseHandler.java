package com.echopen.asso.echopen.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Name
    private static final String DB_NAME = "echopenDatabase.db";
    private static final int DB_VERSION = 1;
    public static final String IMG_TABLE_CREATE =
            "CREATE TABLE images (id INTEGER PRIMARY KEY AUTOINCREMENT, imgName TEXT, settings TEXT, note TEXT);";

    public static final String IMG_TABLE_DROP =
            "DROP TABLE IF EXISTS images;";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IMG_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(IMG_TABLE_DROP);
        onCreate(db);
    }
}
