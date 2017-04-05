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

        public static final String REDPITAYA_UDP_IP = "192.168.1.12";

        public static final int REDPITAYA_UDP_PORT = 7536;
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
         largest number of pixels in z-direction -
         number of weight coefficients
         **/
        /* speed of sound in m.s^{—1} */
        public static final int SPEED_OF_SOUND = 1540;

        /* sampling frequency in Hz */
        public static final int SAMPLING_FREQUENCY = (int) 125 /32 * (int) Math.pow(10,6);

        public static final int N_x = 512;

        public static final int N_z = 512;

        public static final int NUM_LINES = 120;

        public static final float IMAGE_SIZE = (float) 0.201;

        /* image width in rad */
        public static final double IMAGE_WIDTH = 0.5 * Math.PI;

        public static final int NUM_SAMPLES = 511;

        public static final int NUM_IMG_DATA = 120;

        public static final double RADIAL_IMG_INIT = 0.013;

        /* depth for start of data in m (meters) */
        public static final double RADIAL_DATA_INIT = SPEED_OF_SOUND/2*1/(double) SAMPLING_FREQUENCY;

        public static final int ANGLE_INIT = 0;

        /* no dimension param */
        public static final float STEP_ANGLE_INIT = (float) (IMAGE_WIDTH/NUM_LINES);

        /* sampling interval for data in m (meters)*/
        public static final double STEP_RADIAL_INIT = SPEED_OF_SOUND/2*1/(double) SAMPLING_FREQUENCY;

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
            float[] float_constants = new float[7];
            float_constants[0] = (float) PreProcParam.RADIAL_IMG_INIT;
            float_constants[1] = PreProcParam.IMAGE_SIZE;
            float_constants[2] = (float) PreProcParam.STEP_RADIAL_INIT;
            float_constants[3] = (float) (PreProcParam.RADIAL_DATA_INIT);
            float_constants[4] = (float)  Math.floor(PreProcParam.NUM_SAMPLES);
            float_constants[5] =  Constants.PreProcParam.NUM_LINES/ 2 * PreProcParam.STEP_ANGLE_INIT;
            float_constants[6] = - PreProcParam.STEP_ANGLE_INIT;
            return float_constants;
        }

        public static double[] getLoadDoubleConstants() {
            double[] double_constants = new double[7];
            double_constants[0] = PreProcParam.RADIAL_IMG_INIT;
            double_constants[1] = PreProcParam.IMAGE_SIZE;
            double_constants[2] = PreProcParam.STEP_RADIAL_INIT;
            double_constants[3] = PreProcParam.RADIAL_DATA_INIT;
            double_constants[4] =  Math.floor(PreProcParam.NUM_SAMPLES);
            double_constants[5] =  Constants.PreProcParam.NUM_LINES/ 2 * PreProcParam.STEP_ANGLE_INIT;
            double_constants[6] = - PreProcParam.STEP_ANGLE_INIT;
            return double_constants;
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
