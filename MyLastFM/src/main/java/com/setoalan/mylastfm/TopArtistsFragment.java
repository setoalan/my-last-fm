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

import com.setoalan.mylastfm.datastructures.Artist;
import com.setoalan.mylastfm.fetchservices.FetchArtists;

import java.util.ArrayList;

public class TopArtistsFragment extends Fragment {

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment = new WeekFragmentTab();
    Fragment mMonthFragment = new MonthFragmentTab();
    Fragment mYearFragment = new YearFragmentTab();
    Fragment mOverallFragment = new OverallFragmentTab();

    public static ArrayList<Artist> WEEK_ARTISTS;
    public static ArrayList<Artist> MONTH_ARTISTS;
    public static ArrayList<Artist> YEAR_ARTISTS;
    public static ArrayList<Artist> OVERALL_ARTISTS;

    ImageView artistIV;
    TextView artistTV, playCountTV;

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
        View view = inflater.inflate(R.layout.fragment_top_artists_container, container, false);
        return view;
    }

    private class TopArtistsAdapter extends ArrayAdapter<Artist> {

        String mTime;

        public TopArtistsAdapter(ArrayList<Artist> data, String time) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
            mTime = time;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_main, null);
            }

            Artist artist = null;
            if (mTime.equals("week")) {
                artist = WEEK_ARTISTS.get(position);
            } else if (mTime.equals("month")) {
                artist = MONTH_ARTISTS.get(position);
            } else if (mTime.equals("year")) {
                artist = YEAR_ARTISTS.get(position);
            } else if (mTime.equals("overall")) {
                artist = OVERALL_ARTISTS.get(position);
            }

            artistIV = (ImageView) convertView.findViewById(R.id.image_iv);
            artistTV = (TextView) convertView.findViewById(R.id.name_tv);
            playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

            artistIV.setImageDrawable(artist.getImage());
            artistTV.setText(artist.getName());
            playCountTV.setText(artist.getPlayCount() + " plays");

            return convertView;
        }

    }

    public class WeekFragmentTab extends ListFragment {

        public WeekFragmentTab() {
            WEEK_ARTISTS = new ArrayList<Artist>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchArtists().fetchArtists(50, "7day");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopArtistsAdapter(WEEK_ARTISTS, "week"));
            }

        }

    }

    public class MonthFragmentTab extends ListFragment {

        public MonthFragmentTab() {
            MONTH_ARTISTS = new ArrayList<Artist>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchArtists().fetchArtists(50, "1month");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopArtistsAdapter(MONTH_ARTISTS, "month"));
            }

        }

    }

    public class YearFragmentTab extends ListFragment {

        public YearFragmentTab() {
            YEAR_ARTISTS = new ArrayList<Artist>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchArtists().fetchArtists(50, "12month");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopArtistsAdapter(YEAR_ARTISTS, "year"));
            }

        }

    }

    public class OverallFragmentTab extends ListFragment {

        public OverallFragmentTab() {
            OVERALL_ARTISTS = new ArrayList<Artist>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchArtists().fetchArtists(50, "overall");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopArtistsAdapter(OVERALL_ARTISTS, "overall"));
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
