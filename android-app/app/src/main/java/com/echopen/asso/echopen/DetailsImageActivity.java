package com.echopen.asso.echopen;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class DetailsImageActivity extends Activity {


    private int imageId;
    private ImageView detailsImage;
    private int clientId;

    public DetailsImageActivity()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageView detailsImage = (ImageView) findViewById(R.id.detailImage);

        Bundle b = getIntent().getExtras();
        if(b != null)
            this.imageId = b.getInt("imageId");
            this.clientId = b.getInt("clientId");

        ImageHandler ImageHandler = new ImageHandler(getFilesDir(), this.clientId);
        ImageHandler.setContext(this);
        Drawable detailImage = ImageHandler.getImageById(imageId);
        detailsImage.setImageDrawable(detailImage);
    }



}
