package com.example.eventlookup.Event.POJOs;

import android.location.Location;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;

public class EventPOJO {

    private String Id;
    private String ImageURL; // private ArrayList<Base64> mImagesBitmaps; // Should be array list of images bitmaps
    private Base64 mImageUrl;
    private String EventTitle;
    private String EventShortDescription;
    private String EventDescription;
    private String EventLocation;
    private LatLng CoordinatesLatLng;
    private String EventDate;

    public EventPOJO(String imageURL, String eventTitle, String eventShortDescription, String eventDescription, String eventLocation, String eventDate) {
        ImageURL = imageURL;
        EventTitle = eventTitle;
        EventShortDescription = eventShortDescription;
        EventDescription = eventDescription;
        EventLocation = eventLocation;
        EventDate = eventDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Base64 getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(Base64 mImageUrl) {
        this.mImageUrl = mImageUrl;
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

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
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




}
