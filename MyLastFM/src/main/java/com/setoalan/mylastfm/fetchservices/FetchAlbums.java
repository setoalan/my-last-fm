package com.setoalan.mylastfm.fetchservices;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.setoalan.mylastfm.MyLastFMFragment;
import com.setoalan.mylastfm.TopAlbumsFragment;
import com.setoalan.mylastfm.datastructures.Album;

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

public class FetchAlbums {

    private static final String URL = "http://ws.audioscrobbler.com/2.0/?";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    InputStream mInputStream;
    Drawable mDrawable;

    public void fetchAlbums(int limit, String period)  {
        String url = Uri.parse(URL).buildUpon()
                .appendQueryParameter("method", "user.gettopalbums")
                .appendQueryParameter("user", MyLastFMFragment.USERNAME)
                .appendQueryParameter("api_key", KEY)
                .appendQueryParameter("period", period)
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

        deserialize(result, limit, period);

        return;
    }

    private void deserialize(String result, int limit, String period) {
        Album album;

        try {
            JSONObject jsonObjectMain = new JSONObject(result);
            JSONArray jsonArray = jsonObjectMain.getJSONObject("topalbums")
                    .getJSONArray("album");
            JSONObject jsonObject;

            for (int i=0; i<limit; i++) {
                jsonObject = jsonArray.getJSONObject(i);

                album = new Album();
                album.setRank(jsonObject.getJSONObject("@attr").getInt("rank"));
                album.setArtist(jsonObject.getJSONObject("artist").getString("name"));
                album.setName(jsonObject.getString("name"));
                album.setPlayCount(jsonObject.getInt("playcount"));
                album.setUrl(jsonObject.getString("url"));
                mInputStream = (InputStream) new java.net.URL(jsonObject.getJSONArray("image")
                        .getJSONObject(2).getString("#text")).getContent();
                mDrawable = Drawable.createFromStream(mInputStream, "src name");
                album.setImage(mDrawable);

                if (limit == 3 && period.equals("7day")) {
                    MyLastFMFragment.WEEKLY_ALBUMS.add(album);
                } else if (period.equals("7day")) {
                    TopAlbumsFragment.WEEK_ALBUMS.add(album);
                } else if (period.equals("1month")) {
                    TopAlbumsFragment.MONTH_ALBUMS.add(album);
                } else if (period.equals("12month")) {
                    TopAlbumsFragment.YEAR_ALBUMS.add(album);
                } else if (period.equals("overall")) {
                    TopAlbumsFragment.OVERALL_ALBUMS.add(album);
                }
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
