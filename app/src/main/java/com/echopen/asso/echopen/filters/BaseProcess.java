package com.echopen.asso.echopen.filters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import boofcv.android.ConvertBitmap;
import boofcv.struct.image.ImageDataType;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSingleBand;
import boofcv.struct.image.ImageUInt8;

/**
 * Created by mehdibenchoufi on 27/08/15.
 */
public class BaseProcess {

    protected Bitmap bitmap;

    protected BaseImage imageBase;

    protected ImageDataType imageDataType;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public BaseProcess(ImageView imageview) {
        bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
    }

    protected void getBackBitmap(){
       if (imageDataType.equals("U8"))
            bitmap = ConvertBitmap.grayToBitmap((ImageUInt8) imageBase.getImage(), Bitmap.Config.ARGB_8888);
        else if (imageDataType.equals("F32"))
           bitmap = ConvertBitmap.grayToBitmap((ImageFloat32) imageBase.getImage(), Bitmap.Config.ARGB_8888);
    }
}
