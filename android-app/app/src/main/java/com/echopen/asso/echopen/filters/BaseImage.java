package com.echopen.asso.echopen.filters;

import boofcv.struct.image.ImageSingleBand;

/**
 * BoofCV uses several ImageType : ImageUInt8, ImageFloat32, ImageFloat32. They inherit from ImageSingleBand
 * (or more parently form some ImageBase, but it is not used not confuse between naming convention, here BaseImage - not a good but a convenient reason)
 * all information about BoofCV tyoe format are available here : http://boofcv.org/index.php?title=Tutorial_Images
 */
public class BaseImage extends AbstractBaseImage {

    protected ImageSingleBand imageSingleBand;

    protected BaseImage(ImageSingleBand imageSingleBand) {
        super(imageSingleBand);
    }
}
