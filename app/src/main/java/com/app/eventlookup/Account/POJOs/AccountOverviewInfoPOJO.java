package com.app.eventlookup.Account.POJOs;

public class AccountOverviewInfoPOJO {
    private String mEmail;
    private String mDisplayName;
    private String mBiography;
    private int mUserInterestedEventsCount;
    private int mUserGoingEventsCount;
    private int mUserCreatedEventsCount;
    private String mImagePath;

    public AccountOverviewInfoPOJO(String mEmail, String mDisplayName, String mBiography, int mUserInterestedEventsCount,
                                   int mUserGoingEventsCount, int mUserCreatedEventsCount, String mImagePath) {
        this.mEmail = mEmail;
        this.mDisplayName = mDisplayName;
        this.mBiography = mBiography;
        this.mUserInterestedEventsCount = mUserInterestedEventsCount;
        this.mUserGoingEventsCount = mUserGoingEventsCount;
        this.mUserCreatedEventsCount = mUserCreatedEventsCount;
        this.mImagePath = mImagePath;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getmBiography() {
        return mBiography;
    }

    public void setmBiography(String mBiography) {
        this.mBiography = mBiography;
    }

    public int getmUserInterestedEventsCount() {
        return mUserInterestedEventsCount;
    }

    public void setmUserInterestedEventsCount(int mUserInterestedEventsCount) {
        this.mUserInterestedEventsCount = mUserInterestedEventsCount;
    }

    public int getmUserGoingEventsCount() {
        return mUserGoingEventsCount;
    }

    public void setmUserGoingEventsCount(int mUserGoingEventsCount) {
        this.mUserGoingEventsCount = mUserGoingEventsCount;
    }

    public int getmUserCreatedEventsCount() {
        return mUserCreatedEventsCount;
    }

    public void setmUserCreatedEventsCount(int mUserCreatedEventsCount) {
        this.mUserCreatedEventsCount = mUserCreatedEventsCount;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }
}
