package com.echopen.asso.echopen.fragments;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.bdd.Image;
import com.echopen.asso.echopen.bdd.ImageDAO;
import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.filters.BaseProcess;
import com.echopen.asso.echopen.filters.ImageEnhancement;
import com.echopen.asso.echopen.ui.RenderingContextController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_PORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaptureFragment extends Fragment implements EchographyImageVisualisationContract.View {


    public CaptureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RenderingContextController rdController = new RenderingContextController();
        final EchographyImageStreamingService serviceEcho = new EchographyImageStreamingService(rdController);
        final EchographyImageVisualisationPresenter presenter = new EchographyImageVisualisationPresenter(serviceEcho, this);

        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode("10.37.214.123", REDPITAYA_PORT);
        serviceEcho.connect(mode, getActivity());
        presenter.start();

        final Button btn_capture = (Button) getView().findViewById(R.id.btn_capture);
        final Button btn_save = (Button) getView().findViewById(R.id.btn_save);
        final LinearLayout layout_screenshot = (LinearLayout) getView().findViewById(R.id.layout_screenshot);
        layout_screenshot.setVisibility(View.INVISIBLE);

        btn_capture.setOnClickListener(new View.OnClickListener() {
            //Freeze picture & hide take button
            public void onClick(View v) {
                serviceEcho.deleteObservers();
                btn_capture.setVisibility(View.INVISIBLE);
                layout_screenshot.setVisibility(View.VISIBLE);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImageView image = (ImageView) getView().findViewById(R.id.echo_view);
                Bitmap iBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Log.d("SAVED", saveToInternalStorage(iBitmap));
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT);
                toast.show();
                presenter.start();
                btn_capture.setVisibility(View.VISIBLE);
                layout_screenshot.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView echoImage = (ImageView) getView().findViewById(R.id.echo_view);
                    echoImage.setImageBitmap(iBitmap);
                    //Timer.logResult("Display Bitmap");
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter presenter) {

    }

    private String saveToInternalStorage(Bitmap iBitmap) {

        //Open Database and Load ImageDAO
        ImageDAO imageDAO = new ImageDAO(getContext());
        imageDAO.open();

        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        Long tsLong = System.currentTimeMillis() / 1000;
        String imgName = tsLong.toString() + ".jpg";
        // Create imageDir
        File mypath = new File(directory, imgName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            iBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // Save the directory path with name Image
            Image img = new Image();
            img.setImgName(directory.getAbsolutePath() + "/" + imgName);
            imageDAO.add(img);

            List imgs = imageDAO.getAll();
            Log.d("LIST", imgs.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" + imgName;
    }

    private void loadImageFromStorage(String path) {
        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = (ImageView) getView().findViewById(R.id.echo_view);
            img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
