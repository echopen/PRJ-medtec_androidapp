package com.echopen.asso.echopen;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DashboardActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ImageButton button_exam = (ImageButton) findViewById(R.id.button_exam);
        button_exam.setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.title);
        TextView welcome_text_1 = (TextView) findViewById(R.id.welcome_text_1);
        TextView welcome_text_2 = (TextView) findViewById(R.id.welcome_text_2);
        TextView welcome_name = (TextView) findViewById(R.id.welcome_name);

        setFont(title,"Moderat-Bold.ttf");
        setFont(welcome_text_1,"Avenir-Book.ttf");
        setFont(welcome_text_2,"Avenir-Book.ttf");
        setFont(welcome_name,"Avenir-Heavy.ttf");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_exam:
                startActivity(new Intent( this, SettingsActivity.class));
                break;
        }
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
}
