package com.setoalan.mylastfm;

import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.setoalan.mylastfm.activities.TrackActivity;
import com.setoalan.mylastfm.datastructures.Track;
import com.setoalan.mylastfm.fetchservices.FetchRecentTracks;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RecentTracksFragment extends ListFragment {

    RecentTracksAdapter mRecentTracksAdapter;

    ImageView albumIV;
    TextView artistTV, lastPlayedTV, trackTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (MyLastFMActivity.RECENT_TRACKS.isEmpty()) {
            new FetchDataTask().execute();
        } else {
            mRecentTracksAdapter = new RecentTracksAdapter(MyLastFMActivity.RECENT_TRACKS);
            setListAdapter(mRecentTracksAdapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TopTracksFragment.track = MyLastFMActivity.RECENT_TRACKS.get(position);
        TopTracksFragment.track.setArtist(MyLastFMActivity.RECENT_TRACKS.get(position).getArtist());
        startActivity(new Intent(getActivity(), TrackActivity.class));
    }

    private class FetchDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            new FetchRecentTracks().fetchRecentTracks(50);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isVisible()) {
                mRecentTracksAdapter = new RecentTracksAdapter(MyLastFMActivity.RECENT_TRACKS);
                setListAdapter(mRecentTracksAdapter);
            }
        }

    }

    private class RecentTracksAdapter extends ArrayAdapter<Track> {

        public RecentTracksAdapter(ArrayList<Track> data) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_detail, null);

            Track track = MyLastFMActivity.RECENT_TRACKS.get(position);

            albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
            artistTV = (TextView) convertView.findViewById(R.id.artist_tv);
            trackTV = (TextView) convertView.findViewById(R.id.name_tv);
            lastPlayedTV = (TextView) convertView.findViewById(R.id.detail_tv);

            albumIV.setImageDrawable(track.getImage());
            artistTV.setText(track.getArtist());
            trackTV.setText(track.getName());
            trackTV.setSelected(true);
            if (track.isNowPlaying()) {
                lastPlayedTV.setText("now");
            } else {
                long difference = new Date().getTime()/1000 - new Date(track.getDate()).getTime();
                long time;
                if (difference < 3600) {
                    time = TimeUnit.SECONDS.toMinutes(difference);
                    if (time == 1) lastPlayedTV.setText(time + " minute ago");
                    else lastPlayedTV.setText(time  + " minutes ago");
                } else if (difference < 86400) {
                    time = TimeUnit.SECONDS.toHours(difference);
                    if (time == 1) lastPlayedTV.setText(time + " hour ago");
                    else lastPlayedTV.setText(time + " hours ago");
                }  else if (difference < 2592000) {
                    time = TimeUnit.SECONDS.toDays(difference);
                    if (time == 1) lastPlayedTV.setText(time + " day ago");
                    else lastPlayedTV.setText(time + " days ago");
                } else if (difference < 31556952) {
                    time = TimeUnit.SECONDS.toDays(difference) / 12;
                    if (time == 1) lastPlayedTV.setText(time + " month ago");
                    else lastPlayedTV.setText(time + " months ago");
                } else {
                    time = TimeUnit.SECONDS.toDays(difference) / 365;
                    if (time == 1) lastPlayedTV.setText(time + " year ago");
                    else lastPlayedTV.setText(time + " years ago");
                }
            }

            return convertView;
        }
    }

}
