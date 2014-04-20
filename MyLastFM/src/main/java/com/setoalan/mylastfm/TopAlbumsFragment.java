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

import com.setoalan.mylastfm.datastructures.Album;
import com.setoalan.mylastfm.fetchservices.FetchAlbums;

import java.util.ArrayList;

public class TopAlbumsFragment extends Fragment {

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment = new WeekFragmentTab();
    Fragment mMonthFragment = new MonthFragmentTab();
    Fragment mYearFragment = new YearFragmentTab();
    Fragment mOverallFragment = new OverallFragmentTab();

    public static ArrayList<Album> WEEK_ALBUMS;
    public static ArrayList<Album> MONTH_ALBUMS;
    public static ArrayList<Album> YEAR_ALBUMS;
    public static ArrayList<Album> OVERALL_ALBUMS;

    ImageView albumIV;
    TextView albumTV, playCountTV;

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

    private class TopAlbumsAdapter extends ArrayAdapter<Album> {

        String mTime;

        public TopAlbumsAdapter(ArrayList<Album> data, String time) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
            mTime = time;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_main, null);
            }

            Album album = null;
            if (mTime.equals("week")) {
                album = WEEK_ALBUMS.get(position);
            } else if (mTime.equals("month")) {
                album = MONTH_ALBUMS.get(position);
            } else if (mTime.equals("year")) {
                album = YEAR_ALBUMS.get(position);
            } else if (mTime.equals("overall")) {
                album = OVERALL_ALBUMS.get(position);
            }

            albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
            albumTV = (TextView) convertView.findViewById(R.id.name_tv);
            playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

            albumIV.setImageDrawable(album.getImage());
            albumTV.setText(album.getName());
            playCountTV.setText(album.getPlayCount() + " plays");

            return convertView;
        }

    }

    public class WeekFragmentTab extends ListFragment {

        public WeekFragmentTab() {
            WEEK_ALBUMS = new ArrayList<Album>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            if (WEEK_ALBUMS.size() == 0)
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
                new FetchAlbums().fetchAlbums(50, "7day");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopAlbumsAdapter(WEEK_ALBUMS, "week"));
            }

        }

    }

    public class MonthFragmentTab extends ListFragment {

        public MonthFragmentTab() {
            MONTH_ALBUMS = new ArrayList<Album>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            if (MONTH_ALBUMS.size() == 0)
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
                new FetchAlbums().fetchAlbums(50, "1month");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopAlbumsAdapter(MONTH_ALBUMS, "month"));
            }

        }

    }

    public class YearFragmentTab extends ListFragment {

        public YearFragmentTab() {
            YEAR_ALBUMS = new ArrayList<Album>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            if (YEAR_ALBUMS.size() == 0)
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
                new FetchAlbums().fetchAlbums(50, "12month");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopAlbumsAdapter(YEAR_ALBUMS, "year"));
            }

        }

    }

    public class OverallFragmentTab extends ListFragment {

        public OverallFragmentTab() {
            OVERALL_ALBUMS = new ArrayList<Album>();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            if (OVERALL_ALBUMS.size() == 0)
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
                new FetchAlbums().fetchAlbums(50, "overall");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new TopAlbumsAdapter(OVERALL_ALBUMS, "overall"));
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
