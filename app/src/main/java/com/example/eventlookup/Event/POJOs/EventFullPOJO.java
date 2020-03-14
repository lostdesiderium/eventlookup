package com.example.eventlookup.Event.POJOs;

import java.util.ArrayList;

public class EventFullPOJO {

    private String Id;
    private String EventTitle;
    private String EventDescription;
    private String EventLocation;
    private String EventDate;
    private String InterestedPeopleCount;
    private String GoingPeopleCount;
    private String DaysEventActive;
    private String Lat;
    private String Lng;
    private ArrayList<String> ListImageUrl;

    public EventFullPOJO(String id, String eventTitle, String eventDescription, String eventLocation, String eventDate,
                         String interestedPeopleCount, String goingPeopleCount, String daysEventActive, String lat, String lng, ArrayList<String> listImageUrl) {
        Id = id;
        EventTitle = eventTitle;
        EventDescription = eventDescription;
        EventLocation = eventLocation;
        EventDate = eventDate;
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
}
