package com.echopen.asso.echopen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ArchiveActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        Button archivebutton1 = (Button) findViewById(R.id.archive_folder_1);
        archivebutton1.setOnClickListener(this);

        Button archivebutton2 = (Button) findViewById(R.id.archive_folder_2);
        archivebutton2.setOnClickListener(this);

        Button archivebutton3 = (Button) findViewById(R.id.archive_folder_3);
        archivebutton3.setOnClickListener(this);

        Button archivebutton4 = (Button) findViewById(R.id.archive_folder_4);
        archivebutton4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        //I did a switch and called 4 times the same code because i was not sure we would use only one Activity
        //with more time i want to change this "dirty quick code" using fragments

        switch (view.getId()){
            case R.id.archive_folder_1 :
                btArchiveFolder1Clicked();
                break;
            case R.id.archive_folder_2 :
                btArchiveFolder2Clicked();
                break;
            case R.id.archive_folder_3 :
                btArchiveFolder3Clicked();
                break;
            case R.id.archive_folder_4 :
                btArchiveFolder4Clicked();
                break;
        }
    }

    private void btArchiveFolder1Clicked(){
        Intent intent = new Intent(this, FolderActivity.class);
        startActivity(intent);
    }

    private void btArchiveFolder2Clicked(){
        Intent intent = new Intent(this, FolderActivity.class);
        startActivity(intent);
    }

    private void btArchiveFolder3Clicked(){
        Intent intent = new Intent(this, FolderActivity.class);
        startActivity(intent);
    }

    private void btArchiveFolder4Clicked(){
        Intent intent = new Intent(this, FolderActivity.class);
        startActivity(intent);
    }
}