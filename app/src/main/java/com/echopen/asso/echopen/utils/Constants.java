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
        public static final int N_x = 1024;

        public static final int N_y = 1024;

        public static final int NUM_LINES = 128;

        public static final int NUM_SAMPLES = 128;

        public static final int NUM_IMG_DATA = 128;

        public static final int RADIAL_IMG_INIT = 0;

        public static final int RADIAL_DATA_INIT = 0;

        public static final int ANGLE_INIT = 0;

        public static final int STEP_ANGLE_INIT = 0;

        public static final int STEP_RADIAL_INIT = 0;

        public static final int IMAGE_SIZE = 0;

        public static final int SCALE_FACTOR = 0;
    }

    public static class Internationalization{
        public static final Locale locale_country = Locale.FRANCE;
    }

    public static class ParseConstants {
        private ParseConstants() {}
    }
}
