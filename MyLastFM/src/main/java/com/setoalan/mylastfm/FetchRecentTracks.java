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
import java.net.URL;

public class FetchRecentTracks {

    private static final String URL = "http://ws.audioscrobbler.com/2.0/?method=";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    Drawable mDrawable;
    InputStream mInputStream;

    public void fetchRecentTracks()  {
        String url = Uri.parse(URL).buildUpon()
                .appendQueryParameter("method", "user.getrecenttracks")
                .appendQueryParameter("user", MyLastFMFragment.USERNAME)
                .appendQueryParameter("api_key", KEY)
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
        Track track = new Track();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("recenttracks").getJSONArray("track");

            track.setArtist(jsonArray.getJSONObject(0).getJSONObject("artist").getString("#text"));
            track.setName(jsonArray.getJSONObject(0).getString("name"));
            track.setAlbum(jsonArray.getJSONObject(0).getJSONObject("album").getString("#text"));
            track.setUrl(jsonArray.getJSONObject(0).getString("url"));
            mInputStream = (InputStream) new URL(jsonArray.getJSONObject(0)
                    .getJSONArray("image").getJSONObject(2).getString("#text")).getContent();
            mDrawable = Drawable.createFromStream(mInputStream, "src name");
            track.setImage(mDrawable);
            JSONObject dateJsonObject = jsonArray.getJSONObject(0).optJSONObject("date");
            if (dateJsonObject != null)
                track.setDate(dateJsonObject.getLong("uts"));
            JSONObject nowPlayingJsonObject = jsonArray.getJSONObject(0).optJSONObject("@attr");
            if (nowPlayingJsonObject!= null)
                track.setNowPlaying(true);
            MyLastFMFragment.RECENTTRACKS.add(track);

            for (int i=1; i<3; i++) {
                track = new Track();
                track.setArtist(jsonArray.getJSONObject(i).getJSONObject("artist").getString("#text"));
                track.setName(jsonArray.getJSONObject(i).getString("name"));
                track.setAlbum(jsonArray.getJSONObject(i).getJSONObject("album").getString("#text"));
                track.setUrl(jsonArray.getJSONObject(i).getString("url"));
                mInputStream = (InputStream) new URL(jsonArray.getJSONObject(i)
                        .getJSONArray("image").getJSONObject(2).getString("#text")).getContent();
                mDrawable = Drawable.createFromStream(mInputStream, "src name");
                track.setImage(mDrawable);
                track.setDate(jsonArray.getJSONObject(i).getJSONObject("date").getLong("uts"));
                MyLastFMFragment.RECENTTRACKS.add(track);
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
