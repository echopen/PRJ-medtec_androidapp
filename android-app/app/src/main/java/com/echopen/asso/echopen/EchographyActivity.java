package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.sqlite_database.FeedReaderContract;
import com.echopen.asso.echopen.sqlite_database.FeedReaderDbHelper;

import java.util.UUID;

/**
 * This class handle the displaying of the echography results
 */
public class EchographyActivity extends Activity implements EchographyImageVisualisationContract.View {


    public Bitmap takeScreenshot() {
        View rootView = findViewById(R.id.image);
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }


    private void saveImageToGallery(Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        //bitmap = imageView.getDrawingCache();

        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "/screenshot" + UUID.randomUUID().toString() + ".png", null);
    }

//    public void saveBitmap(Bitmap bitmap) {
//
//        String screenshot = "/screenshot" + UUID.randomUUID().toString() +".png";
//        File imagePath = new File(Environment.getExternalStorageDirectory() + screenshot);
//
//        FileOutputStream fos;
//        try {
//            fos = new FileOutputStream(imagePath);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            Log.e("bitmap", String.valueOf(imagePath));
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            Log.e("bitmap", e.getMessage(), e);
//        }
//    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echography);


        final ImageButton buttonscreenshot = (ImageButton) findViewById(R.id.buttonscreenshot);
        buttonscreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                saveImageToGallery(bitmap);
                AlertDialog alertDialog = new AlertDialog.Builder(EchographyActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You took a screenshot");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        final ImageButton buttonmenu = (ImageButton) findViewById(R.id.buttonmenu);
        buttonmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EchographyActivity.this);

                View view = getLayoutInflater().inflate(R.layout.dialog, null);
                builder.setView(view);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        EchographyImageStreamingService mEchographyImageStreamingService = ((EchOpenApplication) this.getApplication()).getEchographyImageStreamingService();
        // instanciate the streaming service passing by the contextController
        EchographyImageVisualisationContract.Presenter mEchographyImageVisualisationPresenter = new EchographyImageVisualisationPresenter(mEchographyImageStreamingService, this);
        // subscribe to the observable stream
        this.setPresenter(mEchographyImageVisualisationPresenter);
    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        try {
            // on probe output refresh the image view
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView echoImage = (ImageView) findViewById(R.id.image);
                    echoImage.setImageBitmap(iBitmap);
                    Log.e("image", iBitmap.getWidth() + "," + iBitmap.getHeight());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter presenter) {
        presenter.start();
    }

    private long savePerson() {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ECHO_TYPE, "test");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SEX, "restest");

        // Insert the new row, returning the primary key value of the new row
        return db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }
}
