package com.echopen.asso.echopen;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.icu.text.LocaleDisplayNames;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class ImageAdapter extends BaseAdapter {

    private File galleryDirectory;
    private int clientId;
    private Context mContext;
    // references to our images
    private Drawable[] mThumbIds;
    private File[] files;

    public ImageAdapter(Context c, int clientId, File galleryDirectory) {
        mContext = c;
        this.clientId = clientId;
        this.galleryDirectory = galleryDirectory;
        files = (new File(this.galleryDirectory.toString() + "/" + clientId + "/")).listFiles();
        setmThumbIds(files);

    }

    // TODO: 06/07/2017 Refactoriser pour avoir la meme source de provenance de data ( tableau to list )
    public Drawable[] getImages(File[] files) {
            Drawable[] validImages = new Drawable[files.length];
            int i = 0;
            if (files != null) {
                for (File as : files) {
                    //Convert bitmap to drawable
                    Drawable drawable = new BitmapDrawable(mContext.getResources(), BitmapFactory.decodeFile(as.getPath()));
                    try {
                        validImages[i] = drawable;
                        i++;
                    } catch ( ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        return validImages;
    }

    public void setmThumbIds(File[] files) {
        if (files != null) {
            this.mThumbIds = getImages(files);
        }
    }

    public int getCount() {
        if (files != null) {
            return mThumbIds.length;
        }
        return 0;
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
        View item = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.gallery_item, parent, false);
        }
        else {
            item = (View) convertView;
        }

        ImageView galleryImage;
        TextView galleryTitle;

        galleryImage = (ImageView) item.findViewById(R.id.galleryImage);
        galleryTitle = (TextView) item.findViewById(R.id.galleryTitle);

        galleryImage.setImageDrawable(mThumbIds[position]);
        galleryTitle.setText("Hello");

        return item;
    }
}