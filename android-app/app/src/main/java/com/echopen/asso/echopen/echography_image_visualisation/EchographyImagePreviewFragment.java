package com.echopen.asso.echopen.echography_image_visualisation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.model.EchopenImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EchographyImagePreviewFragment extends Fragment implements EchographyImageSaveContract.View{

    private static final String IMAGE_PREVIEW = "image-preview";
    private EchographyImageSaveContract.Presenter mPresenter;
    private EchopenImage mSelectedImage;

    @BindView(R.id.image_preview) ImageView mImagePreview;
    @BindView(R.id.cancel_image_preview) ImageView mCancelPreview;
    @OnClick(R.id.cancel_image_preview)
    public void cancelImagePreview(View v){
        mPresenter.cancelPreview();
    }

    @BindView(R.id.save_image) ImageView mSavePreview;
    @OnClick(R.id.save_image)
    public void saveImagePreview(View v){
        mPresenter.saveImage(mSelectedImage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        Bundle args = getArguments();
        mSelectedImage = (EchopenImage) args.getParcelable(IMAGE_PREVIEW);

        View rootView = inflater.inflate(R.layout.fragment_image_preview, container, false);

        ButterKnife.bind(this, rootView);
        mImagePreview.setImageBitmap(mSelectedImage.getBitmap());
        mImagePreview.setRotation(MainActivity.IMAGE_ROTATION_FACTOR);
        mImagePreview.setScaleX(MainActivity.IMAGE_ZOOM_FACTOR);
        mImagePreview.setScaleY(MainActivity.IMAGE_ZOOM_FACTOR);
        return rootView;
    }

    public static EchographyImagePreviewFragment newInstance(EchopenImage iEchopenImage) {

        Bundle args = new Bundle();
        args.putParcelable(IMAGE_PREVIEW, iEchopenImage);
        EchographyImagePreviewFragment fragment = new EchographyImagePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(EchographyImageSaveContract.Presenter iPresenter) {
        mPresenter = iPresenter;
    }
}
