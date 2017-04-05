package com.echopen.asso.echopen.utils;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class TouchEffect implements OnTouchListener
{
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{

		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			Drawable d = v.getBackground();
			d.mutate();
			d.setAlpha(150);
			v.setBackgroundDrawable(d);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL)
		{
			Drawable d = v.getBackground();
			d.setAlpha(255);
			v.setBackgroundDrawable(d);
		}
		return false;
	}

}
