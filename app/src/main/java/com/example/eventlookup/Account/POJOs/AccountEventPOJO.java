package com.example.eventlookup.Account.POJOs;

public class AccountEventPOJO {

    private String mEventId;
    private String mImagePath;
    private String mTitle;

    public AccountEventPOJO(String mEventId, String mImagePath, String mTitle) {
        this.mEventId = mEventId;
        this.mImagePath = mImagePath;
        this.mTitle = mTitle;
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
}
