package com.example.eventlookup.Shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private final String TAG = "EventLookup.Utils";

    private final String DISPLAY_FORMAT = "MMM dd HH:mm yyyy";
    private final String PARSE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public Utils(){}

    public  String getFormattedEventDateString(String date){
        SimpleDateFormat formatter = new SimpleDateFormat( PARSE_FORMAT, Locale.getDefault() );

        try{
            Date formattedDate = formatter.parse( date );
            if(formattedDate != null)
                return new SimpleDateFormat( DISPLAY_FORMAT, Locale.getDefault() ).format( formattedDate );
        }
        catch(ParseException e){
            Log.e(TAG, e.toString());
        }

        return "1970-01-01";
    }

    public Date getFormattedEventDateObject(String date){
        SimpleDateFormat formatter = new SimpleDateFormat( PARSE_FORMAT, Locale.getDefault() );
        Date formattedDate = new Date();
        try{
            formattedDate = formatter.parse( date );
        }
        catch(ParseException e){
            Log.e(TAG, e.toString());
        }
        return formattedDate;
    }

    public long getOneDaySeconds(){
        return 1000 * 60 * 60 *  24;
    }

    public SharedPreferences getAppSharedPreferences(Context context){
        return context.getSharedPreferences( AppConf.APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE );
    }

    public String getAppToken(Context context){
        return getAppSharedPreferences( context ).getString( AppConf.TOKEN_KEY, "" );
    }

    public void writeAppTokenToSharedPreferences(Context context, String token){
        SharedPreferences prefs = getAppSharedPreferences( context );
        prefs.edit().putString( AppConf.TOKEN_KEY, token).apply();
    }
}
