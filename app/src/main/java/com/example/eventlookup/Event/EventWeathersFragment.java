package com.example.eventlookup.Event;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eventlookup.Event.Adapters.ImageSliderPageAdapter;
import com.example.eventlookup.Event.POJOs.EventFullPOJO;
import com.example.eventlookup.Event.POJOs.EventWeatherPOJO;
import com.example.eventlookup.R;
import com.example.eventlookup.Shared.AppConf;
import com.example.eventlookup.Shared.CacheInterceptor;
import com.example.eventlookup.Shared.MainThreadOkHttpCallback;
import com.example.eventlookup.Shared.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventWeathersFragment extends Fragment {
    private final String TAG = "EventWeathersFragment";
    private final int MAX_DAYS_FORECAST = 16;

    private View mThisView;
    private LinearLayout linearLayoutParent;
    private FrameLayout progressBarHolder;
    private OkHttpClient okHttpClient;
    private ConstraintLayout notAvailableCL;
    private AlphaAnimation inAlphaAnimation;
    private AlphaAnimation outAlphaAnimation;

    private String _eventId;
    private Date mEventStartDate;
    private int mEventDaysActive;
    private String mEventCity;
    private double mEventLat;
    private double mEventLng;
    private ArrayList<EventWeatherPOJO> mWeatherForecasts;
    private Date currentDate = new Date();

    public EventWeathersFragment() {
        // Required empty public constructor
    }

    public EventWeathersFragment(String eventId){
        this._eventId = eventId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_event_weathers, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        getLayoutViews( view );
        prepareCommonVariables();

        progressBarHolder.setVisibility( View.VISIBLE );
        try{
            getEventDetailedInfo();
        }
        catch(Exception e){
            Log.e(TAG, e.toString());
        }

    }

    private void initActionsSequence(){
        boolean isForecastAvailable = checkIfForecastIsAvailable();
        if(isForecastAvailable){
            getWeathersForecast();
        }
        else{
            showForecastUnavailableLayout();
        }
    }

    private void getLayoutViews(View view){
        mThisView = view;
        progressBarHolder = view.findViewById( R.id.FL_PB_holder_events_list );
        linearLayoutParent = view.findViewById( R.id.LL_event_weathers );
    }

    private void prepareCommonVariables(){
        File httpCacheDirectory = new File(getContext().getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache( httpCacheDirectory, cacheSize );

        okHttpClient = new OkHttpClient.Builder(  ).addNetworkInterceptor( new CacheInterceptor() )
                .cache( cache )
                .build();
        mWeatherForecasts = new ArrayList<>();
    }

    private void getEventDetailedInfo() throws Exception {
        File httpCacheDirectory = new File(getContext().getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache( httpCacheDirectory, cacheSize );

        okHttpClient = new OkHttpClient.Builder(  ).addNetworkInterceptor( new CacheInterceptor() )
                .cache( cache )
                .build();

        AppConf apiConf = AppConf.getInstance();
        String eventInfoRoute = apiConf.getEventGetEventDetailedApiRoute() + _eventId;

        HttpUrl.Builder urlBuilder = HttpUrl.parse(eventInfoRoute)
                .newBuilder();

        String url = urlBuilder.build()
                .toString();

        final Request request = new Request.Builder(  )
                .url( url )
                .build();

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    Utils utils = new Utils();
                    JSONObject response = new JSONObject( body );

                    String startDate = response.getString( "StartDate" );
                    mEventStartDate = utils.getFormattedEventDateObject( startDate );
                    mEventDaysActive = Integer.parseInt( response.getString( "DaysEventActive" ) );

                    JSONObject address = response.getJSONObject( "Address" );
                    mEventCity = address.getString( "City" );
                    mEventLat = Double.parseDouble( address.getString( "Lat" ) );
                    mEventLng = Double.parseDouble( address.getString( "Lng" ));

                    initActionsSequence();

                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing api/event/{id} response data - " + e);
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "Api call http://<host>/api/event failed");
                progressBarHolder.setVisibility( View.GONE);
            }

        } );

    }

    private void getWeathersForecast(){
        AppConf apiConf = AppConf.getInstance();
        String coordinates = "?lat=" + mEventLat + "&lon=" + mEventLng;
        String apiKey = "&key=" + AppConf.WEATHERBIT_API_KEY;
        String weatherbitUrl = apiConf.getWEATHERBIT_GET_FORECAST_16() + coordinates + apiKey;

        HttpUrl.Builder urlBuilder = HttpUrl.parse(weatherbitUrl)
                .newBuilder();

        String url = urlBuilder.build()
                .toString();

        final Request request = new Request.Builder(  )
                .url( url )
                .build();

        okHttpClient.newCall(request).enqueue( new MainThreadOkHttpCallback() {

            @Override
            public void apiCallSuccess(String body){
                try{
                    JSONObject responseObj = new JSONObject( body );
                    JSONArray responseDataArray = responseObj.getJSONArray( "data" );

                    int offsetToData = getDayNumberOffsetToForecast();
                    int daysNumberToGet = getAvailableDaysNumberForForecast();
                    int limit = (offsetToData + 1) + daysNumberToGet;
                    for(int i = offsetToData + 1; i < limit && i < MAX_DAYS_FORECAST; i++){
                        JSONObject oneDayForecastObj = responseDataArray.getJSONObject( i );
                        double windSpeed = oneDayForecastObj.getDouble( "wind_spd" );
                        String windDirectionText = oneDayForecastObj.getString( "wind_cdir_full" );

                        JSONObject weatherDescriptionObj = oneDayForecastObj.getJSONObject( "weather" );
                        String iconCode = weatherDescriptionObj.getString( "icon" );
                        String description = weatherDescriptionObj.getString( "description" );

                        double maxTemp = oneDayForecastObj.getDouble( "max_temp" );
                        String forecastDate = oneDayForecastObj.getString( "datetime" );

                        EventWeatherPOJO eventDayForecast = new EventWeatherPOJO(
                                windSpeed,
                                windDirectionText,
                                iconCode,
                                description,
                                maxTemp,
                                forecastDate
                        );
                        mWeatherForecasts.add( eventDayForecast );
                    }

                    attachWeatherDataToLayout( mThisView );
                    progressBarHolder.setVisibility( View.GONE);
                }
                catch (JSONException e){
                    Log.e("OkHttp", "Error while parsing http://api.weatherbit.io/v2.0/forecast/daily?lat=?&lon=?&key=? response data - " + e);
                }
            }

            @Override
            public void apiCallFail(Exception e){
                Log.e("OkHttp", "http://api.weatherbit.io/v2.0/forecast/daily?lat=?&lon=?&key=? failed");
                progressBarHolder.setVisibility( View.GONE);
            }

        } );
    }

    private void attachWeatherDataToLayout(View view ) {
        Resources resources = getContext().getResources();
        String packageName = getContext().getPackageName();
        for (int i = 0; i < mWeatherForecasts.size(); i++) {

            EventWeatherPOJO eventDayForecast = mWeatherForecasts.get( i );
            View eventWeatherForecast = getLayoutInflater().inflate( R.layout.event_weather_row, (ViewGroup) view, false );

            ImageView iconIV = eventWeatherForecast.findViewById( R.id.IV_event_weather_day_forecast_icon );
            TextView eventForecastDateTV = eventWeatherForecast.findViewById( R.id.TV_event_weather_day_forecast_date );
            TextView eventForecastValueTV = eventWeatherForecast.findViewById( R.id.TV_event_weather_day_forecast_value );
            TextView eventForecastDescription = eventWeatherForecast.findViewById( R.id.TV_event_weather_day_description );
            TextView eventForecastWindDescription = eventWeatherForecast.findViewById( R.id.TV_event_weather_day_wind_description );

            String iconCode = eventDayForecast.getmIconCode();
            final int resourceId = resources.getIdentifier( iconCode, "drawable", packageName );
            Drawable actualIcon = resources.getDrawable( resourceId );
            iconIV.setImageDrawable( actualIcon );

            eventForecastDateTV.setText( eventDayForecast.getmDate() );

            String maxTemp = eventDayForecast.getmMaxTemp() + "° C";
            eventForecastValueTV.setText( maxTemp );

            eventForecastDescription.setText( eventDayForecast.getmWeatherDescription() );

            double windSpeed = (double) Math.round( eventDayForecast.getmWindSpeed() * 10 ) / 10;
            String windDescription = windSpeed + " m/s " + eventDayForecast.getmWindDirectionText() + " wind";
            eventForecastWindDescription.setText( windDescription );

            linearLayoutParent.addView( eventWeatherForecast );

        }
        progressBarHolder.setVisibility( View.GONE );
    }

    private boolean checkIfForecastIsAvailable(){
        long oneDay = new Utils().getOneDaySeconds();
        Date datePlusSixteenDays = new Date(currentDate.getTime() + (oneDay * (long)MAX_DAYS_FORECAST) ); // L means to operate with long type and not int
        if(mEventStartDate.before( currentDate ))
            return false;

        return mEventStartDate.before(datePlusSixteenDays);
    }

    private int getAvailableDaysNumberForForecast(){
        long oneDay = new Utils().getOneDaySeconds();
        Date datePlusSixteenDays = new Date(currentDate.getTime() + (oneDay * (long)MAX_DAYS_FORECAST) ); // long means to operate with long type and not int for accurate results

        if(mEventStartDate.getTime() + (oneDay * (long)mEventDaysActive) < datePlusSixteenDays.getTime()){
            return mEventDaysActive;
        }
        long differenceInMillis = Math.abs(datePlusSixteenDays.getTime() - mEventStartDate.getTime());
        long differenceInDays = TimeUnit.DAYS.convert( differenceInMillis, TimeUnit.MILLISECONDS );

        return Math.round(differenceInDays);
    }

    private int getDayNumberOffsetToForecast(){
        long differenceInMillis = Math.abs(mEventStartDate.getTime() - currentDate.getTime());
        long differenceInDays = TimeUnit.DAYS.convert( differenceInMillis, TimeUnit.MILLISECONDS );

        return Math.round(differenceInDays);
    }

    private void showForecastUnavailableLayout(){
        LayoutInflater inflater = getLayoutInflater();
        View notAvailableForecastCL = inflater.inflate(R.layout.forecast_not_available, (ViewGroup) mThisView, false );
        if(mEventStartDate.before( currentDate )){
            TextView notAvailableForecastTV = notAvailableForecastCL.findViewById( R.id.TV_event_forecast_not_available_description );
            notAvailableForecastTV.setText( R.string.event_finished_no_forecast );
        }

        linearLayoutParent.addView( notAvailableForecastCL );
        progressBarHolder.setVisibility( View.GONE );
    }

}
