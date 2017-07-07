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

public class FAQActivity extends Activity {
    ArrayList<String> faq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_faq);

        ListView langsList=(ListView)findViewById(R.id.list_faq);
        Button btn_back = (Button) findViewById(R.id.back_faq);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        faq = new ArrayList<>();
        faq.add("Problème de prise de vue");
        faq.add("Impossible d'archiver");
        faq.add("La sonde ne se connecte pas");
        faq.add("Le freeze ne s'affiche pas");
        faq.add("L'app est ralentie");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                faq );
        langsList.setAdapter(arrayAdapter);
    }

}
