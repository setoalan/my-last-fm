package com.setoalan.mylastfm;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Track;
import com.setoalan.mylastfm.fetchservices.FetchTracks;

import java.util.ArrayList;

public class TopTracksFragment extends Fragment {

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment = new WeekFragmentTab();
    Fragment mMonthFragment = new MonthFragmentTab();
    Fragment mYearFragment = new YearFragmentTab();
    Fragment mOverallFragment = new OverallFragmentTab();

    public static ArrayList<Track> WEEK_TRACKS;
    public static ArrayList<Track> MONTH_TRACKS;
    public static ArrayList<Track> YEAR_TRACKS;
    public static ArrayList<Track> OVERALL_TRACKS;

    ImageView albumIV;
    TextView artistTV, playCountTV, trackTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mWeekTab = actionBar.newTab().setText("Week");
        mMonthTab = actionBar.newTab().setText("Month");
        mYearTab = actionBar.newTab().setText("Year");
        mOverallTab = actionBar.newTab().setText("Overall");

        mWeekTab.setTabListener(new MyTabListener(mWeekFragment));
        mMonthTab.setTabListener(new MyTabListener(mMonthFragment));
        mYearTab.setTabListener(new MyTabListener(mYearFragment));
        mOverallTab.setTabListener(new MyTabListener(mOverallFragment));

        new WeekFragmentTab();
        new MonthFragmentTab();
        new YearFragmentTab();
        new OverallFragmentTab();

        actionBar.addTab(mWeekTab);
        actionBar.addTab(mMonthTab);
        actionBar.addTab(mYearTab);
        actionBar.addTab(mOverallTab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_container, container, false);
        return view;
    }

    private class TopTracksAdapter extends ArrayAdapter<Track> {

        String mTime;

        public TopTracksAdapter(ArrayList<Track> data, String time) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
            mTime = time;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_recent_tracks, null);
            }

            Track track = null;
            if (mTime.equals("week")) {
                track = WEEK_TRACKS.get(position);
            } else if (mTime.equals("month")) {
                track = MONTH_TRACKS.get(position);
            } else if (mTime.equals("year")) {
                track = YEAR_TRACKS.get(position);
            } else if (mTime.equals("overall")) {
                track = OVERALL_TRACKS.get(position);
            }

            albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
            artistTV = (TextView) convertView.findViewById(R.id.name_tv);
            trackTV = (TextView) convertView.findViewById(R.id.track_tv);
            playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

            albumIV.setImageDrawable(track.getImage());
            artistTV.setText(track.getArtist());
            trackTV.setText(track.getName());
            playCountTV.setText(track.getPlayCount() + " plays");

            return convertView;
        }

    }

    public class WeekFragmentTab extends ListFragment {

        public WeekFragmentTab() {
            WEEK_TRACKS = new ArrayList<Track>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchTracks().fetchTracks(50, "7day");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopTracksAdapter(WEEK_TRACKS, "week"));
            }

        }

    }

    public class MonthFragmentTab extends ListFragment {

        public MonthFragmentTab() {
            MONTH_TRACKS = new ArrayList<Track>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchTracks().fetchTracks(50, "1month");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopTracksAdapter(MONTH_TRACKS, "month"));
            }

        }

    }

    public class YearFragmentTab extends ListFragment {

        public YearFragmentTab() {
            YEAR_TRACKS = new ArrayList<Track>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchTracks().fetchTracks(50, "12month");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopTracksAdapter(YEAR_TRACKS, "year"));
            }

        }

    }

    public class OverallFragmentTab extends ListFragment {

        public OverallFragmentTab() {
            OVERALL_TRACKS = new ArrayList<Track>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchTracks().fetchTracks(50, "overall");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopTracksAdapter(OVERALL_TRACKS, "overall"));
            }

        }

    }

    public class MyTabListener implements ActionBar.TabListener {

        Fragment fragment;

        public MyTabListener(Fragment fragment) {
            this.fragment = fragment;
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.remove(fragment);
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

    }

}
