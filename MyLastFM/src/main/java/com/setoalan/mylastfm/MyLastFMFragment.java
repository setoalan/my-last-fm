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

public class MyLastFMFragment extends ListFragment {

    public static String USERNAME;
    public static UserInfo USERINFO;
    public static ArrayList<Track> RECENTTRACKS;

    private ArrayList<UserInfo> mList;

    TextView playCountTV, playsSinceTV;
    ImageView albumIV;
    TextView trackNameTV, lastPlayedTV;

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
            new FetchDataTask().execute();
        }
    }

    private class FetchDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            new FetchUserInfo().fetchUserInfo();
            new FetchRecentTracks().fetchRecentTracks();
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
                            .inflate(R.layout.list_item_recent_tracks_header, null);
                } else if (position == 2 || position == 3 || position == 4) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_recent_tracks, null);
                    albumIV = (ImageView) convertView.findViewById(R.id.album_iv);
                    trackNameTV = (TextView) convertView.findViewById(R.id.track_name_tv);
                    lastPlayedTV = (TextView) convertView.findViewById(R.id.last_played_tv);

                    trackNameTV.setText(RECENTTRACKS.get(position-2).getName());
                    lastPlayedTV.setText(RECENTTRACKS.get(position-2).getNowPlaying() + "");
                }
            }

            return convertView;
        }

    }

}
