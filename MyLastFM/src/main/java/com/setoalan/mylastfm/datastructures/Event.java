package com.setoalan.mylastfm.datastructures;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {

    private String mTitle;
    private String mHeadliner;
    private List<String> mArtists = new ArrayList<String>();
    private ArrayList<Drawable> mArtistsImages = new ArrayList<Drawable>();
    private String mVenue;
    private String mCity;
    private String mCountry;
    private String mStreet;
    private int mPostalCode;
    private String mLatitude;
    private String mLongitude;
    private String mWebsite;
    private String mPhoneNumber;
    private Date mStartDate;
    private String mDescription;
    private Drawable mImage;
    private String mUrl;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getHeadliner() {
        return mHeadliner;
    }

    public void setHeadliner(String headliner) {
        mHeadliner = headliner;
    }

    public List<String> getArtists() {
        return mArtists;
    }

    public void setArtists(List<String> artists) {
        mArtists = artists;
    }

    public ArrayList<Drawable> getArtistsImages() {
        return mArtistsImages;
    }

    public void setArtistsImages(ArrayList<Drawable> artistsImages) {
        mArtistsImages = artistsImages;
    }

    public String getVenue() {
        return mVenue;
    }

    public void setVenue(String venue) {
        mVenue = venue;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        mStreet = street;
    }

    public int getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(int postalCode) {
        mPostalCode = postalCode;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable image) {
        mImage = image;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
