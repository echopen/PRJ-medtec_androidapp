package com.echopen.asso.echopen.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.utils.Constants;

public class ConstantFragment extends Fragment
{

    private FilterActionController filterActionController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.protocol_listener, null);

        GridView list = (GridView) v.findViewById(R.id.grid);
        list.setAdapter(new GridAdapter());
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                ((MainActivity) getActivity())
                        .goBackFromFragment(Constants.Settings.DISPLAY_PHOTO);
            }
        });
        list.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                ((MainActivity) getActivity()).gesture.onTouchEvent(event);
                return false;
            }
        });
        return v;
    }

    private class GridAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return 9;
        }

        @Override
        public Object getItem(int arg0)
        {
            return null;
        }

        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            if (v == null)
                v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.filter_item, null);

            filterActionController = new FilterActionController();

            return filterActionController.displayImage(pos, v);
        }

    }
}
