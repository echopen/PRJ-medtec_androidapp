package com.echopen.echopen;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton leftBtn = (ImageButton) findViewById(R.id.leftBtn);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent echo = new Intent(getApplicationContext(),echoActivity.class);
                echo.putExtra("orientation","Left");
                startActivity(echo);
            }
        });

        ImageButton rightBtn = (ImageButton) findViewById(R.id.rightBtn);

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent echo = new Intent(getApplicationContext(),echoActivity.class);
                echo.putExtra("orientation","Right");
                startActivity(echo);

            }
        });

    }
}
