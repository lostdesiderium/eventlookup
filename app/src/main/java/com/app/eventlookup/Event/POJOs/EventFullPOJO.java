package com.app.eventlookup.Event.POJOs;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventFullPOJO {
    private final String TAG = "EVENT_FULL_POJO";

    private String Id;
    private String EventTitle;
    private String EventDescription;
    private String EventLocation;
    private String EventDate;
    private String EventEndDate;
    private String InterestedPeopleCount;
    private String GoingPeopleCount;
    private String DaysEventActive;
    private String Lat;
    private String Lng;
    private ArrayList<String> ListImageUrl;

    private final String DISPLAY_FORMAT = "MMM dd HH:mm yyyy";
    private final String PARSE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public EventFullPOJO(String id, String eventTitle, String eventDescription, String eventLocation, String eventDate, String eventFinishDate,
                         String interestedPeopleCount, String goingPeopleCount, String daysEventActive, String lat, String lng, ArrayList<String> listImageUrl) {
        Id = id;
        EventTitle = eventTitle;
        EventDescription = eventDescription;
        EventLocation = eventLocation;
        EventDate = eventDate;
        EventEndDate = eventFinishDate;
        InterestedPeopleCount = interestedPeopleCount;
        GoingPeopleCount = goingPeopleCount;
        DaysEventActive = daysEventActive;
        Lat = lat;
        Lng = lng;
        ListImageUrl = listImageUrl;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
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

    public String getInterestedPeopleCount() {
        return InterestedPeopleCount;
    }

    public void setInterestedPeopleCount(String interestedPeopleCount) {
        InterestedPeopleCount = interestedPeopleCount;
    }

    public String getGoingPeopleCount() {
        return GoingPeopleCount;
    }

    public void setGoingPeopleCount(String goingPeopleCount) {
        GoingPeopleCount = goingPeopleCount;
    }

    public String getDaysEventActive() {
        return DaysEventActive;
    }

    public void setDaysEventActive(String daysEventActive) {
        DaysEventActive = daysEventActive;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public ArrayList<String> getListImageUrl() {
        return ListImageUrl;
    }

    public void setListImageUrl(ArrayList<String> listImageUrl) {
        ListImageUrl = listImageUrl;
    }

    public String getEventEndDate() {
        return EventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        EventEndDate = eventEndDate;
    }
}
