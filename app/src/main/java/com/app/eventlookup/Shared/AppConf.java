package com.app.eventlookup.Shared;

public class AppConf {

    // Permissions
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5000;
    public static final int PERMISSIONS_REQUEST_NETWORK = 5001;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 5002;
    public static final int ERROR_DIALOG_REQUEST = 5003;

    // Libs constants
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final String GOOGLE_MAPS_API_KEY = "AIzaSyBR0fP8lskDHUQEriVjzeGTG79mjswtWfE";
    public static final String WEATHERBIT_API_KEY = "0ef36a384ba0410b8cc6d23589a40bbc";

    // API URL base for EMULATOR
    private static final String API_SCHEME = "http://";
    private static final String API_BASE = "10.0.2.2:19422/api/";

    // API URL base for server
//    private static final String API_SCHEME = "https://";
//    private static final String API_BASE = "46f32164.ngrok.io/api/";

    // API Routes
    private final String HOST_URL = API_SCHEME + API_BASE;
    private final String EVENT_GET_LIST_API_ROUTE = HOST_URL + "event";
    private final String EVENT_GET_EVENT_DETAILED_API_ROUTE = HOST_URL + "event/";
    private final String ACCOUNT_LOGIN_API_ROUTE = HOST_URL + "users/login";
    private final String ACCOUNT_TOKEN_LOGIN_API_ROUTE = HOST_URL + "users/token-login";
    private final String ACCOUNT_REGISTER_API_ROUTE = HOST_URL + "users/register";
    private final String ACCOUNT_GET_USER_API_ROUTE = HOST_URL + "users";
    private final String ACCOUNT_UPDATE_USER_API_ROUTE = HOST_URL + "users/update";
    private final String ACCOUNT_REMOVE_USER_EVENT_API_ROUTE = HOST_URL + "users/remove-event";
    private final String ACCOUNT_UPLOAD_IMAGE_API_ROUTE = HOST_URL + "users/upload-image";
    private final String EVENT_MARK_USER_EVENT = HOST_URL + "users/mark-user-event";
    private final String ACCOUNT_CHANGE_PASSWORD = HOST_URL + "users/change-password";

    private final String WEATHERBIT_GET_FORECAST_16 = API_SCHEME + "api.weatherbit.io/v2.0/forecast/daily";

    // Header
    public static final String JsonMediaTypeString = "application/json; charset=utf-8";

    // App's filenames constants
    public static final String APP_SHARED_PREFERENCES_NAME = "com.app.eventlookup.access";
    public static final String TOKEN_KEY = "app_token";
    public static final String USER_ID = "user_id";

    // Singleton class
    private static AppConf appConfInstance = null;

    // Private constructor
    private AppConf(){
    }

    public static AppConf getInstance(){

        if(appConfInstance == null){
            appConfInstance = new AppConf();
        }

        return appConfInstance;
    }

    public String getEventGetListApiRoute() {
        return EVENT_GET_LIST_API_ROUTE;
    }

    public String getEventGetEventDetailedApiRoute() {
        return EVENT_GET_EVENT_DETAILED_API_ROUTE;
    }

    public String getWEATHERBIT_GET_FORECAST_16() {
        return WEATHERBIT_GET_FORECAST_16;
    }

    public String getACCOUNT_LOGIN_API_ROUTE() { return ACCOUNT_LOGIN_API_ROUTE; }

    public String getACCOUNT_TOKEN_LOGIN_API_ROUTE() { return ACCOUNT_TOKEN_LOGIN_API_ROUTE; }

    public String getACCOUNT_REGISTER_API_ROUTE() { return ACCOUNT_REGISTER_API_ROUTE; }

    public String getACCOUNT_GET_USER_API_ROUTE() { return ACCOUNT_GET_USER_API_ROUTE; }

    public String getACCOUNT_UPDATE_USER_API_ROUTE() { return ACCOUNT_UPDATE_USER_API_ROUTE; }

    public String getACCOUNT_REMOVE_USER_EVENT_API_ROUTE() { return ACCOUNT_REMOVE_USER_EVENT_API_ROUTE; }

    public String getACCOUNT_UPLOAD_IMAGE_API_ROUTE() { return ACCOUNT_UPLOAD_IMAGE_API_ROUTE; }

    public String getEVENT_MARK_USER_EVENT() { return EVENT_MARK_USER_EVENT; }

    public String getACCOUNT_CHANGE_PASSWORD() { return ACCOUNT_CHANGE_PASSWORD; }
}
