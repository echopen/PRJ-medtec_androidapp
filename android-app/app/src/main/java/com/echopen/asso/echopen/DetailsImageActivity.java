package com.echopen.asso.echopen;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class DetailsImageActivity extends Activity implements View.OnClickListener {

    private int imageId;
    private int clientId;

    private Drawable mImage;
    private String mImageName;
    private File mImageFile;

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

        mImageFile = ImageHandler.getFileByImageId(imageId);

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
        // TODO: 07/07/17 Need to share a image, for the moment olny text is send
        Bitmap bitmap = ((BitmapDrawable)mImage).getBitmap();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        //String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "echo", null);
        Log.e("alex", ""+mImageName);
        Log.e("alex", ""+mImageFile.getPath());

        /*OutputStream stream = new FileOutputStream(mImageFile.get);
        currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Uri bitmapUri = Uri.parse(bitmapPath);
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        sharingIntent.setType("image/png");*/
        /*sharingIntent.setType("text/plain");
        String shareBody = "TODO Share an image and not a text";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);*/
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
