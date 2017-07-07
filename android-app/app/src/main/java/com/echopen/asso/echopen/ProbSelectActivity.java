package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import static com.echopen.asso.echopen.R.id.newdevice_bt;

public class ProbSelectActivity extends AppCompatActivity implements View.OnClickListener {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_prob_select);

        ImageButton backBt = (ImageButton) findViewById(R.id.back_btn);
        backBt.setOnClickListener(this);

        Button nextBt = (Button) findViewById(R.id.next_bt);
        nextBt.setOnClickListener(this);

        Button newdeviceBt = (Button) findViewById(newdevice_bt);
        newdeviceBt.setPaintFlags(newdeviceBt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_btn:
                btBackClicked();
                break;
            case R.id.next_bt:
                btNextClicked();
                break;
        }
    }

    private void btBackClicked(){
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
    }

    private void btNextClicked(){
        Intent intent = new Intent(this, EchoActivity.class);
        startActivity(intent);
    }


}