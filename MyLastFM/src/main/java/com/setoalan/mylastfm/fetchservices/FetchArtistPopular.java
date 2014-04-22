package com.setoalan.mylastfm.fetchservices;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.setoalan.mylastfm.ArtistFragment;
import com.setoalan.mylastfm.datastructures.Album;
import com.setoalan.mylastfm.datastructures.Track;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchArtistPopular {

    private static final String LASTFM_URL = "http://ws.audioscrobbler.com/2.0/?";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    InputStream mInputStream;
    Drawable mDrawable;

    public void fetchArtistPopular()  {
        String url = Uri.parse(LASTFM_URL).buildUpon()
                .appendQueryParameter("method", "artist.gettoptracks")
                .appendQueryParameter("artist", ArtistFragment.mArtist.getName())
                .appendQueryParameter("api_key", KEY)
                .appendQueryParameter("limit", 5 + "")
                .appendQueryParameter("format", "json")
                .build().toString();

        String result = null;

        try {
            final HttpClient httpclient = new DefaultHttpClient();
            final HttpUriRequest request = new HttpGet(url);
            final HttpResponse response = httpclient.execute(request);
            final StatusLine status = response.getStatusLine();

            if (status.getStatusCode() == HttpStatus.SC_OK) {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();

                try {
                    response.getEntity().writeTo(out);
                    result = out.toString();
                } finally {
                    out.close();
                }
            } else {
                response.getEntity().getContent().close();
                throw new IOException(status.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        deserialize(result);

        return;
    }

    private void deserialize(String result) {
        Track track;

        try {
            JSONObject jsonObjectMain = new JSONObject(result);
            JSONArray jsonArray = jsonObjectMain.getJSONObject("toptracks").getJSONArray("track");
            JSONObject jsonObject;

            for (int i=0; i<5; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                track = new Track();
                ArtistFragment.mArtist.getTracks().add(track);
                ArtistFragment.mArtist.getTracks().get(i).setRank(jsonObject.getJSONObject("@attr").getInt("rank"));
                ArtistFragment.mArtist.getTracks().get(i).setArtist(jsonObject.getJSONObject("artist").getString("name"));
                ArtistFragment.mArtist.getTracks().get(i).setName(jsonObject.getString("name"));
                if (!jsonObject.getString("duration").equals(""))
                    ArtistFragment.mArtist.getTracks().get(i).setDuration(jsonObject.getInt("duration"));
                ArtistFragment.mArtist.getTracks().get(i).setPlayCount(jsonObject.getInt("playcount"));
                ArtistFragment.mArtist.getTracks().get(i).setUrl(jsonObject.getString("url"));
                if (jsonObject.optJSONArray("image") != null) {
                    mInputStream = (InputStream) new URL(jsonObject.getJSONArray("image")
                            .getJSONObject(2).getString("#text")).getContent();
                    mDrawable = Drawable.createFromStream(mInputStream, "src name");
                    ArtistFragment.mArtist.getTracks().get(i).setImage(mDrawable);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fetchArtistPopular2();

        return;
    }

    private void fetchArtistPopular2() {
        String url = Uri.parse(LASTFM_URL).buildUpon()
                .appendQueryParameter("method", "artist.gettopalbums")
                .appendQueryParameter("artist", ArtistFragment.mArtist.getName())
                .appendQueryParameter("api_key", KEY)
                .appendQueryParameter("limit", 5 + "")
                .appendQueryParameter("format", "json")
                .build().toString();

        String result = null;

        try {
            final HttpClient httpclient = new DefaultHttpClient();
            final HttpUriRequest request = new HttpGet(url);
            final HttpResponse response = httpclient.execute(request);
            final StatusLine status = response.getStatusLine();

            if (status.getStatusCode() == HttpStatus.SC_OK) {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();

                try {
                    response.getEntity().writeTo(out);
                    result = out.toString();
                } finally {
                    out.close();
                }
            } else {
                response.getEntity().getContent().close();
                throw new IOException(status.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        deserialize2(result);

        return;
    }

    private void deserialize2(String result) {
        Album album;

        try {
            JSONObject jsonObjectMain = new JSONObject(result);
            JSONArray jsonArray = jsonObjectMain.getJSONObject("topalbums").getJSONArray("album");
            JSONObject jsonObject;

            for (int i=0; i<5; i++) {
                jsonObject = jsonArray.getJSONObject(i);

                album = new Album();
                ArtistFragment.mArtist.getAlbums().add(album);
                ArtistFragment.mArtist.getAlbums().get(i).setRank(jsonObject.getJSONObject("@attr").getInt("rank"));
                ArtistFragment.mArtist.getAlbums().get(i).setArtist(jsonObject.getJSONObject("artist").getString("name"));
                ArtistFragment.mArtist.getAlbums().get(i).setName(jsonObject.getString("name"));
                ArtistFragment.mArtist.getAlbums().get(i).setPlayCount(jsonObject.getInt("playcount"));
                ArtistFragment.mArtist.getAlbums().get(i).setUrl(jsonObject.getString("url"));
                if (!jsonObject.getJSONArray("image").getJSONObject(2).getString("#text")
                        .equals("")) {
                    mInputStream = (InputStream) new URL(jsonObject.getJSONArray("image")
                            .getJSONObject(2).getString("#text")).getContent();
                    mDrawable = Drawable.createFromStream(mInputStream, "src name");
                    ArtistFragment.mArtist.getAlbums().get(i).setImage(mDrawable);
                }

            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }


}
