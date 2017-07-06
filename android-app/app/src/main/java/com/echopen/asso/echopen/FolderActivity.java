package com.echopen.asso.echopen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FolderActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        Button saved_image1 = (Button) findViewById(R.id.saved_image1_bt);
        saved_image1.setOnClickListener(this);

        Button saved_image2 = (Button) findViewById(R.id.saved_image2_bt);
        saved_image2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        //I did a switch and called 4 times the same code because i was not sure we would use only one Activity
        //with more time i want to change this "dirty quick code" using fragments

        switch (view.getId()){
            case R.id.saved_image1_bt :
                btSavedImage1Clicked();
                break;
            case R.id.saved_image2_bt :
                btSavedImage2Clicked();
                break;
        }
    }

    private void btSavedImage1Clicked(){
        Intent intent = new Intent(this, SavedImageActivity.class);
        startActivity(intent);
    }

    private void btSavedImage2Clicked(){
        Intent intent = new Intent(this, SavedImageActivity.class);
        startActivity(intent);
    }
}
