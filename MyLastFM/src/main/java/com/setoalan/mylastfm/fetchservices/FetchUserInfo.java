package com.setoalan.mylastfm.fetchservices;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.setoalan.mylastfm.MyLastFMActivity;
import com.setoalan.mylastfm.MyLastFMFragment;
import com.setoalan.mylastfm.datastructures.UserInfo;

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

public class FetchUserInfo {

    private static final String URL = "http://ws.audioscrobbler.com/2.0/?method=";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    InputStream mInputStream;
    Drawable mDrawable;

    public void fetchUserInfo()  {
        String url = Uri.parse(URL).buildUpon()
                .appendQueryParameter("method", "user.getinfo")
                .appendQueryParameter("user", MyLastFMFragment.USERNAME)
                .appendQueryParameter("api_key", KEY)
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
        UserInfo user = new UserInfo();

        try {
            JSONObject jsonObject = new JSONObject(result);
            jsonObject = jsonObject.getJSONObject("user");

            user.setName(jsonObject.getString("name"));
            user.setRealName(jsonObject.getString("realname"));
            user.setUrl(jsonObject.getString("url"));
            mInputStream = (InputStream) new java.net.URL(jsonObject.getJSONArray("image")
                    .getJSONObject(2).getString("#text")).getContent();
            mDrawable = Drawable.createFromStream(mInputStream, "src name");
            user.setImage(mDrawable);
            user.setCountry(jsonObject.getString("country"));
            user.setAge(jsonObject.optInt("age"));
            user.setGender(jsonObject.getString("gender"));
            user.setPlayCount(jsonObject.getInt("playcount"));
            user.setRegistered(jsonObject.getJSONObject("registered").getLong("unixtime"));

            MyLastFMActivity.USERINFO = user;
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
