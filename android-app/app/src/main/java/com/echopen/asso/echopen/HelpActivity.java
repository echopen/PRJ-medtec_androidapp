package com.echopen.asso.echopen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;


public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.toobar_title);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    Intent files = new Intent(getApplicationContext(), FilesActivity.class);
                    startActivity(files);
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    Intent search = new Intent(getApplicationContext(), ClientActivity.class);
                    startActivity(search);
                    finish();
                    return true;
                case R.id.navigation_help:
                    Intent help = new Intent(getApplicationContext(), HelpActivity.class);
                    startActivity(help);
                    finish();
                    return true;
            }
            return false;
        }

    };

}
