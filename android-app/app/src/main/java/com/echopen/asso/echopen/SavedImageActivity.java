package com.echopen.asso.echopen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SavedImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_image);
    }
}
