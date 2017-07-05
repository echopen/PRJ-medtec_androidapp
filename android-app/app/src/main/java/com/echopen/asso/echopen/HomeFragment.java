package com.echopen.asso.echopen;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.echopen.asso.echopen.utils.Config;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private Bitmap mImage;
    private ImageView echoImage;

    public HomeFragment() {

    }

    public HomeFragment(Bitmap img) {
        this.mImage = img;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        echoImage = (ImageView) view.findViewById(R.id.echo);
        echoImage.setImageBitmap(mImage);
        echoImage.setColorFilter(Config.colorMatrixColorFilter);

        ImageButton btn = (ImageButton) view.findViewById(R.id.btnGallery);
        btn.setOnClickListener(this);

        ImageButton btnFilters = (ImageButton) view.findViewById(R.id.btnFilter);
        btnFilters.setOnClickListener(this);

        ImageButton btnCapture = (ImageButton) view.findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(this);

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
                ((MainActivity) getActivity()).switchActivity();
                break;
            // If click on filter button, we display the filter modal
            case R.id.btnFilter:
                displayFilterModal();
                break;
            // If click on capture button, we save the last image received
            case R.id.btnCapture:
                ((MainActivity) getActivity()).getImageHandler().saveImage(mImage);
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

    public void refreshImage(Bitmap img) {
        echoImage.setImageBitmap(img);
    }
}
