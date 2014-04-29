package com.setoalan.mylastfm.datastructures;


import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Date;

public class Album {

    private int mRank;
    private String mArtist;
    private String mName;
    private int mPlayCount;
    private String mUrl;
    private Drawable mImage;
    private Drawable mLargeImage;
    private Date mReleaseDate;
    private int mListeners;
    private int mPlays;
    private ArrayList<Track> mTracks = new ArrayList<Track>();

    public int getRank() {
        return mRank;
    }

    public void setRank(int rank) {
        mRank = rank;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
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

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        mReleaseDate = releaseDate;
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

    public ArrayList<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        mTracks = tracks;
    }

}
