package com.echopen.asso.echopen;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class ListImagesActivity extends Activity {

    private int clientId;

    public ListImagesActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle b = getIntent().getExtras();
        if(b != null)
            this.clientId = b.getInt("clientId");

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this,this.clientId,getFilesDir()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO: 04/07/17 create links to img view
                Log.d("galleryImageclck","WAZZZZZA");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
