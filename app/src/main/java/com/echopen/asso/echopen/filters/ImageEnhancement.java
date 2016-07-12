package com.echopen.asso.echopen.filters;

import android.widget.ImageView;

import boofcv.alg.enhance.EnhanceImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.android.ConvertBitmap;
import boofcv.struct.image.ImageUInt8;

/**
 * Created by mehdibenchoufi on 02/02/16.
 */
public class ImageEnhancement extends BaseProcess {

    private ImageUInt8 gray;
    private ImageUInt8 adjusted;

    private int histogram[] = new int[256];
    private int transform[] = new int[256];


    public ImageEnhancement(ImageView imageview) {
        super(imageview);
        imageBase = new BaseImage(ConvertBitmap.bitmapToGray(bitmap, (ImageUInt8) null, null));
        imageDataType = imageBase.getImage().getDataType();
        gray = (ImageUInt8) imageBase.getImage();
        adjusted = gray._createNew(gray.getWidth(), gray.getHeight());
    }

    private void enhancing() {
        ImageStatistics.histogram(gray, histogram);
        EnhanceImageOps.equalize(histogram, transform);
        EnhanceImageOps.applyTransform(gray, transform, adjusted);

        imageBase.setImage(adjusted);
    }

    public void enhance() {
        enhancing();
        getBackBitmap();
    }
}
