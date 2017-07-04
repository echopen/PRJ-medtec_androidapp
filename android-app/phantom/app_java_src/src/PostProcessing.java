/** 
 *this example has been identically taken from the BoofCV wiki example http://boofcv.org/index.php?title=Example_Wavelet_Noise_Removal 
 */

import boofcv.abst.denoise.FactoryImageDenoise;
import boofcv.abst.denoise.WaveletDenoiseFilter;
import boofcv.alg.misc.GImageMiscOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.ListDisplayPanel;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import boofcv.io.UtilIO;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.ImageFloat32;

import java.io.File;
import java.util.Random;

/**
 * Created by mehdibenchoufi on 03/02/16.
 */
public class PostProcessing {

    public static void main( String args[] ) {

        // load the input image, declare data structures, create a noisy image
        Random rand = new Random(234);
        String dir = new File(".").getAbsolutePath();
        File file = new File("/Users/mehdibenchoufi/Downloads/Example_lena_denoise_noisy.jpg");

        ImageFloat32 input = UtilImageIO.loadImage(file.getAbsolutePath(), ImageFloat32.class);
        ImageFloat32 noisy = input.clone();
        GImageMiscOps.addGaussian(noisy, rand, 20, 0, 255);
        ImageFloat32 denoised = new ImageFloat32(input.width, input.height);

        // How many levels in wavelet transform
        int numLevels = 4;
        // Create the noise removal algorithm
        WaveletDenoiseFilter<ImageFloat32> denoiser =
                FactoryImageDenoise.waveletBayes(ImageFloat32.class, numLevels, 0, 255);

        // remove noise from the image
        denoiser.process(noisy,denoised);

        // display the results
        ListDisplayPanel gui = new ListDisplayPanel();
        gui.addImage(ConvertBufferedImage.convertTo(input, null),"Input");
        gui.addImage(ConvertBufferedImage.convertTo(noisy,null),"Noisy");
        gui.addImage(ConvertBufferedImage.convertTo(denoised,null),"Denoised");

        ShowImages.showWindow(input, "With  noise", true);
        ShowImages.showWindow(denoised, "Without noise", true);
    }
}
