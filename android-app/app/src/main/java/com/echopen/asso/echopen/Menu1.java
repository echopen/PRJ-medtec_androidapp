package com.echopen.asso.echopen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.model.Synchronizer;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.ui.RulerView;
import com.echopen.asso.echopen.utils.Config;
import com.echopen.asso.echopen.utils.Constants;
import com.echopen.asso.echopen.utils.Strings;
import com.echopen.asso.echopen.utils.Timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



/**
 * Created by admin on 05/07/2017.
 */

//public class Menu1 extends Fragment {
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        //returning our layout file
//        //change R.layout.yourlayoutfilename for each of your fragments
//        return inflater.inflate(R.layout.fragment_menu_1, container, false);
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 1");
//    }
//}


/**
 * A simple {@link Fragment} subclass.
 */
public  class Menu1 extends Fragment implements AbstractActionActivity, EchographyImageVisualisationContract.View {

    private View takePicture;
    private View imageViewBitmap;
    private ImageView goToGallery;
    private SeekBar mSeekBarLinearLutOffset;

    private SeekBar mSeekBarGain;

    private ImageView heartImage;
    private ImageView obstetriqueImage;
    private ImageView poumonImage;
    private ImageView uroImage;
    private ImageView osteoImage;

    private String shootButton;

    /* integer constant that switch whether the photo or the video is on */
    private int display;
    /* class that deals with the view of MainActivity */
    //private MainActionController mainActionController;
    public GestureDetector gesture;
    /* Ruler enables get the overview of body scales */
    public RulerView rulerView;
    /* Setter for rulerView */
    public void setRulerView(RulerView rulerView) {
        this.rulerView = rulerView;
    }
    /* constant setting the process via local, UDP or TCP */
    public static boolean LOCAL_ACQUISITION = false;

    public static boolean TCP_ACQUISITION = true;

    public static boolean UDP_ACQUISITION = false;

    private EchographyImageStreamingService mEchographyImageStreamingService;
    private RenderingContextController mRenderingContextController;

    private EchographyImageVisualisationContract.Presenter mEchographyImageVisualisationPresenter;

    public Menu1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                /* loading config constants in singleton Class */
        Config.getInstance(getActivity());
        Config.singletonConfig.getWidth();
        /* aiming to synchronize views : when operator drags lines to measure distance between points,
         the measure is synchronized with a TextView */
        Synchronizer.getInstance(getActivity());

        mEchographyImageStreamingService = ((EchOpenApplication) getActivity().getApplication() ).getEchographyImageStreamingService();
        mRenderingContextController = mEchographyImageStreamingService.getRenderingContextController();

        mEchographyImageVisualisationPresenter = new EchographyImageVisualisationPresenter(mEchographyImageStreamingService, this);
        this.setPresenter(mEchographyImageVisualisationPresenter);

        final View rootView = inflater.inflate(R.layout.fragment_menu_1, container, false);

        ImageView echoImage = (ImageView) rootView.findViewById(R.id.echo);

        echoImage.setOnTouchListener(new OnSwipeTouchListener(getContext()){

            @Override
            public void onSwipeLeft(float distance){
                mSeekBarLinearLutOffset.setAlpha(1);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                mSeekBarLinearLutOffset.setAlpha(0);
                            }
                        },
                        5000);
                Integer loffset =  mSeekBarLinearLutOffset.getProgress();
                mSeekBarLinearLutOffset.setProgress(loffset + (int)distance /10);
            }

            @Override
            public void onSwipeRight(float distance){
                mSeekBarLinearLutOffset.setAlpha(1);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                mSeekBarLinearLutOffset.setAlpha(0);
                            }
                        },
                        5000);
                Integer loffset =  mSeekBarLinearLutOffset.getProgress();
                mSeekBarLinearLutOffset.setProgress(loffset + (int)distance / 10);
            }

            @Override
            public void onSwipeTop(float distance){
                mSeekBarGain.setAlpha(1);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                mSeekBarGain.setAlpha(0);
                            }
                        },
                        5000);
                Integer loffset =  mSeekBarGain.getProgress();
                mSeekBarGain.setProgress(loffset + (int)distance /10);
            }

        });

        ImageView organ = (ImageView) rootView.findViewById(R.id.organ);
        organ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "click on imageview");

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.organ_modal, null);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();

                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

                wmlp.gravity = Gravity.BOTTOM;
//                wmlp.x = -50;   //x position
                wmlp.y = -100;   //y position
                wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;


                initOrgan(mView);

                heartImage.setOnClickListener(this);
                obstetriqueImage.setOnClickListener(this);
                uroImage.setOnClickListener(this);
                poumonImage.setOnClickListener(this);
                osteoImage.setOnClickListener(this);

                heartImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w("myApp", "click on heartImage");

                        changeHeartButton();
                    }
                });

                obstetriqueImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w("myApp", "click on obstetriqueImage");

                        changeObstetriqueButton();
                    }
                });

                uroImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w("myApp", "click on uroImage");

                        changeUroButton();
                    }
                });

                poumonImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w("myApp", "click on poumonImage");

                        changePoumonButton();
                    }
                });

                osteoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w("myApp", "click on osteoImage");

                        changeOsteoButton();
                    }
                });

                dialog.show();

            }
        });
        ImageView showSettings = (ImageView) rootView.findViewById(R.id.settings);
        showSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBarLinearLutOffset.setAlpha(1);
                mSeekBarGain.setAlpha(1);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                mSeekBarLinearLutOffset.setAlpha(0);
                                mSeekBarGain.setAlpha(0);
                            }
                        },
                        5000);
//                Animation animation =
//                        AnimationUtils.loadAnimation(getContext(),
//                                R.anim.fade);
//                mSeekBarLinearLutSlope.startAnimation(animation);

            }


        });

        init(inflater, container, rootView);

        initImageManipulationViewComponents(rootView);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        goToGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "take pictures");

                displayGallery(rootView);
            }
        });




        // Inflate the layout for this fragment
        return rootView;
    }

    private void changeHeartButton() {
        heartImage.setImageResource(R.drawable.cardiaque_blue);
        obstetriqueImage.setImageResource(R.drawable.obstetrique);
        uroImage.setImageResource(R.drawable.uro_digestif);
        poumonImage.setImageResource(R.drawable.poumon);
        osteoImage.setImageResource(R.drawable.osteo_articulaire2);

        shootButton = (String) "cardiaque";
    }

    private void changeObstetriqueButton() {
        heartImage.setImageResource(R.drawable.heart);
        obstetriqueImage.setImageResource(R.drawable.obstetrique_blue);
        uroImage.setImageResource(R.drawable.uro_digestif);
        poumonImage.setImageResource(R.drawable.poumon);
        osteoImage.setImageResource(R.drawable.osteo_articulaire2);

        shootButton = (String) "obstetrique";

    }

    private void changePoumonButton() {

        heartImage.setImageResource(R.drawable.heart);
        obstetriqueImage.setImageResource(R.drawable.obstetrique);
        uroImage.setImageResource(R.drawable.uro_digestif);
        poumonImage.setImageResource(R.drawable.poumon_blue);
        osteoImage.setImageResource(R.drawable.osteo_articulaire2);

        shootButton = (String) "poumon";
    }

    private void changeUroButton() {

        heartImage.setImageResource(R.drawable.heart);
        obstetriqueImage.setImageResource(R.drawable.obstetrique);
        uroImage.setImageResource(R.drawable.uro_digestif2_blue);
        poumonImage.setImageResource(R.drawable.poumon);
        osteoImage.setImageResource(R.drawable.osteo_articulaire2);

        shootButton = (String) "uro_digestif";
    }

    private void changeOsteoButton() {

        heartImage.setImageResource(R.drawable.heart);
        obstetriqueImage.setImageResource(R.drawable.obstetrique);
        uroImage.setImageResource(R.drawable.uro_digestif);
        poumonImage.setImageResource(R.drawable.poumon);
        osteoImage.setImageResource(R.drawable.osteo_articulaire_blue);
        shootButton = (String) "osteo_articulaire";
    }

    private void displayGallery(View rootView) {

        //initializing the fragment object which is selected
        Fragment fragment = new GalleryFragment();

        //replacing the fragment
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    /**
     * @param inflater
     * @param container
     * @param rootView
     */
    public void init(LayoutInflater inflater, ViewGroup container, View rootView) {
        imageViewBitmap = rootView.findViewById(R.id.echo);
        takePicture = rootView.findViewById(R.id.btnCapture);
        goToGallery = (ImageView) rootView.findViewById(R.id.gallery);
    }

    public void initOrgan(View mView) {
        heartImage = (ImageView) mView.findViewById(R.id.heart);
        obstetriqueImage = (ImageView) mView.findViewById(R.id.obstetrique);
        poumonImage  = (ImageView) mView.findViewById(R.id.poumon);
        uroImage  = (ImageView) mView.findViewById(R.id.uro_digestif);
        osteoImage = (ImageView) mView.findViewById(R.id.osteo_articulaire);
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * save image in gallery
     */
    public void saveImage() {
        FileOutputStream fileOutputStream = null;
        File file = getDisc();

        if (!file.exists() && !file.mkdirs()) {
            Toast.makeText(getActivity(), "Can't create directory for image", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss", Locale.FRANCE);
        String date = simpleDateFormat.format(new Date());
        String name = "Echo" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);

        try {
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(imageViewBitmap, imageViewBitmap.getWidth(), imageViewBitmap.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            Toast.makeText(getActivity(), "Save image success", Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshGallery(new_file);
    }

    /**
     * Refresh gallery
     * @param file
     */
    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        getActivity().sendBroadcast(intent);
    }

    /**
     * get directory file
     * @return
     */
    private File getDisc() {

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Image");
    }

    @Override
    public void onResume(){
        super.onResume();
        mEchographyImageVisualisationPresenter.start();
        EchographyImageStreamingTCPMode tcpMode = new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT);
        mEchographyImageStreamingService.connect(tcpMode, getActivity());
    }


    private void initImageManipulationViewComponents(View rootView) {
        mSeekBarLinearLutOffset = (SeekBar) rootView.findViewById(R.id.seekBarLinearLutOffset);
        mSeekBarLinearLutOffset.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double lOffset = progress * 256 / 100;
//                mTextViewLinearLutOffset.setText(new DecimalFormat("#.00").format(lOffset));
                mRenderingContextController.setLinearLutOffset(lOffset);
            }
        });

        mSeekBarGain = (SeekBar) rootView.findViewById(R.id.seekBarGain);
        mSeekBarGain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double lGain = progress * 3.0 / 100;
                mRenderingContextController.setIntensityGain(lGain);
            }
        });



//        mLayoutExponentialLutAlpha = (LinearLayout) findViewById(R.id.layoutExponentialLutAlpha);
//        mTextViewExponentialLutAlpha = (TextView) findViewById(R.id.textExponentialLutAlpha);
//        mSeekBarExponentialLutAlpha = (SeekBar) findViewById(R.id.seekBarExponentialLutAlpha);
//        mSeekBarExponentialLutAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                double lAlpha = 0.001 * progress;
//                mTextViewExponentialLutAlpha.setText(new DecimalFormat("#.00").format(lAlpha));
//                mRenderingContextController.setExponentialLutAlpha(lAlpha);
//            }
//        });

//        Spinner lDropdownLut = (Spinner)findViewById(R.id.dropdownLut);
//        String[] lLutItems = new String[]{"Linear Lut", "Exponential Lut"};
//        ArrayAdapter<String> lLutItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lLutItems);
//        lDropdownLut.setAdapter(lLutItemsAdapter);

//        lDropdownLut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                switch(i){
//                    case 0:
//                        // linear lut
//                        selectLinearLut();
//                        break;
//                    case 1:
//                        // exponential lut
//                        selectExponentialLut();
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        lDropdownLut.setSelection(0);
    }

//    private void selectLinearLut(){
//        mSeekBarLinearLutOffset.setProgress(0);
//        mSeekBarLinearLutSlope.setProgress(50);
//        mLayoutLinearLutOffset.setVisibility(View.VISIBLE);
//        mLayoutLinearLutSlope.setVisibility(View.VISIBLE);
//
//        mLayoutExponentialLutAlpha.setVisibility(View.GONE);
//    }

//    private void selectExponentialLut(){
//
//        mLayoutLinearLutOffset.setVisibility(View.GONE);
//        mLayoutLinearLutSlope.setVisibility(View.GONE);
//
//        mSeekBarExponentialLutAlpha.setProgress(0);
//        mLayoutExponentialLutAlpha.setVisibility(View.VISIBLE);
//    }

    /**
     * @param item, MenuItem instance
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    /**
     * Following the doc https://developer.android.com/intl/ko/training/basics/intents/result.html,
     * onActivityResult is “Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.”,
     * See more here : https://stackoverflow.com/questions/20114485/use-onactivityresult-android
     *
     * @param requestCode, integer argument that identifies your request
     * @param resultCode, to get its values, check RESULT_CANCELED, RESULT_OK here https://developer.android.com/reference/android/app/Activity.html#RESULT_OK
     * @param data,       Intent instance
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView echoImage = (ImageView) getView().findViewById(R.id.echo);
                    echoImage.setImageBitmap(iBitmap);
                    echoImage.setColorFilter(Config.colorMatrixColorFilter);
                    Timer.logResult("Display Bitmap");
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter iPresenter) {
        mEchographyImageVisualisationPresenter = iPresenter;
    }

    @Override
    public void initActionController() {

    }
}