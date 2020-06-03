package com.app.eventlookup.Event.POJOs;

public class UserEventPOJO {
    private boolean Going;
    private boolean Interested;
    private boolean Created;
    private String EventId;
    private String UserId;

    public UserEventPOJO(boolean going, boolean interested, boolean created, String eventId, String userId) {
        Going = going;
        Interested = interested;
        Created = created;
        EventId = eventId;
        UserId = userId;
    }

    public boolean isGoing() {
        return Going;
    }

    public void setGoing(boolean going) {
        Going = going;
    }

    public boolean isInterested() {
        return Interested;
    }

    public void setInterested(boolean interested) {
        Interested = interested;
    }

    public boolean isCreated() {
        return Created;
    }

    public void setCreated(boolean created) {
        Created = created;
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
