package com.echopen.asso.echopen;

import android.app.Application;
import android.util.Log;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.ui.RenderingContextController;

import java.io.File;

/**
 * Created by lecoucl on 05/06/17.
 *
 * @class echOpen application
 */
public class EchOpenApplication extends Application {

    private final String TAG = this.getClass().getSimpleName();

    private EchographyImageStreamingService mEchographyImageStreaming; /* image streaming service */

    @Override
    public void onCreate() {
        super.onCreate();

        mEchographyImageStreaming = new EchographyImageStreamingService(new RenderingContextController());

          String[] Files =  this.fileList();


   /**     for(String file : Files){
             File test =new File(this.getFilesDir()+"/"+file+"/"+);
            Log.d("Fichier Savedddddd","fichier"+);
        }
    */
    }

    /**
     * @return image streaming service
     * @brief getter - image streaming service
     */
    public EchographyImageStreamingService getEchographyImageStreamingService() {
        return mEchographyImageStreaming;
    }
}
