package com.echopen.asso.echopen.model.EchoImage;

import android.graphics.Bitmap;

/**
 * Created by mehdibenchoufi on 21/10/15.
 */
public abstract class AbstractEchoImage {

    private Bitmap bitmap;

    public Bitmap createImage(){
        return accept(bitmap);
    }

    abstract protected Bitmap accept(Bitmap bitmap);
}
