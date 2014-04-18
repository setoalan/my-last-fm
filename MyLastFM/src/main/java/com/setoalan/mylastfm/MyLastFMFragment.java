package com.setoalan.mylastfm;

import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyLastFMFragment extends ListFragment {

    public static String USERNAME;
    public static UserInfo USERINFO;
    public static ArrayList<Track> RECENTTRACKS;
    public static ArrayList<Artist> WEEKLYARTISTS;

    private ArrayList<UserInfo> mList;

    TextView headerTV;
    TextView playCountTV, playsSinceTV;
    ImageView albumIV, artistIV;
    TextView trackNameTV, lastPlayedTV;
    TextView artistTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        USERNAME = sharedPreferences.getString("username", null);
        if (USERNAME == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            mList = new ArrayList<UserInfo>();
            RECENTTRACKS = new ArrayList<Track>();
            WEEKLYARTISTS = new ArrayList<Artist>();
            new FetchDataTask().execute();
        }
    }

    private class FetchDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            new FetchUserInfo().fetchUserInfo();
            new FetchRecentTracks().fetchRecentTracks();
            new FetchWeeklyArtists().fetchWeeklyArtists();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mList.add(USERINFO);
            mList.add(USERINFO);
            mList.add(USERINFO);
            mList.add(USERINFO);
            mList.add(USERINFO);
            mList.add(USERINFO);
            mList.add(USERINFO);
            mList.add(USERINFO);
            mList.add(USERINFO);
            UserInfoAdapter userInfoAdapter = new UserInfoAdapter(mList);
            setListAdapter(userInfoAdapter);
        }

    }

    private class UserInfoAdapter extends ArrayAdapter<UserInfo> {

        public UserInfoAdapter(ArrayList<UserInfo> List) {
            super(getActivity(), android.R.layout.simple_list_item_1, List);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                if (position == 0) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_user, null);

                    playCountTV = (TextView) convertView.findViewById(R.id.play_count_tv);
                    playsSinceTV = (TextView) convertView.findViewById(R.id.plays_since_tv);

                    playCountTV.setText(USERINFO.getPlayCount() + "");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                    playsSinceTV.setText("plays since "
                            + simpleDateFormat.format(new Date(USERINFO.getRegistered()*1000)));
                } else if (position == 1) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_header, null);
                    headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                    headerTV.setText("Recent Tracks");
                } else if (position == 2 || position == 3 || position == 4) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_main, null);

                    albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                    trackNameTV = (TextView) convertView.findViewById(R.id.name_tv);
                    lastPlayedTV = (TextView) convertView.findViewById(R.id.detail_tv);

                    albumIV.setImageDrawable(RECENTTRACKS.get(position - 2).getImage());
                    trackNameTV.setText(RECENTTRACKS.get(position - 2).getName());
                    if (RECENTTRACKS.get(position - 2).getNowPlaying()) {
                        lastPlayedTV.setText("now");
                    } else {
                        long difference = new Date().getTime()/1000 -
                                new Date(RECENTTRACKS.get(position - 2).getDate()).getTime();
                        if (difference < 3600) {
                            lastPlayedTV.setText(TimeUnit.SECONDS.toMinutes(difference)
                                    + " minutes ago");
                        } else if (difference < 86400) {
                            lastPlayedTV.setText(TimeUnit.SECONDS.toMinutes(difference)
                                    + " hours ago");
                        } else {
                            lastPlayedTV.setText(TimeUnit.SECONDS.toDays(difference)
                                    + " days ago");
                        }
                    }
                } else if (position == 5) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_header, null);

                    headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                    headerTV.setText("Top Artists");
                } else if (position == 6 || position == 7 || position == 8) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_main, null);

                    artistIV = (ImageView) convertView.findViewById(R.id.image_iv);
                    artistTV = (TextView) convertView.findViewById(R.id.name_tv);
                    playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

                    artistIV.setImageDrawable(WEEKLYARTISTS.get(position - 6).getImage());
                    artistTV.setText(WEEKLYARTISTS.get(position - 6).getName());
                    playCountTV.setText(WEEKLYARTISTS.get(position - 6).getPlayCount() + " plays");
                }
            }

            return convertView;
        }

    }

}
