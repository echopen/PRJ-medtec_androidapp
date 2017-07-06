package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.Constants;

/**
 * MainActivity class handles the main screen of the app.
 */

public class MainActivity extends Activity implements EchographyImageVisualisationContract.View, View.OnClickListener {

    private ImageHandler ImageHandler;

    private int clientId;

    private ImageView echoImage;
    private Bitmap mImage;

    private Boolean canTakePicture = true;

    /**
     * This method calls all the UI methods and then gives hand to  UDPToBitmapDisplayer class.
     * UDPToBitmapDisplayer listens to UDP data, processes them with the help of ScanConversion,
     * and then displays them.
     * Also, this method uses the Config singleton class that provides device-specific constants
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EchographyImageStreamingService echographyImageStreamingService = ((EchOpenApplication) this.getApplication()).getEchographyImageStreamingService();
        EchographyImageVisualisationContract.Presenter presenter = new EchographyImageVisualisationPresenter(echographyImageStreamingService, this);


        EchographyImageStreamingTCPMode lTCPMode = new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT);
        echographyImageStreamingService.connect(lTCPMode, this);
        presenter.start();

        initViewListeners();

        if (getFilesDir().listFiles().length > 0) {
            // set client ID ( number of client folders +1 )
            this.clientId = getFilesDir().listFiles().length + 1;
        } else {
            this.clientId = 1;
        }

        // create file handler to save images
        ImageHandler = new ImageHandler(getFilesDir(), this.clientId);
    }

    public void initViewListeners() {
        echoImage = (ImageView) findViewById(R.id.echo);

        ImageButton btn = (ImageButton) findViewById(R.id.btnGallery);
        btn.setOnClickListener(this);

        ImageButton btnFilters = (ImageButton) findViewById(R.id.btnFilter);
        btnFilters.setOnClickListener(this);

        ImageButton btnCapture = (ImageButton) findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(this);

        ImageButton btnDone = (ImageButton) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
    }

    public void switchActivity() {
        Intent intent = new Intent(this, ListImagesActivity.class);
        // pass Client Id to listImagesActivity
        Bundle b = new Bundle();
        b.putInt("clientId", this.clientId);
        intent.putExtras(b);
        startActivity(intent);
    }

    public ImageHandler getImageHandler() {
        return this.ImageHandler;
    }

    public void cantTakePictureNow() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Can't take picture now")
                .setMessage("The probe is not correctly connected or you click too fast on capture button.")
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 06/07/17 do something
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Following the doc https://developer.android.com/intl/ko/training/basics/intents/result.html,
     * onActivityResult is “Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.”,
     * See more here : https://stackoverflow.com/questions/20114485/use-onactivityresult-android
     *
     * @param requestCode, integer argument that identifies your request
     * @param resultCode,  to get its values, check RESULT_CANCELED, RESULT_OK here https://developer.android.com/reference/android/app/Activity.html#RESULT_OK
     * @param data,        Intent instance
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        try {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mImage == null) {
                        findViewById(R.id.loader).setVisibility(View.GONE);
                        findViewById(R.id.loader_text).setVisibility(View.GONE);
                        findViewById(R.id.echo).setVisibility(View.VISIBLE);
                    }
                    mImage = iBitmap;
                    echoImage.setImageBitmap(iBitmap);
                    echoImage.setColorFilter(Config.colorMatrixColorFilter);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter presenter) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // If click on gallery button, we change the activity to the image gallery
            case R.id.btnGallery:
                switchActivity();
                break;
            // If click on filter button, we display the filter modal
            case R.id.btnFilter:
                displayFilterModal();
                break;
            // If click on capture button, we save the last image received
            case R.id.btnCapture:
                onCapturePhoto();
                break;
            case R.id.btnDone:
                int newId = this.clientId + 1;
                ImageHandler.setClientId(newId);
                this.clientId = newId;
                break;
        }
    }

    /**
     * Function called when we clicked on the large button in center
     */
    public void onCapturePhoto() {
        if (mImage != null & canTakePicture) {
            canTakePicture = false;
            Toast.makeText(this, "Photo taken !", Toast.LENGTH_SHORT).show();
            findViewById(R.id.captureImg).setVisibility(View.VISIBLE);
            mImage.toString();
            getImageHandler().saveImage(mImage);
            findViewById(R.id.captureImg).setVisibility(View.GONE);
            canTakePicture = true;
        } else {
            cantTakePictureNow();
        }
    }

    /**
     * Function called when we clicked on the filter button (baby)
     */
    public void displayFilterModal() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.filters_modal);
        dialog.show();
    }
}