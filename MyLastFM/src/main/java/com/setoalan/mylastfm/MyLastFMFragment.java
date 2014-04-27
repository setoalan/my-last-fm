package com.setoalan.mylastfm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.setoalan.mylastfm.activities.AlbumActivity;
import com.setoalan.mylastfm.activities.ArtistActivity;
import com.setoalan.mylastfm.activities.LoginActivity;
import com.setoalan.mylastfm.fetchservices.FetchAlbums;
import com.setoalan.mylastfm.fetchservices.FetchArtists;
import com.setoalan.mylastfm.fetchservices.FetchRecentTracks;
import com.setoalan.mylastfm.fetchservices.FetchTracks;
import com.setoalan.mylastfm.fetchservices.FetchUserInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyLastFMFragment extends ListFragment {

    public static String USERNAME;

    private ArrayList<String> mList;

    ImageView albumIV, artistIV;
    TextView albumTV, artistTV, headerTV, lastPlayedTV, playCountTV, playsSinceTV, trackTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        USERNAME = sharedPreferences.getString("username", null);
        if (USERNAME == null) {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1);
        } else {
            mList = new ArrayList<String>();
            if (MyLastFMActivity.USERINFO.getName() == null) {
                new FetchDataTask().execute();
            } else {
                for (int i=0; i<17; i++)
                    mList.add("");
                setListAdapter(new MyLastFMAdapter(mList));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mList = new ArrayList<String>();
        new FetchDataTask().execute();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (position == 6 || position == 7 || position == 8 ) {
            TopArtistsFragment.artist = MyLastFMActivity.WEEKLY_ARTISTS.get(position - 6);
            startActivity(new Intent(getActivity(), ArtistActivity.class));
        } else if (position == 14 || position == 15 || position == 16) {
            TopAlbumsFragment.album = MyLastFMActivity.WEEKLY_ALBUMS.get(position - 14);
            startActivity(new Intent(getActivity(), AlbumActivity.class));
        }

    }

    private class FetchDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            new FetchUserInfo().fetchUserInfo();
            new FetchRecentTracks().fetchRecentTracks(3);
            new FetchArtists().fetchArtists(3, "7day");
            new FetchTracks().fetchTracks(3, "7day");
            new FetchAlbums().fetchAlbums(3, "7day");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyLastFMActivity.refreshDrawer();
            for (int i=0; i<17; i++)
                mList.add("");
            if (MyLastFMActivity.USERINFO.getName() == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("No user with that name was found")
                        .setPositiveButton("Enter username", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(getActivity(),
                                        LoginActivity.class), 1);
                            }
                        });
                builder.setCancelable(false);
                Dialog dialog = builder.create();
                dialog.show();
            }
            if (isVisible())
                setListAdapter(new MyLastFMAdapter(mList));
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
                playCountTV = (TextView) convertView.findViewById(R.id.plays_tv);
                playsSinceTV = (TextView) convertView.findViewById(R.id.plays_since_tv);

                playCountTV.setText(MyLastFMActivity.USERINFO.getPlayCount() + "");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy");
                playsSinceTV.setText("plays since "
                        + simpleDateFormat.format(new Date(MyLastFMActivity.USERINFO
                        .getRegistered()*1000)));
            } else if (position == 1) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_header, null);
                headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                headerTV.setText("Recent Tracks");
            } else if (position == 2 || position == 3 || position == 4) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_default, null);
                if (MyLastFMActivity.THREE_RECENT_TRACKS.size() == 0) return convertView;
                albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                trackTV = (TextView) convertView.findViewById(R.id.name_tv);
                lastPlayedTV = (TextView) convertView.findViewById(R.id.detail_tv);

                albumIV.setImageDrawable(MyLastFMActivity.THREE_RECENT_TRACKS.get(position - 2)
                        .getImage());
                trackTV.setText(MyLastFMActivity.THREE_RECENT_TRACKS.get(position - 2).getName());
                trackTV.setSelected(true);
                if (MyLastFMActivity.THREE_RECENT_TRACKS.get(position - 2).isNowPlaying()) {
                    lastPlayedTV.setText("now");
                } else {
                    long difference = new Date().getTime()/1000 -
                            new Date(MyLastFMActivity.THREE_RECENT_TRACKS.get(position - 2)
                                    .getDate()).getTime();
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
                    } else if (difference < 2592000) {
                        time = TimeUnit.SECONDS.toDays(difference);
                        if (time == 1)
                            lastPlayedTV.setText(time + " day ago");
                        else
                            lastPlayedTV.setText(time + " days ago");
                    } else if (difference < 31556952) {
                        time = TimeUnit.SECONDS.toDays(difference) / 12;
                        if (time == 1)
                            lastPlayedTV.setText(time + " month ago");
                        else
                            lastPlayedTV.setText(time + " months ago");
                    } else {
                        time = TimeUnit.SECONDS.toDays(difference) / 365;
                        if (time == 1)
                            lastPlayedTV.setText(time + " year ago");
                        else
                            lastPlayedTV.setText(time + " years ago");
                    }
                }
            } else if (position == 5) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_header, null);
                headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                headerTV.setText("Top Artists");
            } else if (position == 6 || position == 7 || position == 8) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_default, null);
                if (MyLastFMActivity.WEEKLY_ARTISTS.size() == 0) return convertView;
                artistIV = (ImageView) convertView.findViewById(R.id.image_iv);
                artistTV = (TextView) convertView.findViewById(R.id.name_tv);
                playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

                artistIV.setImageDrawable(MyLastFMActivity.WEEKLY_ARTISTS.get(position - 6)
                        .getImage());
                artistTV.setText(MyLastFMActivity.WEEKLY_ARTISTS.get(position - 6).getName());
                playCountTV.setText(MyLastFMActivity.WEEKLY_ARTISTS.get(position - 6)
                        .getPlayCount() + " plays");
            } else if (position == 9) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_header, null);
                headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                headerTV.setText("Top Tracks");
            } else if (position == 10 || position == 11 || position == 12) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_default, null);
                if (MyLastFMActivity.WEEKLY_TRACKS.size() == 0) return convertView;
                albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                trackTV = (TextView) convertView.findViewById(R.id.name_tv);
                playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

                albumIV.setImageDrawable(MyLastFMActivity.WEEKLY_TRACKS.get(position - 10)
                        .getImage());
                trackTV.setText(MyLastFMActivity.WEEKLY_TRACKS.get(position - 10).getName());
                playCountTV.setText(MyLastFMActivity.WEEKLY_TRACKS.get(position - 10)
                        .getPlayCount() + " plays");
            } else if (position == 13) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_header, null);
                headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                headerTV.setText("Top Albums");
            } else if (position == 14 || position == 15 || position == 16) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_default, null);
                if (MyLastFMActivity.WEEKLY_ALBUMS.size() == 0) return convertView;
                albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                albumTV = (TextView) convertView.findViewById(R.id.name_tv);
                playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

                albumIV.setImageDrawable(MyLastFMActivity.WEEKLY_ALBUMS.get(position - 14)
                        .getImage());
                albumTV.setText(MyLastFMActivity.WEEKLY_ALBUMS.get(position - 14).getName());
                playCountTV.setText(MyLastFMActivity.WEEKLY_ALBUMS.get(position - 14)
                        .getPlayCount() + " plays");
            }

            return convertView;
        }

    }

}
