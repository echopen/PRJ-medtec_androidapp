package com.echopen.asso.echopen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.echopen.asso.echopen.utils.Config;

/**
 * Created by alex on 05/07/17.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private Bitmap mImage;

    public HomeFragment() {

    }

    public HomeFragment(Bitmap img) {
        this.mImage = img;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView echoImage = (ImageView) view.findViewById(R.id.echo);
        echoImage.setImageBitmap(mImage);
        echoImage.setColorFilter(Config.colorMatrixColorFilter);

        ImageButton btn = (ImageButton) view.findViewById(R.id.btnGallery);
        btn.setOnClickListener(this);

        ImageButton btnFilters = (ImageButton) view.findViewById(R.id.btnFilter);
        btnFilters.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        onBtnCLick(v.getId());
    }

    public void onBtnCLick(int id) {
        switch (id) {
            // If click on gallery button, we change the activity to the image gallery
            case R.id.btnGallery:
                ((MainActivity)getActivity()).switchActivity();
                break;
            // If click on filter button, we display the filter modal
            case R.id.btnFilter:
                displayFilterModal();
                break;
        }
    }

    public void displayFilterModal() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.filters_modal);
        dialog.show();
    }
}
