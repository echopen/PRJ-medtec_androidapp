package com.echopen.asso.echopen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ChoiceActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        ImageButton enterBt = (ImageButton) findViewById(R.id.archive_bt);
        enterBt.setOnClickListener(this);

        ImageButton signupBt = (ImageButton) findViewById(R.id.echo_bt);
        signupBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.echo_bt :
                btEchoClicked();
                break;
            case R.id.archive_bt :
                btArchiveClicked();
                break;
        }
    }

    private void btEchoClicked(){
        Intent intent = new Intent(this, EchoActivity.class);
        startActivity(intent);
    }

    private void btArchiveClicked(){
        Intent intent = new Intent(this, ArchiveActivity.class);
        startActivity(intent);
    }

}
