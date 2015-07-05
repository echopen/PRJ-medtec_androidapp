package com.echopen.asso.echopen.utils;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User benchoufi
 * Imported from the Donnfelker Android Bootstrap
 */
public class Constants {
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

    public static class Internationalization{
        public static final Locale locale_country = Locale.FRANCE;
    }
    public static class ParseConstants {
        private ParseConstants() {}
    }
}
