package com.echopen.asso.echopen.echography_image_visualisation;

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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EchographySequencePreviewFragment extends Fragment implements EchographyImageSaveContract.View{

    public static final String TAG = EchographySequencePreviewFragment.class.getSimpleName();
    private static final String SEQUENCE_PREVIEW = "sequence-preview";
    private EchographyImageSaveContract.Presenter mPresenter;
    private ScheduledExecutorService mExecutorService;
    private EchopenImageSequence mSequence;

    @BindView(R.id.sequence_preview) ImageView mSequencePreview;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        Bundle args = getArguments();
        mSequence = args.getParcelable(SEQUENCE_PREVIEW);
        Log.d(TAG, " Sequence " + String.valueOf(mSequence.getSequenceSize()));
        View rootView = inflater.inflate(R.layout.fragment_sequence_preview, container, false);

        ButterKnife.bind(this, rootView);
        mExecutorService = Executors.newScheduledThreadPool(1);
        mExecutorService.scheduleAtFixedRate(new Runnable() {
            private int mCurrentImageIndex = 0;

            @Override
            public void run() {
                if(mCurrentImageIndex >= mSequence.getSequenceSize()){
                    mCurrentImageIndex = 0;
                }

                mSequencePreview.setImageBitmap(mSequence.getImage(mCurrentImageIndex).getBitmap());

                Log.d(TAG, String.valueOf(mSequence.getImage(mCurrentImageIndex).getId()));
                Log.d(TAG, "currentIndex"  + String.valueOf(mCurrentImageIndex));

                mSequencePreview.setRotation(MainActivity.IMAGE_ROTATION_FACTOR);
                mSequencePreview.setScaleX(MainActivity.IMAGE_ZOOM_FACTOR);
                mSequencePreview.setScaleY(MainActivity.IMAGE_ZOOM_FACTOR);

                mCurrentImageIndex++;
            }
        },0, 200, TimeUnit.MILLISECONDS);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        mExecutorService.shutdownNow();
        super.onDestroyView();
    }

    public static EchographySequencePreviewFragment newInstance(EchopenImageSequence iSequence) {

        Bundle args = new Bundle();
        args.putParcelable(SEQUENCE_PREVIEW, iSequence);
        EchographySequencePreviewFragment fragment = new EchographySequencePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(EchographyImageSaveContract.Presenter iPresenter) {
        mPresenter = iPresenter;
    }
}
