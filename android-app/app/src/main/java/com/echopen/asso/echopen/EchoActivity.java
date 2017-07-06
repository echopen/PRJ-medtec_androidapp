package com.echopen.asso.echopen;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.ui.RenderingContextController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_IP;
import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_PORT;



public class EchoActivity extends Activity implements EchographyImageVisualisationContract.View {

    private String imageNameSave = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo);
        EchographyImageStreamingService serviceEcho = ((EchOpenApplication)getApplication()).getEchographyImageStreamingService();

        EchographyImageVisualisationPresenter presenter = new EchographyImageVisualisationPresenter(serviceEcho, this);

        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode(REDPITAYA_IP, REDPITAYA_PORT);
        serviceEcho.connect(mode, this);
        this.setPresenter(presenter);

        Button save_pics = (Button)findViewById(R.id.save_pics);
        save_pics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = (ImageView)findViewById(R.id.echo_iv);
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Log.e("SAVED", saveToInternalStorage(bitmap));
            }
        });

        Button load_pics = (Button)findViewById(R.id.load_pics);
        load_pics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ImageView echoImage = (ImageView) findViewById(R.id.echo_iv);
                    echoImage.setImageBitmap(iBitmap);

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter presenter) {
        presenter.start();
    }

    private String saveToInternalStorage (Bitmap iBitmap){
        ContextWrapper cw = new ContextWrapper(EchoActivity.this);
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        Long tsLong = System.currentTimeMillis() / 1000;
        String imgName = tsLong.toString()+ ".jpg";
        imageNameSave = imgName ;

        File mypath = new File(directory, imgName);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(mypath);
            iBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" +imgName;
    }

    private void loadImage (){
        try {
            ContextWrapper cw = new ContextWrapper(EchoActivity.this);
            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            String mypath = directory.getAbsolutePath();
            File f = new File(mypath, imageNameSave);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = (ImageView) findViewById(R.id.echo_iv_load);
            img.setImageBitmap(b);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
