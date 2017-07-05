package com.echopen.asso.echopen;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.Context.MODE_PRIVATE;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.baby, R.drawable.theme_green,
            R.drawable.theme_blue, R.drawable.theme_green,
            R.drawable.theme_blue, R.drawable.theme_green,
            R.drawable.theme_blue, R.drawable.theme_green,
            R.drawable.theme_blue, R.drawable.theme_green,
            R.drawable.theme_blue, R.drawable.theme_green,
    };



    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        // TextView textview = null;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            // textview = new TextView(mContext);


            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
           //  textview.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
           //  textview.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        // textview.setText(mThumbIds[position+1]);
        return imageView;
    }
}