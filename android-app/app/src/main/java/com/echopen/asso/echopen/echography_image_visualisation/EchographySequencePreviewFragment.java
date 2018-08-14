package com.echopen.asso.echopen.echography_image_visualisation;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.model.EchopenImage;
import com.echopen.asso.echopen.model.EchopenImageSequence;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EchographySequencePreviewFragment extends Fragment implements EchographySequenceSaveContract.View{

    public static final String TAG = EchographySequencePreviewFragment.class.getSimpleName();
    private static final String SEQUENCE_PREVIEW = "sequence-preview";
    private EchographySequenceSaveContract.Presenter mPresenter;

    @BindView(R.id.sequence_speed_bar) IndicatorSeekBar mSpeedSequenceSlider;
    @BindView(R.id.sequence_preview) ImageView mSequencePreview;
    @BindView(R.id.cancel_sequence_preview) ImageView mCancelPreview;
    @OnClick(R.id.cancel_sequence_preview)
    public void cancelSequencePreview(View v){
        mPresenter.cancelPreview();
    }
    @OnClick(R.id.save_sequence)
    public void saveSequencePreview(View v){
        mPresenter.saveSequence();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_sequence_preview, container, false);

        ButterKnife.bind(this, rootView);
        String[] lSpeedSequenceOptions = {"x1.0", "x0.5", "x0.25"};
        mSpeedSequenceSlider.customTickTexts(lSpeedSequenceOptions);
        mSpeedSequenceSlider.setIndicatorTextFormat("${TICK_TEXT}");
        mSpeedSequenceSlider.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if(seekParams.progress == 100){
                    mPresenter.changeSequenceSpeed(SequenceOption.Speed.VERY_SLOW);
                }
                else if(seekParams.progress == 50){
                    mPresenter.changeSequenceSpeed(SequenceOption.Speed.SLOW);
                }
                else{
                    mPresenter.changeSequenceSpeed(SequenceOption.Speed.REGULAR);
                }
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        mSpeedSequenceSlider.setProgress(0.0f);
        mPresenter.start();

        return rootView;
    }


    @Override
    public void refreshImage(Bitmap iBitmap) {
        if(getView() == null || mSequencePreview == null){
            return;
        }

        Log.d(TAG, "refreshImage");
        mSequencePreview.setRotation(MainActivity.IMAGE_ROTATION_FACTOR);
        mSequencePreview.setScaleX(MainActivity.IMAGE_ZOOM_FACTOR);
        mSequencePreview.setScaleY(MainActivity.IMAGE_ZOOM_FACTOR);
        mSequencePreview.setImageBitmap(iBitmap);
    }

    @Override
    public void onDestroyView() {
        mPresenter.stopSequence();
        super.onDestroyView();
    }

    public static EchographySequencePreviewFragment newInstance() {

        EchographySequencePreviewFragment fragment = new EchographySequencePreviewFragment();
        return fragment;
    }

    @Override
    public void setPresenter(EchographySequenceSaveContract.Presenter iPresenter) {
        mPresenter = iPresenter;
    }

}
