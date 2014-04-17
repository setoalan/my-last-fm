package com.setoalan.mylastfm;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLastFMFragment extends Fragment {

    public static String USERNAME;
    public static UserInfo USERINFO;

    TextView playCountTV, playsSinceTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        USERNAME = sharedPreferences.getString("username", null);
        if (USERNAME == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            new FetchDataTask().execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_last_fm, container, false);

        playCountTV = (TextView) v.findViewById(R.id.play_count_tv);
        playsSinceTV = (TextView) v.findViewById(R.id.plays_since_tv);

        return v;
    }

    private class FetchDataTask extends AsyncTask<Void, Void, UserInfo> {

        @Override
        protected UserInfo doInBackground(Void... params) {
            return new FetchUserInfo().fetchUserInfo();
        }

        @Override
        protected void onPostExecute(UserInfo userInfo) {
            USERINFO = userInfo;
            playCountTV.setText(USERINFO.getPlayCount() + "");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            playsSinceTV.setText("plays since "
                    + simpleDateFormat.format(new Date(USERINFO.getRegistered()*1000)));
        }

    }

}
