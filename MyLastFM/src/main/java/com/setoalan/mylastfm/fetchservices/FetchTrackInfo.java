package com.setoalan.mylastfm.fetchservices;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.setoalan.mylastfm.MyLastFMActivity;
import com.setoalan.mylastfm.TrackFragment;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchTrackInfo {

    private static final String LASTFM_URL = "http://ws.audioscrobbler.com/2.0/?";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    InputStream mInputStream;
    Drawable mDrawable;

    public void fetchTrackInfo()  {
        String url = Uri.parse(LASTFM_URL).buildUpon()
                .appendQueryParameter("method", "track.getinfo")
                .appendQueryParameter("artist", TrackFragment.mTrack.getArtist())
                .appendQueryParameter("track", TrackFragment.mTrack.getName())
                .appendQueryParameter("api_key", KEY)
                .appendQueryParameter("username", MyLastFMActivity.USERINFO.getName())
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
        try {
            JSONObject jsonObject = new JSONObject(result);
            jsonObject = jsonObject.getJSONObject("track");

            TrackFragment.mTrack.setRank(jsonObject.getJSONObject("album").getJSONObject("@attr")
                    .getInt("position"));
            mInputStream = (InputStream) new URL(jsonObject.getJSONObject("album")
                    .getJSONArray("image").getJSONObject(2).getString("#text")).getContent();
            mDrawable = Drawable.createFromStream(mInputStream, "src name");
            TrackFragment.mTrack.setImage(mDrawable);
            TrackFragment.mTrack.setListeners(jsonObject.getInt("listeners"));
            TrackFragment.mTrack.setPlays(jsonObject.getInt("playcount"));
            TrackFragment.mTrack.setDuration(jsonObject.getInt("duration"));
            TrackFragment.mTrack.setSummary(jsonObject.getJSONObject("wiki").getString("summary"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }

}
