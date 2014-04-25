package com.setoalan.mylastfm.fetchservices;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.setoalan.mylastfm.ArtistFragment;
import com.setoalan.mylastfm.datastructures.Event;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class FetchArtistEvents {

    private static final String LASTFM_URL = "http://ws.audioscrobbler.com/2.0/?";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    InputStream mInputStream;
    Drawable mDrawable;

    public void fetchArtistEvents()  {
        String url = Uri.parse(LASTFM_URL).buildUpon()
                .appendQueryParameter("method", "artist.getevents")
                .appendQueryParameter("artist", ArtistFragment.mArtist.getName())
                .appendQueryParameter("api_key", KEY)
                .appendQueryParameter("limit", "10")
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
        Event event;

        try {
            JSONObject jsonObjectMain = new JSONObject(result);
            JSONArray jsonArray = jsonObjectMain.getJSONObject("events").getJSONArray("event");
            JSONObject jsonObject;

            for (int i=0; i<10; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                event = new Event();
                ArtistFragment.mArtist.getEvents().add(event);

                ArtistFragment.mArtist.getEvents().get(i).setTitle(jsonObject.getString("title"));
                ArtistFragment.mArtist.getEvents().get(i).setHeadliner(jsonObject
                        .getJSONObject("artists").getString("headliner"));
                ArtistFragment.mArtist.getEvents().get(i).setArtists(Arrays.asList(jsonObject
                        .getJSONObject("artists").getString("artist").replaceAll("\\[", "")
                        .replaceAll("\\]","").replace("\"", "").split(",")));

                /*for (int j=0; j<ArtistFragment.mArtist.getEvents().get(i).getArtists().size(); j++) {
                   fetchArtistImage(ArtistFragment.mArtist.getEvents().get(i).getArtists().get(j), i);
                }*/

                ArtistFragment.mArtist.getEvents().get(i).setVenue(jsonObject
                        .getJSONObject("venue").getString("name"));
                ArtistFragment.mArtist.getEvents().get(i).setLatitude(jsonObject
                        .getJSONObject("venue").getJSONObject("location").getJSONObject("geo:point")
                        .getString("geo:lat"));
                ArtistFragment.mArtist.getEvents().get(i).setLongitude(jsonObject
                        .getJSONObject("venue").getJSONObject("location").getJSONObject("geo:point")
                        .getString("geo:long"));
                ArtistFragment.mArtist.getEvents().get(i).setCity(jsonObject.getJSONObject("venue")
                        .optString("city"));
                ArtistFragment.mArtist.getEvents().get(i).setCountry(jsonObject
                        .getJSONObject("venue").optString("country"));
                ArtistFragment.mArtist.getEvents().get(i).setStreet(jsonObject
                        .getJSONObject("venue").optString("street"));
                ArtistFragment.mArtist.getEvents().get(i).setPostalCode(jsonObject
                        .getJSONObject("venue").optInt("postalcode"));
                ArtistFragment.mArtist.getEvents().get(i).setUrl(jsonObject.getString("url"));
                ArtistFragment.mArtist.getEvents().get(i)
                        .setWebsite(jsonObject.getString("website"));
                ArtistFragment.mArtist.getEvents().get(i).setPhoneNumber(jsonObject
                        .optString("phonenumber"));
                if (!jsonObject.getJSONArray("image").getJSONObject(2).getString("#text")
                        .equals("")) {
                    mInputStream = (InputStream) new URL(jsonObject.getJSONArray("image")
                            .getJSONObject(2).getString("#text")).getContent();
                    mDrawable = Drawable.createFromStream(mInputStream, "src name");
                    ArtistFragment.mArtist.getEvents().get(i).setImage(mDrawable);
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                ArtistFragment.mArtist.getEvents().get(i).setStartDate(simpleDateFormat
                        .parse(jsonObject.getString("startDate")));
            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return;
    }

}
