package com.echopen.asso.echopen;

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

import com.echopen.asso.echopen.custom.CustomActivity;
import com.echopen.asso.echopen.model.Data.Data;
import com.echopen.asso.echopen.ui.AbstractActionActivity;
import com.echopen.asso.echopen.ui.SettingActionController;

import java.util.ArrayList;

/**
 * This class stands for the fine tuning settings for the health operator
 * For now, any of them are faked, they will be replaced by real ones in the future
 */
public class Settings extends CustomActivity implements AbstractActionActivity
{

    /* an ArrayList to store the list of settings element */
    private ArrayList<Data> sList;

    /* class that deals with the view of Settings class */
    private SettingActionController settingActionController;

    /** This method calls all the UI methods .
     * Tools are called in the following order :
     * - initActionController() : in order to separate concerns, View parts are handled by the initActionController()
     * - initViewComponents() mainly sets the clickable elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initActionController();
        initViewComponents();
        MainActivity mainActivity = new MainActivity();
    }

    /*
    * initActionController() is used to separate concerns
    * settingActionController sets the main UI elements
    * @param no params
    * */
    public void initActionController() {
        settingActionController = new SettingActionController(this);
    }

    /**
     * Mainly set click listeners to the list fo the setting hold by an adapter,
     * defined in an inner private class SettingAdapter
     */
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

    /**
     * Handles clickable View
     * @param v, the clickable View
     */
    @Override
    public void onClick(View v)
    {
        super.onClick(v);
    }

    /**
     * @param menu, Menu instance
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param item, MenuItem instance
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_friends)
        {
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * throw the setting ArrayList when called in initViewComponents() method
     */
    private void getSettings()
    {
        sList = new ArrayList<Data>();
    }

    /**
     * Adapter class extending BaseAdapter, dealing with the ArrayList sList
     */
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

        /**
         * Positionning some textviews, with the help of setCompoundDrawablesWithIntrinsicBounds
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(
                        R.layout.setting_item, null);
            TextView lbl = (TextView) convertView;
            lbl.setText(getItem(position).getTitle());
            if (getItem(position).getDesc() == null) {
                lbl.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            else {
                lbl.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.arrow_blue, 0);
            }
            return lbl;
        }

    }
}
