package com.echopen.asso.echopen;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ScreenshotFragment extends Fragment {
    public static ScreenshotFragment newInstance() {
        ScreenshotFragment fragment = new ScreenshotFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screenshot, container, false);
        final ImageView image = (ImageView) view.findViewById(R.id.imageView2);

        final imagesHandler imagesHandler = new imagesHandler(getActivity().getFilesDir());
        image.setImageBitmap(imagesHandler.getLastCachedImage());

        ImageButton back = (ImageButton) view.findViewById(R.id.imageBack);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.frame_layout, ScannerFragment.newInstance());
                transaction.commit();
                return true;
            }
        });

        ImageButton download = (ImageButton) view.findViewById(R.id.imageDownload);
        download.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Bitmap imageBitmap = imagesHandler.drawableToBitmap(image.getDrawable());
                    imagesHandler.saveImage(imageBitmap);
                    Toast.makeText(getActivity(), "Image enregistrée", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        return view;
    }
}