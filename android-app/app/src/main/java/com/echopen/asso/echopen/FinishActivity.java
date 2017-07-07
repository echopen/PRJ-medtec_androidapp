package com.echopen.asso.echopen;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

public class FinishActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        String id = (String) getIntent().getSerializableExtra("id");
        TextView title = (TextView) findViewById(R.id.title);
        TextView text_finish = (TextView) findViewById(R.id.text_finish);
        TextView id_patient = (TextView) findViewById(R.id.id_patient);
        ImageButton button_dashboard = (ImageButton) findViewById(R.id.button_dashboard);
        button_dashboard.setOnClickListener(this);
        id_patient.setText(id);

        setFont(title,"Moderat-Bold.ttf");
        setFont(text_finish,"Avenir-Book.ttf");
        setFont(id_patient,"Moderat-Bold.ttf");
    }

    public void setFont(TextView textView, String fontName) {
        if(fontName != null){
            try {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/" + fontName);
                textView.setTypeface(typeface);
            } catch (Exception e) {
                Log.e("FONT", fontName + " not found", e);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_dashboard:
                startActivity(new Intent( this, DashboardActivity.class));
                break;
        }
    }
}
