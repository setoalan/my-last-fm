package com.setoalan.mylastfm.fetchservices;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.setoalan.mylastfm.MyLastFMFragment;
import com.setoalan.mylastfm.TopArtistsFragment;
import com.setoalan.mylastfm.datastructures.Artist;

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

public class FetchArtists {

    private static final String URL = "http://ws.audioscrobbler.com/2.0/?method=";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    InputStream mInputStream;
    Drawable mDrawable;

    public void fetchArtists(int limit, String period)  {
        String url = Uri.parse(URL).buildUpon()
                .appendQueryParameter("method", "user.gettopartists")
                .appendQueryParameter("user", "LostPolitik42")//MyLastFMFragment.USERNAME)
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
        Artist artist;

        try {
            JSONObject jsonObjectMain = new JSONObject(result);
            JSONArray jsonArray = jsonObjectMain.getJSONObject("topartists")
                    .getJSONArray("artist");
            JSONObject jsonObject;

            for (int i=0; i<limit; i++) {
                jsonObject = jsonArray.getJSONObject(i);

                artist = new Artist();
                artist.setRank(jsonObject.getJSONObject("@attr").getInt("rank"));
                artist.setName(jsonObject.getString("name"));
                artist.setPlayCount(jsonObject.getInt("playcount"));
                artist.setUrl(jsonObject.getString("url"));
                mInputStream = (InputStream) new URL(jsonObject.getJSONArray("image")
                        .getJSONObject(2).getString("#text")).getContent();
                mDrawable = Drawable.createFromStream(mInputStream, "src name");
                artist.setImage(mDrawable);

                if (limit == 3 && period.equals("7day")) {
                    MyLastFMFragment.WEEKLY_ARTISTS.add(artist);
                } else if (period.equals("7day")) {
                    TopArtistsFragment.WEEK_ARTISTS.add(artist);
                } else if (period.equals("1month")) {
                    TopArtistsFragment.MONTH_ARTISTS.add(artist);
                } else if (period.equals("12month")) {
                    TopArtistsFragment.YEAR_ARTISTS.add(artist);
                } else if (period.equals("overall")) {
                    TopArtistsFragment.OVERALL_ARTISTS.add(artist);
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
