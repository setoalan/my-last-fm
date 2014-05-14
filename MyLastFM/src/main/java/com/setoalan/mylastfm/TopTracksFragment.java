package com.setoalan.mylastfm;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.setoalan.mylastfm.activities.TrackActivity;
import com.setoalan.mylastfm.datastructures.Track;
import com.setoalan.mylastfm.fetchservices.FetchTracks;

import java.util.ArrayList;

public class TopTracksFragment extends Fragment {

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment, mMonthFragment, mYearFragment, mOverallFragment;
    ImageView albumIV;
    TextView artistTV, playCountTV, rankTV, trackTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if (actionBar.getTabCount() != 0)
            actionBar.removeAllTabs();

        mWeekFragment = new TrackFragmentTab("7day");
        mMonthFragment = new TrackFragmentTab("1month");
        mYearFragment = new TrackFragmentTab("12month");
        mOverallFragment = new TrackFragmentTab("overall");

        mWeekTab = actionBar.newTab().setText("Week");
        mMonthTab = actionBar.newTab().setText("Month");
        mYearTab = actionBar.newTab().setText("Year");
        mOverallTab = actionBar.newTab().setText("Overall");

        mWeekTab.setTabListener(new MyTabListener(mWeekFragment));
        mMonthTab.setTabListener(new MyTabListener(mMonthFragment));
        mYearTab.setTabListener(new MyTabListener(mYearFragment));
        mOverallTab.setTabListener(new MyTabListener(mOverallFragment));

        actionBar.addTab(mWeekTab);
        actionBar.addTab(mMonthTab);
        actionBar.addTab(mYearTab);
        actionBar.addTab(mOverallTab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        return view;
    }

    private class TopTracksAdapter extends ArrayAdapter<Track> {

        private String mPeriod;

        public TopTracksAdapter(ArrayList<Track> data, String period) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
            mPeriod = period;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_detail, null);

            Track track = null;
            if (mPeriod.equals("week"))
                track = MyLastFMActivity.WEEK_TRACKS.get(position);
            else if (mPeriod.equals("month"))
                track = MyLastFMActivity.MONTH_TRACKS.get(position);
            else if (mPeriod.equals("year"))
                track = MyLastFMActivity.YEAR_TRACKS.get(position);
            else if (mPeriod.equals("overall"))
                track = MyLastFMActivity.OVERALL_TRACKS.get(position);

            albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
            rankTV = (TextView) convertView.findViewById(R.id.rank_tv);
            artistTV = (TextView) convertView.findViewById(R.id.artist_tv);
            trackTV = (TextView) convertView.findViewById(R.id.name_tv);
            playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

            albumIV.setImageDrawable(track.getImage());
            rankTV.setText(track.getRank() + "");
            artistTV.setText(track.getArtist());
            trackTV.setText(track.getName());
            playCountTV.setText(track.getPlayCount() + " plays");

            return convertView;
        }

    }

    public class TrackFragmentTab extends ListFragment {

        TopTracksAdapter mTopTracksAdapter;

        private boolean dataCalled = false;
        private String mPeriod;

        public TrackFragmentTab(String period) {
            mPeriod = period;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            if (!dataCalled) {
                dataCalled = true;
                if (mPeriod.equals("7day")) {
                    if (MyLastFMActivity.WEEK_TRACKS.isEmpty()) {
                        new FetchDataTask().execute();
                    } else {
                        mTopTracksAdapter = new TopTracksAdapter(MyLastFMActivity.WEEK_TRACKS, "week");
                        setListAdapter(mTopTracksAdapter);
                    }
                } else if (mPeriod.equals("1month")) {
                    if (MyLastFMActivity.MONTH_TRACKS.isEmpty()) {
                        new FetchDataTask().execute();
                    } else {
                        mTopTracksAdapter = new TopTracksAdapter(MyLastFMActivity.MONTH_TRACKS, "month");
                        setListAdapter(mTopTracksAdapter);
                    }
                } else if (mPeriod.equals("12month")) {
                    if (MyLastFMActivity.YEAR_TRACKS.isEmpty()) {
                        new FetchDataTask().execute();
                    } else {
                        mTopTracksAdapter = new TopTracksAdapter(MyLastFMActivity.YEAR_TRACKS, "year");
                        setListAdapter(mTopTracksAdapter);
                    }
                } else if (mPeriod.equals("overall")) {
                    if (MyLastFMActivity.OVERALL_TRACKS.isEmpty()) {
                        new FetchDataTask().execute();
                    } else {
                        mTopTracksAdapter = new TopTracksAdapter(MyLastFMActivity.OVERALL_TRACKS, "overall");
                        setListAdapter(mTopTracksAdapter);
                    }
                }
            }
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            if (getActivity().getActionBar().getSelectedTab().getPosition() == 0)
                TrackFragment.mTrack = MyLastFMActivity.WEEK_TRACKS.get(position);
            else if (getActivity().getActionBar().getSelectedTab().getPosition() == 1)
                TrackFragment.mTrack = MyLastFMActivity.MONTH_TRACKS.get(position);
            else if (getActivity().getActionBar().getSelectedTab().getPosition() == 2)
                TrackFragment.mTrack = MyLastFMActivity.YEAR_TRACKS.get(position);
            else if (getActivity().getActionBar().getSelectedTab().getPosition() == 3)
                TrackFragment.mTrack  = MyLastFMActivity.OVERALL_TRACKS.get(position);
            startActivity(new Intent(getActivity(), TrackActivity.class));
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchTracks().fetchTracks(50, mPeriod);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (isVisible()) {
                    if (mPeriod.equals("7day"))
                        setListAdapter(new TopTracksAdapter(MyLastFMActivity.WEEK_TRACKS, "week"));
                    else if (mPeriod.equals("1month"))
                        setListAdapter(new TopTracksAdapter(MyLastFMActivity.MONTH_TRACKS, "month"));
                    else if (mPeriod.equals("12month"))
                        setListAdapter(new TopTracksAdapter(MyLastFMActivity.YEAR_TRACKS, "year"));
                    else if (mPeriod.equals("overall"))
                        setListAdapter(new TopTracksAdapter(MyLastFMActivity.OVERALL_TRACKS, "overall"));
                }
            }

        }

    }

    public class MyTabListener implements ActionBar.TabListener {

        Fragment mFragment;

        public MyTabListener(Fragment fragment) {
            mFragment = fragment;
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.replace(R.id.fragment_container, mFragment);
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.remove(mFragment);
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

    }

}
