package com.setoalan.mylastfm.datastructures;

import android.graphics.drawable.Drawable;

public class Artist {

    private int mRank;
    private String mName;
    private int mPlayCount;
    private String mUrl;
    private Drawable mImage;
    private Drawable mLargeImage;
    private int mListeners;
    private int mPlays;
    private String mSummary;

    public int getRank() {
        return mRank;
    }

    public void setRank(int rank) {
        mRank = rank;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPlayCount() {
        return mPlayCount;
    }

    public void setPlayCount(int playCount) {
        mPlayCount = playCount;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable image) {
        mImage = image;
    }

    public Drawable getLargeImage() {
        return mLargeImage;
    }

    public void setLargeImage(Drawable largeImage) {
        mLargeImage = largeImage;
    }

    public int getListeners() {
        return mListeners;
    }

    public void setListeners(int listeners) {
        mListeners = listeners;
    }

    public int getPlays() {
        return mPlays;
    }

    public void setPlays(int plays) {
        mPlays = plays;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

}
