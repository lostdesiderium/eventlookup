package com.example.eventlookup.Account.POJOs;

public class AccountEventPOJO {

    private String mEventId;
    private String mImagePath;
    private String mTitle;
    private Boolean mInterested;
    private Boolean mGoing;

    public AccountEventPOJO(String mEventId, String mImagePath, String mTitle, Boolean mInterested, Boolean mGoing) {
        this.mEventId = mEventId;
        this.mImagePath = mImagePath;
        this.mTitle = mTitle;
        this.mInterested = mInterested;
        this.mGoing = mGoing;
    }

    public String getmEventId() {
        return mEventId;
    }

    public void setmEventId(String mEventId) {
        this.mEventId = mEventId;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Boolean getmInterested() {
        return mInterested;
    }

    public void setmInterested(Boolean mInterested) {
        this.mInterested = mInterested;
    }

    public Boolean getmGoing() {
        return mGoing;
    }

    public void setmGoing(Boolean mGoing) {
        this.mGoing = mGoing;
    }
}
