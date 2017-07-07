package com.echopen.asso.echopen;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.echopen.asso.echopen.menu.FAQActivity;
import com.echopen.asso.echopen.menu.LanguesActivity;
import com.echopen.asso.echopen.menu.SignalProblemActivity;

public class MenuActivity extends Activity implements View.OnClickListener {

    private Button btn_langues = null;
    private Button btn_signal = null;
    private Button btn_faq = null;
    private Button btn_close = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);

        btn_langues = (Button) findViewById(R.id.btn_langues);
        btn_signal = (Button) findViewById(R.id.btn_signal);
        btn_faq = (Button) findViewById(R.id.btn_faq);
        btn_close = (Button) findViewById(R.id.btn_close_menu);

        btn_langues.setOnClickListener(MenuActivity.this);
        btn_signal.setOnClickListener(MenuActivity.this);
        btn_faq.setOnClickListener(MenuActivity.this);
        btn_close.setOnClickListener(MenuActivity.this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case(R.id.btn_langues):
                intent = new Intent(MenuActivity.this, LanguesActivity.class);
                startActivity(intent);
                break;
            case(R.id.btn_signal):
                intent = new Intent(MenuActivity.this, SignalProblemActivity.class);
                startActivity(intent);
                break;
            case(R.id.btn_faq):
                intent = new Intent(MenuActivity.this, FAQActivity.class);
                startActivity(intent);
                break;
            case(R.id.btn_close_menu):
                finish();
                break;
        }

    }

}
