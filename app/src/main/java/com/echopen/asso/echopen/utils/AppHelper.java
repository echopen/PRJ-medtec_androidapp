package com.echopen.asso.echopen.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.echopen.asso.echopen.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mehdibenchoufi on 08/07/15.
 */
public class AppHelper {

    public static Constants.Settings setting;

    public static Uri getFileUri(Activity activity, int mediaType) {
        if (isExternalStorageAvailable()) {
            String appName = activity.getString(R.string.app_name);
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    appName);
            if (! mediaStorageDir.exists()) {
                if (! mediaStorageDir.mkdirs()) {
                    Log.e("TAGGY", "Failed to create directory.");
                    return null;
                }
            }
            File mediaFile;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Constants.Internationalization.locale_country).format(now);
            String path = mediaStorageDir.getPath() + File.separator;
            if (mediaType == setting.MEDIA_TYPE_IMAGE) {
                // @todo : validate .jpg extension
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            }
            else if (mediaType == setting.MEDIA_TYPE_VIDEO) {
                mediaFile = new File(path + "VID_" + timestamp + ".mp4");
            }
            else {
                return null;
            }
            Log.d("TAGGY", "File: " + Uri.fromFile(mediaFile));
            return Uri.fromFile(mediaFile);
        }
        else
            return null;
    }

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) { return true; }
        else { return false; }
    }

    public static void getErrorStorageMessage(Activity activity) {
        Toast.makeText(activity, R.string.external_storage_error,
                Toast.LENGTH_LONG).show();
    }
}
