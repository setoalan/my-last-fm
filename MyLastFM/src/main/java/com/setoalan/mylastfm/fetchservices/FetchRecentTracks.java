package com.setoalan.mylastfm.fetchservices;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.setoalan.mylastfm.MyLastFMFragment;
import com.setoalan.mylastfm.RecentTracksFragment;
import com.setoalan.mylastfm.datastructures.Track;

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

public class FetchRecentTracks {

    private static final String URL = "http://ws.audioscrobbler.com/2.0/?";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    Drawable mDrawable;
    InputStream mInputStream;

    public void fetchRecentTracks(int limit)  {
        String url = Uri.parse(URL).buildUpon()
                .appendQueryParameter("method", "user.getrecenttracks")
                .appendQueryParameter("user", MyLastFMFragment.USERNAME)
                .appendQueryParameter("api_key", KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("limit", limit + "")
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

        deserialize(result, limit);

        return;
    }

    private void deserialize(String result, int limit) {
        Track track = new Track();

        try {
            JSONObject jsonObjectMain = new JSONObject(result);
            JSONObject jsonObject = jsonObjectMain.getJSONObject("recenttracks")
                    .getJSONArray("track").getJSONObject(0);

            track.setArtist(jsonObject.getJSONObject("artist").getString("#text"));
            track.setName(jsonObject.getString("name"));
            track.setAlbum(jsonObject.getJSONObject("album").getString("#text"));
            track.setUrl(jsonObject.getString("url"));
            mInputStream = (InputStream) new URL(jsonObject.getJSONArray("image").getJSONObject(2)
                    .getString("#text")).getContent();
            mDrawable = Drawable.createFromStream(mInputStream, "src name");
            track.setImage(mDrawable);
            JSONObject dateJsonObject = jsonObject.optJSONObject("date");
            if (dateJsonObject != null)
                track.setDate(dateJsonObject.getLong("uts"));
            JSONObject nowPlayingJsonObject = jsonObject.optJSONObject("@attr");
            if (nowPlayingJsonObject!= null)
                track.setNowPlaying(true);

            if (limit == 3)
                MyLastFMFragment.RECENT_TRACKS.add(track);
            else
                RecentTracksFragment.RECENT_TRACKS.add(track);

            for (int i=1; i<limit; i++) {
                jsonObject = jsonObjectMain.getJSONObject("recenttracks").getJSONArray("track")
                        .getJSONObject(i);

                track = new Track();
                track.setArtist(jsonObject.getJSONObject("artist").getString("#text"));
                track.setName(jsonObject.getString("name"));
                track.setAlbum(jsonObject.getJSONObject("album").getString("#text"));
                track.setUrl(jsonObject.getString("url"));
                mInputStream = (InputStream) new URL(jsonObject.getJSONArray("image")
                        .getJSONObject(2).getString("#text")).getContent();
                mDrawable = Drawable.createFromStream(mInputStream, "src name");
                track.setImage(mDrawable);
                track.setDate(jsonObject.getJSONObject("date").getLong("uts"));

                if (limit == 3)
                    MyLastFMFragment.RECENT_TRACKS.add(track);
                else
                    RecentTracksFragment.RECENT_TRACKS.add(track);
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
