package com.echopen.asso.echopen.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.echopen.asso.echopen.R;

import java.util.ArrayList;

public class LanguesActivity extends Activity {
    ArrayList<String> langs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_langues);

        ListView langsList=(ListView)findViewById(R.id.langues);
        Button btn_back = (Button) findViewById(R.id.back_langues);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        langs = new ArrayList<>();
        langs.add("English");
        langs.add("Français");
        langs.add("Español");
        langs.add("Indian");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                langs );
        langsList.setAdapter(arrayAdapter);
    }

}
