package com.echopen.asso.echopen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class ClientAdapter extends BaseAdapter {
    private  File[] files;
    private Context mContext;

    public ClientAdapter(Context c,File files) {
        mContext = c;
        this.files =(new File(files.toString() + "/" )).listFiles();
    }

    @Override
    public int getCount() {
            if (files != null) {
                return files.length;
            }
            return 0;
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
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.client_item, parent, false);
        } else {
            item = (View) convertView;
        }

        TextView clientItem = (TextView) item.findViewById(R.id.cliendItem);
        clientItem.setText(files[position].getName());


        return item;
    }
}
