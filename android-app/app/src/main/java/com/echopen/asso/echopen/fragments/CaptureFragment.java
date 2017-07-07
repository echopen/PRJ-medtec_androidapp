package com.echopen.asso.echopen.fragments;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
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
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.echopen.asso.echopen.utils.ImageService;

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


    public static final String PREFS_PERSONNE = "Preferences_user";
    public static final String PREFS_MORPHO = "Preferences_morpho";
    public static final String PREFS_ORGANES = "Preferences_organes";
    SharedPreferences sharedPreferences;

    public String preference_personne;
    public String preference_morpho;
    public String preference_organes;


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

        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode("192.168.1.33", REDPITAYA_PORT);
        serviceEcho.connect(mode, getActivity());
        presenter.start();

        final Button btn_capture = (Button) getView().findViewById(R.id.btn_capture);
        final ImageButton btn_save = (ImageButton) getView().findViewById(R.id.btn_save);
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
                ImageService.saveToInternalStorage(getContext(), getActivity(), iBitmap);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT);
                toast.show();
                presenter.start();
                btn_capture.setVisibility(View.VISIBLE);
                layout_screenshot.setVisibility(View.INVISIBLE);
            }
        });


        sharedPreferences = getContext().getSharedPreferences(PREFS_PERSONNE, Context.MODE_PRIVATE);


        if (sharedPreferences.contains(PREFS_PERSONNE) && sharedPreferences.contains(PREFS_MORPHO) && sharedPreferences.contains(PREFS_ORGANES)) {

            preference_personne = sharedPreferences.getString(PREFS_PERSONNE, null);
            preference_morpho = sharedPreferences.getString(PREFS_MORPHO, null);
            preference_organes = sharedPreferences.getString(PREFS_ORGANES, null);

            Log.d("HO", preference_morpho + preference_personne);

            switch (preference_personne){
                case "woman":
                    ImageView imageView_personne_woman = (ImageView) getActivity().findViewById(R.id.preference_capture_personne);

                    imageView_personne_woman.setImageResource(R.drawable.woman_icon_white);
                    break;
                case "baby":
                    ImageView imageView_persone_baby = (ImageView) getActivity().findViewById(R.id.preference_capture_personne);

                    imageView_persone_baby.setImageResource(R.drawable.baby_icon_white);
                    break;

                case "man":
                    ImageView imageView_persone_man = (ImageView) getActivity().findViewById(R.id.preference_capture_personne);

                    imageView_persone_man.setImageResource(R.drawable.man_icon_white);
                    break;
            }

            switch (preference_morpho){
                case "s":
                    TextView textView_morpho_s = (TextView) getActivity().findViewById(R.id.preference_capture_morpho);

                    textView_morpho_s.setText(R.string.morpho_s);
                    textView_morpho_s.setTextColor(getResources().getColor(R.color.white));

                    break;
                case "l":
                    TextView textView_morpho_l = (TextView) getActivity().findViewById(R.id.preference_capture_morpho);

                    textView_morpho_l.setText(R.string.morpho_l);
                    textView_morpho_l.setTextColor(getResources().getColor(R.color.white));
                    break;

                case "m":
                    TextView textView_morpho_m = (TextView) getActivity().findViewById(R.id.preference_capture_morpho);

                    textView_morpho_m.setText(R.string.morpho_m);
                    textView_morpho_m.setTextColor(getResources().getColor(R.color.white));

                    break;
            }
            switch (preference_organes){
                case "coeur":
                    ImageView imageView_organes_coeur = (ImageView) getActivity().findViewById(R.id.preference_capture_organe);

                    imageView_organes_coeur.setImageResource(R.drawable.organe1_icon_white);
                    break;
                case "ovaire":
                    ImageView imageView_organes_ovaire = (ImageView) getActivity().findViewById(R.id.preference_capture_organe);

                    imageView_organes_ovaire.setImageResource(R.drawable.organe2_icon_white);
                    break;

                case "poumon":
                    ImageView imageView_organes_poumon = (ImageView) getActivity().findViewById(R.id.preference_capture_organe);

                    imageView_organes_poumon.setImageResource(R.drawable.organe3_icon_white);
                    break;

                case "ventre":
                    ImageView imageView_organes_ventre = (ImageView) getActivity().findViewById(R.id.preference_capture_organe);

                    imageView_organes_ventre.setImageResource(R.drawable.organe4_icon_white);
                    break;
            }





        }



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
