package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        Button enterBt = (Button) findViewById(R.id.enter_bt);
        enterBt.setOnClickListener(this);

        Button signintBt = (Button) findViewById(R.id.signin_bt);
        signintBt.setOnClickListener(this);

        Button emergencyBt = (Button) findViewById(R.id.emergency_bt);
        emergencyBt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.signin_bt :
                btSignInClicked();
                break;
            case R.id.emergency_bt :
                btEmergencyClicked();
                break;
            case R.id.enter_bt :
                btEnterClicked();
                break;
        }
    }

    private void btSignInClicked(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void btEmergencyClicked(){
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
    }

    private void btEnterClicked(){
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

}
