package com.echopen.asso.echopen.filters;

import boofcv.struct.image.ImageSingleBand;

/**
 * getImage() will be needed at least by BaseProcess to get through back to the caller the Bitmap taht will be set as
 * app's ImageView
 */
public class AbstractBaseImage {
    protected ImageSingleBand imageSingleBand;

    protected AbstractBaseImage(ImageSingleBand imageSingleBand) {
        this.imageSingleBand = imageSingleBand;
    }

    protected ImageSingleBand getImage(){
        return imageSingleBand;
    }

    protected void setImage(ImageSingleBand image){
        imageSingleBand = image;
    }
}
