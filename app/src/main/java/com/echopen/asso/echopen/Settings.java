package com.echopen.asso.echopen;

/*
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.custom.CustomActivity;
import com.echopen.asso.echopen.model.Data;
import com.echopen.asso.echopen.ui.SettingActionController;


public class Settings extends CustomActivity implements AbstractActionActivity
{

    private ArrayList<Data> sList;

    private SettingActionController settingActionController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initActionController();
        initViewComponents();
    }

    public void initActionController() {
        settingActionController = new SettingActionController(this);
    }


    private void initViewComponents()
    {
        ListView list = (ListView) findViewById(R.id.list);

        View header = getLayoutInflater()
                .inflate(R.layout.setting_header, null);
        list.addHeaderView(header);

        getSettings();
        final SettingAdapter adp = new SettingAdapter();
        list.setAdapter(adp);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String s = sList.get(arg2 - 1).getDesc();
                sList.get(arg2 - 1).setDesc(s == null ? "" : null);
                adp.notifyDataSetChanged();
            }
        });

        settingActionController.displayHeader(header, this);

        getActionBar().setTitle("Settings");
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_friends)
        {
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSettings()
    {
        sList = new ArrayList<Data>();
    }

    private class SettingAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return sList.size();
        }

        @Override
        public Data getItem(int arg0)
        {
            return sList.get(arg0);
        }

        @Override
        public long getItemId(int arg0)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(
                        R.layout.setting_item, null);
            TextView lbl = (TextView) convertView;
            lbl.setText(getItem(position).getTitle());
            if (getItem(position).getDesc() == null)
                lbl.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
                lbl.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.arrow_blue, 0);
            return lbl;
        }

    }
}
