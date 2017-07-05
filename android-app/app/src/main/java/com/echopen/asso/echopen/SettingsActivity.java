package com.echopen.asso.echopen;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class SettingsActivity extends Activity {



    int personneStatut = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        ImageView btn_choice_baby = (ImageView) findViewById(R.id.choice_baby);
        ImageView btn_choice_man = (ImageView) findViewById(R.id.choice_man);
        ImageView btn_choice_woman = (ImageView) findViewById(R.id.choice_woman);


        btn_choice_baby.setImageResource(R.drawable.baby_icon_selected);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if(item.getItemId()==R.id.menu_more_settings) {
            //faire ton action associée
        }

        return true;
    }
}
