package com.echopen.asso.echopen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.echopen.asso.echopen.custom.CustomActivity;

/**
 * This class will be documented more precisely as much as it is needed. For now on,
 * this class is almost not used but it is the basis of the developement of the data transmission
 * of pictures over network
 */
public class Share extends CustomActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        initViewComponents();
    }

    private void initViewComponents()
    {
        setTouchNClick(R.id.btnDownload);
        setTouchNClick(R.id.btnShare);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v.getId() == R.id.btnDownload)
        {
            finish();
        }
        else if (v.getId() == R.id.btnShare)
        {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/*");
            startActivity(Intent.createChooser(i, "Share via"));
        }
    }
}

