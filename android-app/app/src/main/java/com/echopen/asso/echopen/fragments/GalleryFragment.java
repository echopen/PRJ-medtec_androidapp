package com.echopen.asso.echopen.fragments;


import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.bdd.Image;
import com.echopen.asso.echopen.bdd.ImageDAO;
import com.echopen.asso.echopen.utils.ImageService;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {


    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView firstImg = (ImageView) getActivity().findViewById(R.id.imgView_first);
        TextView textView = (TextView) getActivity().findViewById(R.id.gallery);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Bold.ttf");
        textView.setTypeface(typeface);

        ImageDAO imageDAO = new ImageDAO(getContext());
        imageDAO.open();
        Image image = imageDAO.getLastImg();
        Bitmap bitmap = ImageService.loadImageFromStorage(image.getImgName());
        firstImg.setImageBitmap(bitmap);
    }
}
