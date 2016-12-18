package com.echopen.echopen;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.iwf.photopicker.PhotoPicker;

public class GalleryActivity extends AppCompatActivity {


    private static final int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                + "/EchoPen/");
        intent.setDataAndType(uri, "image/png");
        startActivityForResult(Intent.createChooser(intent, "Open folder"), SELECT_PICTURE);
    }
}
