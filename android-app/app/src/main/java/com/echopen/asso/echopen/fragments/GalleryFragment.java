package com.echopen.asso.echopen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.bdd.Image;
import com.echopen.asso.echopen.bdd.ImageDAO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    List<Image> list;
    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ImageDAO imgDAO = new ImageDAO(getActivity());
        imgDAO.open();
        list = imgDAO.getAll();

        return inflater.inflate(R.layout.fragment_gallery2, container, false);
    }

}
