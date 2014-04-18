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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Album;
import com.setoalan.mylastfm.datastructures.Artist;
import com.setoalan.mylastfm.datastructures.Track;
import com.setoalan.mylastfm.datastructures.UserInfo;
import com.setoalan.mylastfm.fetchservices.FetchArtists;
import com.setoalan.mylastfm.fetchservices.FetchRecentTracks;
import com.setoalan.mylastfm.fetchservices.FetchUserInfo;
import com.setoalan.mylastfm.fetchservices.FetchWeeklyAlbums;
import com.setoalan.mylastfm.fetchservices.FetchWeeklyTracks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyLastFMFragment extends ListFragment {

    public static String USERNAME;
    public static UserInfo USERINFO;
    public static ArrayList<Track> RECENT_TRACKS;
    public static ArrayList<Artist> WEEKLY_ARTISTS;
    public static ArrayList<Track> WEEKLY_TRACKS;
    public static ArrayList<Album> WEEKLY_ALBUMS;

    private ArrayList<String> mList;

    Button headerBTN;
    ImageView albumIV, artistIV;
    TextView albumTV, artistTV, headerTV, lastPlayedTV, playCountTV, playsSinceTV, trackTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        USERNAME = sharedPreferences.getString("username", null);
        if (USERNAME == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            mList = new ArrayList<String>();
            RECENT_TRACKS = new ArrayList<Track>();
            WEEKLY_ARTISTS = new ArrayList<Artist>();
            WEEKLY_TRACKS = new ArrayList<Track>();
            WEEKLY_ALBUMS = new ArrayList<Album>();
            new FetchDataTask().execute();
        }
    }

    private class FetchDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            new FetchUserInfo().fetchUserInfo();
            new FetchRecentTracks().fetchRecentTracks(3);
            new FetchArtists().fetchArtists(3, "7day");
            new FetchWeeklyTracks().fetchWeeklyTracks();
            new FetchWeeklyAlbums().fetchWeeklyAlbums();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i=0; i<17; i++)
                mList.add("");
            MyLastFMAdapter myLastFMAdapter = new MyLastFMAdapter(mList);
            setListAdapter(myLastFMAdapter);
        }

    }

    private class MyLastFMAdapter extends ArrayAdapter<String> {

        public MyLastFMAdapter(ArrayList<String> data) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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
                headerBTN = (Button) convertView.findViewById(R.id.header_btn);

                headerTV.setText("Recent Tracks");
                headerBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), RecentTracksActivity.class));
                    }
                });
            } else if (position == 2 || position == 3 || position == 4) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_main, null);
                albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                trackTV = (TextView) convertView.findViewById(R.id.name_tv);
                lastPlayedTV = (TextView) convertView.findViewById(R.id.detail_tv);

                albumIV.setImageDrawable(RECENT_TRACKS.get(position - 2).getImage());
                trackTV.setText(RECENT_TRACKS.get(position - 2).getName());
                if (RECENT_TRACKS.get(position - 2).isNowPlaying()) {
                    lastPlayedTV.setText("now");
                } else {
                    long difference = new Date().getTime()/1000 - new Date(RECENT_TRACKS
                            .get(position - 2).getDate()).getTime();
                    long time;
                    if (difference < 3600) {
                        time = TimeUnit.SECONDS.toMinutes(difference);
                        if (time == 1)
                            lastPlayedTV.setText(time + " minute ago");
                        else
                            lastPlayedTV.setText(time  + " minutes ago");
                    } else if (difference < 86400) {
                        time = TimeUnit.SECONDS.toHours(difference);
                        if (time == 1)
                            lastPlayedTV.setText(time + " hour ago");
                        else
                            lastPlayedTV.setText(time + " hours ago");
                    } else {
                        time = TimeUnit.SECONDS.toDays(difference);
                        if (time == 1)
                            lastPlayedTV.setText(time + " day ago");
                        else
                            lastPlayedTV.setText(time + " days ago");
                    }
                }
            } else if (position == 5) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_header, null);
                headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                headerBTN = (Button) convertView.findViewById(R.id.header_btn);

                headerTV.setText("Top Artists");
                headerBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), TopArtistsActivity.class));
                    }
                });
            } else if (position == 6 || position == 7 || position == 8) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_main, null);
                artistIV = (ImageView) convertView.findViewById(R.id.image_iv);
                artistTV = (TextView) convertView.findViewById(R.id.name_tv);
                playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

                artistIV.setImageDrawable(WEEKLY_ARTISTS.get(position - 6).getImage());
                artistTV.setText(WEEKLY_ARTISTS.get(position - 6).getName());
                playCountTV.setText(WEEKLY_ARTISTS.get(position - 6).getPlayCount() + " plays");
            } else if (position == 9) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_header, null);
                headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                headerBTN = (Button) convertView.findViewById(R.id.header_btn);

                headerTV.setText("Top Tracks");
                headerBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else if (position == 10 || position == 11 || position == 12) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_main, null);
                albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                trackTV = (TextView) convertView.findViewById(R.id.name_tv);
                playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

                albumIV.setImageDrawable(WEEKLY_TRACKS.get(position - 10).getImage());
                trackTV.setText(WEEKLY_TRACKS.get(position - 10).getName());
                playCountTV.setText(WEEKLY_TRACKS.get(position - 10).getPlayCount() + " plays");
            } else if (position == 13) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_header, null);
                headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                headerBTN = (Button) convertView.findViewById(R.id.header_btn);

                headerTV.setText("Top Albums");
                headerBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else if (position == 14 || position == 15 || position == 16) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_main, null);
                albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                albumTV = (TextView) convertView.findViewById(R.id.name_tv);
                playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

                albumIV.setImageDrawable(WEEKLY_ALBUMS.get(position - 14).getImage());
                albumTV.setText(WEEKLY_ALBUMS.get(position - 14).getName());
                playCountTV.setText(WEEKLY_ALBUMS.get(position - 14).getPlayCount() + " plays");
            }

            return convertView;
        }

    }

}
