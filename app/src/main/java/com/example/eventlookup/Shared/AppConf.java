package com.example.eventlookup.Shared;

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

    // API base's
    private static final String API_SCHEME = "http://";
    private static final String API_BASE = "10.0.2.2:19422/api/";

    // API Routes
    private final String EVENT_GET_LIST_API_ROUTE =  API_SCHEME +  API_BASE + "event";
    private final String EVENT_GET_EVENT_DETAILED_API_ROUTE =  API_SCHEME +  API_BASE + "event/";
    private final String WEATHERBIT_GET_FORECAST_16 = API_SCHEME + "api.weatherbit.io/v2.0/forecast/daily";

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
}
