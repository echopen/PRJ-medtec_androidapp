package com.echopen.asso.echopen;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HomeActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(HomeActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn:
                goDetailsActivity();
                break;
        }
    }

    private void goDetailsActivity(){
        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
