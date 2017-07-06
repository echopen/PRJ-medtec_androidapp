package com.echopen.asso.echopen;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsImageActivity extends Activity implements View.OnClickListener {

    private int imageId;
    private int clientId;

    public DetailsImageActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            this.imageId = b.getInt("imageId");
            this.clientId = b.getInt("clientId");
        }

        ImageHandler ImageHandler = new ImageHandler(getFilesDir(), this.clientId);
        ImageHandler.setContext(this);

        // recovering data for the current image
        Drawable detailImage = ImageHandler.getImageById(imageId);
        String detailName = ImageHandler.getImageName(imageId);

        // set data to the current image
        ImageView detailsImage = (ImageView) findViewById(R.id.detailImage);
        detailsImage.setImageDrawable(detailImage);

        // set the toolbar title
        TextView toolbarTitle = (TextView) findViewById(R.id.galleryItem);
        toolbarTitle.setText(detailName);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backToGallery);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backToGallery) {
            this.finish();
        }
    }
}
