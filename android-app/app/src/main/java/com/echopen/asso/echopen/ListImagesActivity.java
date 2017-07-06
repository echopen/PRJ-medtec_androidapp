package com.echopen.asso.echopen;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

public class ListImagesActivity extends Activity implements View.OnClickListener {

    private int clientId;

    public ListImagesActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle b = getIntent().getExtras();
        if (b != null)
            this.clientId = b.getInt("clientId");

        ImageButton backBtn = (ImageButton) findViewById(R.id.backToHome);
        backBtn.setOnClickListener(this);

        ImageButton searchBtn = (ImageButton) findViewById(R.id.searchClient);
        searchBtn.setOnClickListener(this);

        // create the gridview to get the picture's list
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, this.clientId, getFilesDir()));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switchActivity(position);
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

    public void displayFilterModal() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.client_modal);


        ListView list = (ListView) dialog.findViewById(R.id.listview);
        list.setAdapter(new ClientAdapter(this, getFilesDir()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                changeClientId(position, dialog);
            }
        });
        dialog.show();
    }

    public void changeClientId(int id, Dialog dialog) {
        //this.clientId = id + 1;
        dialog.dismiss();
        // TODO: 06/07/2017 Partager le client Id  parmis tout les fichiers
        // MainActivity.setClientID(this.clientId);
        // this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backToHome) {
            this.finish();
        } else if (v.getId() == R.id.searchClient) {
            displayFilterModal();
        }
    }
}
