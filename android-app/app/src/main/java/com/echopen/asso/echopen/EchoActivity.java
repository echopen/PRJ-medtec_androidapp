package com.echopen.asso.echopen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.ui.RenderingContextController;

import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_IP;
import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_PORT;

public class EchoActivity extends Activity implements EchographyImageVisualisationContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo);

        RenderingContextController rdController = new RenderingContextController();
        final EchographyImageStreamingService serviceEcho = new EchographyImageStreamingService(rdController);
        final EchographyImageVisualisationPresenter presenter = new EchographyImageVisualisationPresenter(serviceEcho, this);

        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode(REDPITAYA_IP, REDPITAYA_PORT);
        serviceEcho.connect(mode, this);
        presenter.start();
    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView echoImage = (ImageView) findViewById(R.id.img);
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
}
