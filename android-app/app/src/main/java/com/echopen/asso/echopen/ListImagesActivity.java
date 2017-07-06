package com.echopen.asso.echopen;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
                switchActivity((int)position);
            }
        });
    }
    public void switchActivity(int id) {
        Intent intent = new Intent(this, DetailsImageActivity.class);
        // pass image id to DetailsImageActivity
        Bundle b = new Bundle();
        b.putInt("imageId", id);
        b.putInt("clientId", this.clientId);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
