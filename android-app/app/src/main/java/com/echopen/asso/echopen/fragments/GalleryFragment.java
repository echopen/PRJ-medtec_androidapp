package com.echopen.asso.echopen.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.bdd.Image;
import com.echopen.asso.echopen.bdd.ImageDAO;
import com.echopen.asso.echopen.utils.ImageService;


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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set Typeface
        ImageView firstImg = (ImageView) getActivity().findViewById(R.id.imgView_first);
        TextView textView = (TextView) getActivity().findViewById(R.id.gallery);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Bold.ttf");
        textView.setTypeface(typeface);

        final Button btn_close = (Button) getActivity().findViewById(R.id.btn_closeImg);

        final RelativeLayout showImage = (RelativeLayout) getActivity().findViewById(R.id.layout_showImg);
        showImage.setVisibility(View.INVISIBLE);

        final ImageView imageView = (ImageView) getActivity().findViewById(R.id.showImage) ;

        ImageDAO imageDAO = new ImageDAO(getContext());
        imageDAO.open();
        Image image = imageDAO.getLastImg();

        if (image.getImgName() != null) {
            Bitmap bitmap = ImageService.loadImageFromStorage(image.getImgName());
            firstImg.setImageBitmap(bitmap);
            imageView.setImageBitmap(bitmap);
        }

        final List<Image> images = imageDAO.getAll();
        GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getContext(), images));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                showImage.setVisibility(View.VISIBLE);
                Bitmap bitmap = ImageService.loadImageFromStorage(images.get(position).getImgName());
                imageView.setImageBitmap(bitmap);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showImage.setVisibility(View.INVISIBLE);
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<Image> imageList;

        public ImageAdapter(Context mContext, List imageList) {
            this.mContext = mContext;
            this.imageList = imageList;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);
            if (!imageList.isEmpty()) {
                imageView.setImageBitmap(ImageService.loadImageFromStorage(imageList.get(position).getImgName()));
            }
            return imageView;
        }
    }
}
