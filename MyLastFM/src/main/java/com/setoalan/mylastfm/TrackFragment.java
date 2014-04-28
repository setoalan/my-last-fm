package com.setoalan.mylastfm;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Track;
import com.setoalan.mylastfm.fetchservices.FetchTrackInfo;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TrackFragment extends Fragment {

    public static Track mTrack;

    ImageView artistIV;
    TextView artistTV, playsTV, listenersTV, durationTV, trackNumTV;
    View loadingV;

    public TrackFragment() {
        mTrack = TopTracksFragment.track;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mTrack.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        loadingV = view.findViewById(R.id.loading_container);
        artistIV = (ImageView) view.findViewById(R.id.image_iv);
        artistTV = (TextView) view.findViewById(R.id.name_tv);
        playsTV = (TextView) view.findViewById(R.id.plays_tv);
        listenersTV = (TextView) view.findViewById(R.id.listeners_tv);
        durationTV = (TextView) view.findViewById(R.id.duration_tv);
        trackNumTV = (TextView) view.findViewById(R.id.track_num_tv);

        if (mTrack.getLargeImage() == null) {
            loadingV.setVisibility(View.VISIBLE);
            new FetchDataTask().execute();
        } else {
            artistIV.setImageDrawable(mTrack.getLargeImage());
            playsTV.setText(NumberFormat.getNumberInstance(Locale.US)
                    .format(mTrack.getPlays()) + " PLAYS");
            listenersTV.setText(NumberFormat.getNumberInstance(Locale.US)
                    .format(mTrack.getListeners()) + " LISTENERS");
            long minute =  TimeUnit.MILLISECONDS.toMinutes(mTrack.getDuration());
            if ((mTrack.getDuration() % (60 * 1000)) < 10)
                durationTV.setText(minute + ":0" + (mTrack.getDuration() % (60 * 1000) / 1000));
            else
                durationTV.setText(minute + ":" + (mTrack.getDuration() % (60 * 1000) / 1000));
            trackNumTV.setText("Track No. " + mTrack.getRank());
            artistTV.setText(mTrack.getName());
        }

        return view;
    }

    private class FetchDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            new FetchTrackInfo().fetchTrackInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingV.setVisibility(View.INVISIBLE);
            artistIV.setImageDrawable(mTrack.getLargeImage());
            playsTV.setText(NumberFormat.getNumberInstance(Locale.US)
                    .format(mTrack.getPlays()) + " PLAYS");
            listenersTV.setText(NumberFormat.getNumberInstance(Locale.US)
                    .format(mTrack.getListeners()) + " LISTENERS");
            long minute =  TimeUnit.MILLISECONDS.toMinutes(mTrack.getDuration());
            if ((mTrack.getDuration() % (60 * 1000)) < 10)
                durationTV.setText(minute + ":0" + (mTrack.getDuration() % (60 * 1000) / 1000));
            else
                durationTV.setText(minute + ":" + (mTrack.getDuration() % (60 * 1000) / 1000));
            trackNumTV.setText("Track No. " + mTrack.getRank());
            artistTV.setText(mTrack.getName());
        }

    }

}
