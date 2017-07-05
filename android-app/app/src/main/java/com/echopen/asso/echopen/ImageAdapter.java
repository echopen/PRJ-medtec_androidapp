package com.echopen.asso.echopen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


import static android.content.Context.MODE_PRIVATE;

public class ImageAdapter extends BaseAdapter {

    private File galleryDirectory;
    private int clientId;
    private Context mContext;

        public ImageAdapter(Context c,int clientId,File galleryDirectory) {
            mContext = c;
            this.clientId = clientId;
            this.galleryDirectory = galleryDirectory;
            setmThumbIds();
        }

        public Drawable[] getImages() {
            File[] a = (new File(this.galleryDirectory.toString()+"/"+clientId +"/")).listFiles();
            Drawable[] imagess = new Drawable[5];
            int i =0;
            for(File as : a){
                //Convert bitmap to drawable
                Drawable drawable = new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeFile(as.getPath()));
                imagess[i] = drawable;
                i++;
            }
            return imagess;
        }

    // references to our images
    private Drawable[] mThumbIds;

    public void setmThumbIds() {
        this.mThumbIds = getImages();
    }

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
        if (convertView == null) {
            imageView = new ImageView(mContext);


            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageDrawable(mThumbIds[position]);
        return imageView;
    }
}