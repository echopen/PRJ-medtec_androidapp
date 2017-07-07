package com.echopen.asso.echopen.fragments;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.echopen.asso.echopen.MenuActivity;
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
import com.echopen.asso.echopen.utils.ImageService;
import com.triggertrap.seekarc.SeekArc;

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
        final Button btn_menu = (Button) getView().findViewById(R.id.btn_menu_main);
        final LinearLayout layout_screenshot = (LinearLayout) getView().findViewById(R.id.layout_screenshot);
        layout_screenshot.setVisibility(View.INVISIBLE);

        final SeekArc seekBar_gain = (SeekArc) getView().findViewById(R.id.seekArc_gain);
        seekBar_gain.setVisibility(View.INVISIBLE);

        btn_gain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekBar_gain.setVisibility(View.VISIBLE);
            }
        });

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
                ImageService.saveToInternalStorage(getContext(), getActivity(), iBitmap);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT);
                toast.show();
                presenter.start();
                btn_capture.setVisibility(View.VISIBLE);
                layout_screenshot.setVisibility(View.INVISIBLE);
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);

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
        //
    }
}
