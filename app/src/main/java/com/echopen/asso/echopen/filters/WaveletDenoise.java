package com.echopen.asso.echopen.filters;

import android.widget.ImageView;

import java.util.Random;

import boofcv.abst.denoise.FactoryImageDenoise;
import boofcv.abst.denoise.WaveletDenoiseFilter;
import boofcv.alg.misc.GImageMiscOps;
import boofcv.android.ConvertBitmap;
import boofcv.struct.image.ImageFloat32;

/**
 * Created by mehdibenchoufi on 27/08/15.
 */
public class WaveletDenoise extends BaseProcess {

    private ImageFloat32 imageFloat32;

    public WaveletDenoise(ImageView imageview) {
        super(imageview);
        imageBase = new BaseImage(ConvertBitmap.bitmapToGray(bitmap, (ImageFloat32) null, null));
        imageDataType = imageBase.getImage().getDataType();
        imageFloat32 = (ImageFloat32) imageBase.getImage();
    }

    private void denoising() {
        Random rand = new Random(234);
        ImageFloat32 input = imageFloat32.clone();

        GImageMiscOps.addGaussian(input, rand, 20, 0, 255);
        ImageFloat32 denoised = new ImageFloat32(input.width, input.height);
        // Levels in wavelet transform
        int numLevels = 4;
        // Noise removal algorithm
        WaveletDenoiseFilter<ImageFloat32> denoiser =
                FactoryImageDenoise.waveletBayes(ImageFloat32.class, numLevels, 0, 255);
        // remove noise from the image
        denoiser.process(input, denoised);
        imageBase.setImage(denoised);
    }

    public void denoise() {
        denoising();
        getBackBitmap();
    }
}
