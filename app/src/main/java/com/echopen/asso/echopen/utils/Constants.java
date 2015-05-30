package com.echopen.asso.echopen.utils;

/**
 * Created with IntelliJ IDEA.
 * User benchoufi
 * Imported from the Donnfelker Android Bootstrap
 */
public class Constants {
    public static class Auth {
        private Auth() {}

        /**
         * Account type id
         */
        public static final String ECHOPEN_ACCOUNT_TYPE = "com.androidECHOPEN";

        /**
         * Account name
         */
        public static final String ECHOPEN_ACCOUNT_NAME = "Android ECHOPEN";

        /**
         * Provider id
         */
        public static final String ECHOPEN_PROVIDER_AUTHORITY = "com.androidECHOPEN.sync";

        /**
         * Auth token type
         */
        public static final String AUTHTOKEN_TYPE = ECHOPEN_ACCOUNT_TYPE;
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for ECHOPEN!
     */
    public static class Http {
        private Http() {}



        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://api.parse.com";

        /**
         * Authentication URL
         */
        public static final String URL_AUTH = URL_BASE + "/1/login";

        /**
         * List Laws URL
         */
        public static final String URL_VOTEIT = "https://salty-beach-7476.herokuapp.com/users";

        public static final String URL_LAWS = "https://salty-beach-7476.herokuapp.com/laws.json";

        public static final String URL_FEED_LAWS = "https://salty-beach-7476.herokuapp.com/feeds.json";

        public static final String PARSE_APP_ID = "zHb2bVia6kgilYRWWdmTiEJooYA17NnkBSUVsr4H";

        public static final String PARSE_REST_API_KEY = "N2kCY1T3t3Jfhf9zpJ5MCURn3b25UpACILhnf5u9";

        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";

        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";

        public static final String CONTENT_TYPE_JSON = "application/json";

    }

    /**
     * Here are the Parse constants !
     */
    public static class ParseConstants {
        private ParseConstants() {}

        public static final String KEY_USERNAME = "username";
        public static final String KEY_FRIENDS_RELATION = "friendsRelation";
    }

}
