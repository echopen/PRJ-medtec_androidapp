package com.echopen.asso.echopen;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class ImageFragment extends Fragment {

    private int position;

    public static ImageFragment newInstance(int position) {
        ImageFragment fragment = new ImageFragment();
        fragment.setPosition(position);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        final imagesHandler imagesHandler = new imagesHandler(getActivity().getFilesDir());
        ImageView image = (ImageView) view.findViewById(R.id.imageView3);
        final int position = this.position;

        image.setImageBitmap(imagesHandler.getSavedImage(this.position));

        ImageButton back = (ImageButton) view.findViewById(R.id.imageBack);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.frame_layout, ArchivesFragment.newInstance());
                transaction.commit();
                return true;
            }
        });

        ImageButton delete = (ImageButton) view.findViewById(R.id.imageDelete);
        delete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imagesHandler.deleteImage(position);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, ArchivesFragment.newInstance());
                    transaction.commit();
                    Toast.makeText(getActivity(), "Image Supprimée", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });



        return view;
    }

    private void setPosition(int position) {
        this.position = position;
    }
}
