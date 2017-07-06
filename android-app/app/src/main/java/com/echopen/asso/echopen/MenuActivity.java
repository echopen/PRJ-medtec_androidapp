package com.echopen.asso.echopen;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.echopen.asso.echopen.menu.LanguesActivity;

public class MenuActivity extends Activity implements View.OnClickListener {

    private Button btn_langues = null;
    private Button btn_close = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn_langues = (Button) findViewById(R.id.btn_langues);
        btn_close = (Button) findViewById(R.id.btn_close_menu);

        btn_close.setOnClickListener(MenuActivity.this);
        btn_langues.setOnClickListener(MenuActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.btn_langues):
                Intent intent = new Intent(MenuActivity.this, LanguesActivity.class);
                startActivity(intent);
            case(R.id.btn_close_menu):
                    finish();
                break;
        }

    }

}
