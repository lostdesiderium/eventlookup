package com.example.eventlookup.Event.POJOs;

import android.location.Location;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;

public class EventPOJO {

    private String Id;
    private String ImageURL; // private ArrayList<Base64> mImagesBitmaps; // Should be array list of images bitmaps
    private String EventTitle;
    private String EventShortDescription;
    private String EventLocation;
    private LatLng CoordinatesLatLng;
    private String EventDate;

    public EventPOJO(String id, String imageURL, String eventTitle, String eventShortDescription, String eventLocation, String eventDate) {
        this.Id = id;
        this.ImageURL = imageURL;
        this.EventTitle = eventTitle;
        this.EventShortDescription = eventShortDescription;
        this.EventLocation = eventLocation;
        this.EventDate = eventDate;
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




}
