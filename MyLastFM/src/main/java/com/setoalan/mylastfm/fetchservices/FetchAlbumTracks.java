package com.setoalan.mylastfm.fetchservices;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.setoalan.mylastfm.AlbumFragment;
import com.setoalan.mylastfm.MyLastFMActivity;
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

public class FetchAlbumTracks {

    private static final String LASTFM_URL = "http://ws.audioscrobbler.com/2.0/?";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    InputStream mInputStream;
    Drawable mDrawable;

    public void fetchAlbumTracks()  {
        String url = Uri.parse(LASTFM_URL).buildUpon()
                .appendQueryParameter("method", "album.getinfo")
                .appendQueryParameter("artist", AlbumFragment.mAlbum.getArtist())
                .appendQueryParameter("album", AlbumFragment.mAlbum.getName())
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
        Track track;

        try {
            JSONObject jsonObjectMain = new JSONObject(result);
            JSONArray jsonArray = jsonObjectMain.getJSONObject("album").getJSONObject("tracks")
                    .getJSONArray("track");
            JSONObject jsonObject;

            for (int i=0; i<jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                track = new Track();
                AlbumFragment.mAlbum.getTracks().add(track);
                AlbumFragment.mAlbum.getTracks().get(i).setRank(jsonObject.getJSONObject("@attr")
                        .getInt("rank"));
                AlbumFragment.mAlbum.getTracks().get(i).setName(jsonObject.getString("name"));
                AlbumFragment.mAlbum.getTracks().get(i).setDuration(jsonObject.getInt("duration"));
                if (!jsonObjectMain.getJSONObject("album").getJSONArray("image").getJSONObject(2)
                        .getString("#text").equals("")) {
                    mInputStream = (InputStream) new URL(jsonObjectMain.getJSONObject("album")
                            .getJSONArray("image").getJSONObject(2).getString("#text"))
                            .getContent();
                    mDrawable = Drawable.createFromStream(mInputStream, "src name");
                    AlbumFragment.mAlbum.getTracks().get(i).setImage(mDrawable);
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
