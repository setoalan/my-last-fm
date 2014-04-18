package com.setoalan.mylastfm.fetchservices;

import android.net.Uri;

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

public class FetchUserInfo {

    private static final String URL = "http://ws.audioscrobbler.com/2.0/?method=";
    private static final String KEY = "caee03757be853540591265ff765b6ff";

    public Void fetchUserInfo()  {
        String url = Uri.parse(URL).buildUpon()
                .appendQueryParameter("method", "user.getinfo")
                .appendQueryParameter("user", MyLastFMFragment.USERNAME)
                .appendQueryParameter("api_key", KEY)
                .appendQueryParameter("format", "json")
                .build().toString();

        String result = null;

        try {
            final HttpClient httpclient = new DefaultHttpClient();;
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

        MyLastFMFragment.USERINFO = deserialize(result);
        return null;
    }

    private UserInfo deserialize(String result) {
        UserInfo user = new UserInfo();

        try {
            JSONObject jsonObject = new JSONObject(result);
            jsonObject = jsonObject.getJSONObject("user");

            user.setName(jsonObject.getString("name"));
            user.setRealName(jsonObject.getString("realname"));
            user.setUrl(jsonObject.getString("url"));
            user.setImage(jsonObject.getString("image"));
            user.setCountry(jsonObject.getString("country"));
            user.setAge(jsonObject.getInt("age"));
            user.setGender(jsonObject.getString("gender"));
            user.setPlayCount(jsonObject.getInt("playcount"));
            user.setRegistered(jsonObject.getJSONObject("registered").getLong("unixtime"));
        } catch (JSONException e){
            e.printStackTrace();
        }

        return user;
    }

}
