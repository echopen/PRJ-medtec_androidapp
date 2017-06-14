package com.echopen.asso.echopen.ui;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.echopen.asso.echopen.R;

public class FilterActionController extends AbstractActionController {

    private Activity activity;

    public void FilterActionController() {
        displayAction();
    }

    public void FilterActionController(Activity activity) {
        this.activity = activity;
        displayAction();
    }

    private void displayAction(){}

    private View findViewById(int id){
        return this.activity.findViewById(id);
    }

    public View displayImage(int pos, View view){
        ImageView img = (ImageView) view;
        if (pos == 0)
            img.setImageResource(R.drawable.filter1);
        else if (pos == 1)
            img.setImageResource(R.drawable.filter2);
        else if (pos == 2)
            img.setImageResource(R.drawable.filter3);
        else if (pos == 3)
            img.setImageResource(R.drawable.filter4);
        else if (pos == 4)
            img.setImageResource(R.drawable.filter5);
        else if (pos == 5)
            img.setImageResource(R.drawable.filter6);
        else if (pos == 6)
            img.setImageResource(R.drawable.filter7);
        else if (pos == 7)
            img.setImageResource(R.drawable.filter8);
        else if (pos == 8)
            img.setImageResource(R.drawable.filter9);
        return view;
    }
}
