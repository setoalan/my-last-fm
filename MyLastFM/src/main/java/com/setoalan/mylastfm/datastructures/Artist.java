package com.setoalan.mylastfm.datastructures;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

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
    private ArrayList<Track> mTracks = new ArrayList<Track>();
    private ArrayList<Album> mAlbums = new ArrayList<Album>();
    private ArrayList<Artist> mSimilar = new ArrayList<Artist>();

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

    public ArrayList<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        mTracks = tracks;
    }

    public ArrayList<Album> getAlbums() {
        return mAlbums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        mAlbums = albums;
    }

    public ArrayList<Artist> getSimilar() {
        return mSimilar;
    }

    public void setSimilar(ArrayList<Artist> similar) {
        mSimilar = similar;
    }

}
