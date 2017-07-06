package com.echopen.asso.echopen;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

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

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, this.clientId, getFilesDir()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switchActivity((int) position);
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
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.client_modal);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backToHome) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (v.getId() == R.id.searchClient) {
            displayFilterModal();
        }
    }
}
