package com.setoalan.mylastfm;

public class Track {

    private String mArtist;
    private String mName;
    private String mAlbum;
    private String mUrl;
    private String mImage;
    private boolean mNowPlaying;
    private long mDate;


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

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String album) {
        mAlbum = album;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public boolean getNowPlaying() {
        return mNowPlaying;
    }

    public void setNowPlaying(boolean nowPlaying) {
        mNowPlaying = nowPlaying;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

}
