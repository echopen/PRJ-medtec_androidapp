package com.echopen.asso.echopen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class DetailsImageActivity extends Activity implements View.OnClickListener {

    private int imageId;
    private int clientId;

    private Drawable mImage;
    private String mImageName;

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
        mImage = ImageHandler.getImageById(imageId);
        mImageName = ImageHandler.getImageName(imageId);

        // set data to the current image
        ImageView detailsImage = (ImageView) findViewById(R.id.detailImage);
        detailsImage.setImageDrawable(mImage);

        // set the toolbar title
        TextView toolbarTitle = (TextView) findViewById(R.id.galleryItem);
        toolbarTitle.setText(mImageName);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backToGallery);
        backBtn.setOnClickListener(this);

        ImageButton btnShare = (ImageButton) findViewById(R.id.shareBtn);
        btnShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backToGallery) {
            this.finish();
        } else if (v.getId() == R.id.shareBtn) {
            shareImage();
        }
    }

    public void shareImage() {
        Bitmap bitmap = ((BitmapDrawable) mImage).getBitmap();
        try {
            File file = new File(this.getCacheDir(), mImageName);
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
