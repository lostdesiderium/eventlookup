package com.app.eventlookup.Event.POJOs;

public class EventWeatherPOJO {

    private double mWindSpeed;
    private String mWindDirectionText;
    private String mIconCode;
    private String mWeatherDescription;
    private double mMaxTemp;
    private String mDate;

    public EventWeatherPOJO(){}

    public EventWeatherPOJO(double mWindSpeed, String mWindDirectionText, String mIconCode, String mWeatherDescription, double mMaxTemp, String mDate) {
        this.mWindSpeed = mWindSpeed;
        this.mWindDirectionText = mWindDirectionText;
        this.mIconCode = mIconCode;
        this.mWeatherDescription = mWeatherDescription;
        this.mMaxTemp = mMaxTemp;
        this.mDate = mDate;
    }

    public double getmWindSpeed() {
        return mWindSpeed;
    }

    public void setmWindSpeed(double mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public String getmWindDirectionText() {
        return mWindDirectionText;
    }

    public void setmWindDirectionText(String mWindDirectionText) {
        this.mWindDirectionText = mWindDirectionText;
    }

    public String getmIconCode() {
        return mIconCode;
    }

    public void setmIconCode(String mIconCode) {
        this.mIconCode = mIconCode;
    }

    public String getmWeatherDescription() {
        return mWeatherDescription;
    }

    public void setmWeatherDescription(String mWeatherDescription) {
        this.mWeatherDescription = mWeatherDescription;
    }

    public double getmMaxTemp() {
        return mMaxTemp;
    }

    public void setmMaxTemp(double mMaxTemp) {
        this.mMaxTemp = mMaxTemp;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
