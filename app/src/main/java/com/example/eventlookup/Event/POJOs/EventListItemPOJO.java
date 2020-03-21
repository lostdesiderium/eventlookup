package com.example.eventlookup.Event.POJOs;

import android.location.Location;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class EventListItemPOJO {

    private final String TAG = "EVENT_LIST_ITEM_POJO";

    private String Id;
    private String ImageURL; // private ArrayList<Base64> mImagesBitmaps; // Should be array list of images bitmaps
    private String EventTitle;
    private String EventShortDescription;
    private String EventLocation;
    private LatLng CoordinatesLatLng;
    private String EventDate;
    private String FormattedEventDate;
    private Date EventDateClassDate;

    private final String DISPLAY_FORMAT = "MMM dd HH:mm yyyy";
    private final String PARSE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public EventListItemPOJO(String id, String imageURL, String eventTitle, String eventShortDescription, String eventLocation, String eventDate) {
        this.Id = id;
        this.ImageURL = imageURL;
        this.EventTitle = eventTitle;
        this.EventShortDescription = eventShortDescription;
        this.EventLocation = eventLocation;
        this.EventDate = eventDate;
        this.FormattedEventDate = getFormattedEventDateString( eventDate );
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public LatLng getCoordinatesLatLng() {
        return CoordinatesLatLng;
    }

    public void setCoordinatesLatLng(LatLng coordinatesLatLng) {
        CoordinatesLatLng = coordinatesLatLng;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }

    public String getEventShortDescription() {
        return EventShortDescription;
    }

    public void setEventShortDescription(String eventShortDescription) {
        EventShortDescription = eventShortDescription;
    }

    public String getEventLocation() {
        return EventLocation;
    }

    public void setEventLocation(String eventLocation) {
        EventLocation = eventLocation;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public  String getFormattedEventDateString(String date){
        SimpleDateFormat formatter = new SimpleDateFormat( PARSE_FORMAT );

        try{
            Date formattedDate = formatter.parse( date );
            EventDateClassDate = formattedDate;
            String reformattedDate = new SimpleDateFormat( DISPLAY_FORMAT ).format( formattedDate );
            return reformattedDate;
        }
        catch(ParseException e){
            Log.e(TAG, e.toString());
        }

        return "";
    }

    public Date getFormattedEventDateObject(String date){
        SimpleDateFormat formatter = new SimpleDateFormat( PARSE_FORMAT );
        Date formattedDate = new Date();
        try{
            formattedDate = formatter.parse( date );
        }
        catch(ParseException e){
            Log.e(TAG, e.toString());
        }
        return formattedDate;
    }

    public String getFormattedEventDate() {
        return FormattedEventDate;
    }

}
