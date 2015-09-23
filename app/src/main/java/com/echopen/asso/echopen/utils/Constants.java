package com.echopen.asso.echopen.utils;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User benchoufi
 * Imported from the Donnfelker Android Bootstrap
 */
public class Constants {

    /* Some systematic constants - To be used later if we need login system */
    public static class Auth {
        private Auth() {}

        public static final String ECHOPEN_ACCOUNT_TYPE = "com.androidECHOPEN";

        public static final String ECHOPEN_ACCOUNT_NAME = "Android ECHOPEN";

        public static final String ECHOPEN_PROVIDER_AUTHORITY = "com.androidECHOPEN.sync";

        public static final String AUTHTOKEN_TYPE = ECHOPEN_ACCOUNT_TYPE;
    }

    public static class Http {
        private Http() {}

        public static final String URL_BASE = "https://api.parse.com";

        public static final String URL_AUTH = URL_BASE + "/1/login";

        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";

        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";

        public static final String PARSE_APP_ID = "";

        public static final String PARSE_REST_API_KEY = "";

        public static final String CONTENT_TYPE_JSON = "application/json";
    }

    public static class Settings{

        public static final int SWIPE_MIN_DISTANCE = 120;

        public static final int SWIPE_MAX_OFF_PATH = 250;

        public static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public static final int DISPLAY_VIDEO = 2;

        public static final int DISPLAY_FILTER = 1;

        public static final int DISPLAY_PHOTO = 0;

        public static final int TAKE_PHOTO = 0;

        public static final int TAKE_VIDEO_REQUEST = 1;

        public static final int PICK_PHOTO_REQUEST = 2;

        public static final int PICK_VIDEO_REQUEST = 3;

        public static final int MEDIA_TYPE_IMAGE = 4;

        public static final int MEDIA_TYPE_VIDEO = 5;

        public static final int FILE_SIZE_LIMIT = 1024*1024*10;
    }

    public static class PreProcParam{
        /** todo define the pre-processing params -
        largest number of pixels in x-direction -
        largest number of pixels in y-direction -
         number of weight coefficients
        **/
        public static final int SPEED_OF_SOUND = 1540;

        public static final int SAMPLING_FREQUENCY = (int) Math.pow(100,5);

        public static final int N_x = 512;

        public static final int N_z = 512;

        public static final int NUM_LINES = 128;

        public static final float IMAGE_SIZE = (float) 0.105;

        public static final int IMAGE_WIDTH = (int) Math.round((90/180*Math.PI));

        public static final int NUM_SAMPLES = 1672;

        public static final int NUM_IMG_DATA = 128;

        public static final double RADIAL_IMG_INIT = 0.02;

        public static final int RADIAL_DATA_INIT = (int) SPEED_OF_SOUND/2*1/SAMPLING_FREQUENCY;

        public static final int ANGLE_INIT = 0;

        public static final float STEP_ANGLE_INIT = (int) Math.round(IMAGE_WIDTH/NUM_LINES);

        public static final int STEP_RADIAL_INIT = SPEED_OF_SOUND/2*1/SAMPLING_FREQUENCY;

        public static final int SCALE_FACTOR = 1;

        public static int[] getLoadIntegerConstants() {
            int[] int_constants = new int[4];
            int_constants[0] = PreProcParam.NUM_LINES;
            int_constants[1] = PreProcParam.SCALE_FACTOR;
            int_constants[2] = PreProcParam.N_z;
            int_constants[3] = PreProcParam.N_x;
            return int_constants;
        }

        public static float[] getLoadFloatConstants() {
            float[] float_constants = new float[8];
            float_constants[0] = (float) PreProcParam.RADIAL_IMG_INIT;
            float_constants[1] = PreProcParam.IMAGE_SIZE;
            float_constants[2] = PreProcParam.STEP_RADIAL_INIT;
            float_constants[3] = (float) (PreProcParam.RADIAL_DATA_INIT * Math.floor(PreProcParam.NUM_SAMPLES / 1024));
            float_constants[4] = (float)  Math.floor(PreProcParam.NUM_SAMPLES / 1024);
            float_constants[5] = PreProcParam.NUM_LINES/ 2 *float_constants[0];
            float_constants[6] = - PreProcParam.STEP_ANGLE_INIT;
            float_constants[7] = PreProcParam.NUM_LINES;

            return float_constants;
        }
    }

    public static class JNI_SETTINGS{
        public static final String LOCAL_MODULE = "com.echopen.asso.echopen";
    }

    public static class Internationalization{
        public static final Locale locale_country = Locale.FRANCE;
    }

    public static class ParseConstants {
        private ParseConstants() {}
    }
}
