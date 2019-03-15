package com.echopen.asso.echopen.echography_image_visualisation;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationWifiNotification;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationWifiNotificationState;
import com.echopen.asso.echopen.view.CaptureButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EchographyImageVisualisationFragment extends Fragment implements EchographyImageVisualisationContract.View{

    public static final String TAG = EchographyImageVisualisationFragment.class.getSimpleName();
    private EchographyImageVisualisationContract.Presenter mEchographyImageVisualisationPresenter;

    @BindView(R.id.main_preset_configuration) ImageView mPresetConfigurationButton;
    @OnClick(R.id.main_preset_configuration)
    public void presetConfigurationChange(View v){
        Log.d(TAG, "Preset configuration changed");

    }
    @BindView(R.id.main_button_end_exam) ImageView mEndExamButton;
    @OnClick(R.id.main_button_end_exam)
    public void endExam(View v){
        Log.d(TAG, "EndExamButton Pressed");
    }
    @BindView(R.id.main_button_battery) ImageView mBatteryStatus;
    @BindView(R.id.main_selected_organ) ImageView mSelectedOrgan;
    @BindView(R.id.main_button_shadow) CaptureButton mCaptureShadow;
    @BindView(R.id.pending_text) TextView mPendingTextView;
    @BindView(R.id.pending_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.pending_layout) LinearLayout mPendingLayout;


    private final static float IMAGE_ZOOM_FACTOR = 1.75f;
    private final static float IMAGE_ROTATION_FACTOR = 90.f;

    public EchographyImageVisualisationFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        mCaptureShadow.setListener(new CaptureButton.CaptureButtonListener() {
            @Override
            public void onTouchDown() {
                Log.d(TAG, "OnStartRecording");
                mEchographyImageVisualisationPresenter.startRecording();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "OnCancel");
                mEchographyImageVisualisationPresenter.endRecording();
            }

            @Override
            public void onShortPress() {
                Log.d(TAG, "onShortPress");
                mEchographyImageVisualisationPresenter.previewImage();
                mEchographyImageVisualisationPresenter.endRecording();
            }

            @Override
            public void onLongPress() {
                Log.d(TAG, "onLongPress");
                mEchographyImageVisualisationPresenter.previewSequence();
                mEchographyImageVisualisationPresenter.endRecording();
            }

        });

        return rootView;
    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(getView() == null){
                    return;
                }

                Log.d(TAG, "refreshImage");
                ImageView lEchOpenImage = (ImageView) getView().findViewById(R.id.echopenImage);
                lEchOpenImage.setRotation(IMAGE_ROTATION_FACTOR);
                lEchOpenImage.setScaleX(IMAGE_ZOOM_FACTOR);
                lEchOpenImage.setScaleY(IMAGE_ZOOM_FACTOR);
                lEchOpenImage.setImageBitmap(iBitmap);
            }
        });
    }

    @Override
    public void displayWifiProgress(String iMessage) {
        if(getView() == null){
            return;
        }

        mPendingLayout.setVisibility(View.VISIBLE);

        mPendingTextView.setText(iMessage);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayWifiError(String iErrorMessage) {
        mPendingLayout.setVisibility(View.VISIBLE);

        mPendingTextView.setText(iErrorMessage);
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void closeWifiProgress() {
        mPendingLayout.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter iPresenter) {
        mEchographyImageVisualisationPresenter = iPresenter;
        mEchographyImageVisualisationPresenter.start();
    }
}
