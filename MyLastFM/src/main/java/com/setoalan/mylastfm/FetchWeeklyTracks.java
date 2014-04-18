package com.setoalan.mylastfm;

import android.graphics.drawable.Drawable;
import android.net.Uri;

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

public class FetchWeeklyTracks {

    private static final String URL = "http://ws.audioscrobbler.com/2.0/?method=";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    InputStream mInputStream;
    Drawable mDrawable;

    public void fetchWeeklyTracks()  {
        String url = Uri.parse(URL).buildUpon()
                .appendQueryParameter("method", "user.gettoptracks")
                .appendQueryParameter("user", MyLastFMFragment.USERNAME)
                .appendQueryParameter("api_key", KEY)
                .appendQueryParameter("period", "7day")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("limit", "3")
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
            JSONArray jsonArray = jsonObjectMain.getJSONObject("toptracks")
                    .getJSONArray("track");
            JSONObject jsonObject;

            for (int i=0; i<3; i++) {
                jsonObject = jsonArray.getJSONObject(i);

                track = new Track();
                track.setRank(jsonObject.getJSONObject("@attr").getInt("rank"));
                track.setArtist(jsonObject.getJSONObject("artist").getString("name"));
                track.setName(jsonObject.getString("name"));
                track.setDuration(jsonObject.getInt("duration"));
                track.setPlayCount(jsonObject.getInt("playcount"));
                track.setUrl(jsonObject.getString("url"));
                mInputStream = (InputStream) new java.net.URL(jsonObject.getJSONArray("image")
                        .getJSONObject(2).getString("#text")).getContent();
                mDrawable = Drawable.createFromStream(mInputStream, "src name");
                track.setImage(mDrawable);

                MyLastFMFragment.WEEKLY_TRACKS.add(track);
            }

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