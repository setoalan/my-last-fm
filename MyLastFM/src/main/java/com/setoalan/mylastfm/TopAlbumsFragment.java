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

    public static ArrayList<Album> WEEK_ALBUMS, MONTH_ALBUMS, YEAR_ALBUMS, OVERALL_ALBUMS;

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment, mMonthFragment, mYearFragment, mOverallFragment;
    ImageView albumIV;
    TextView albumTV, playCountTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mWeekFragment = new AlbumFragmentTab("7day");
        mMonthFragment = new AlbumFragmentTab("1month");
        mYearFragment = new AlbumFragmentTab("12month");
        mOverallFragment = new AlbumFragmentTab("overall");

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_container, container, false);
        return view;
    }

    private class TopAlbumsAdapter extends ArrayAdapter<Album> {

        private String mPeriod;


        public TopAlbumsAdapter(ArrayList<Album> data, String time) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
            mPeriod = time;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_main, null);
            }

            Album album = null;
            if (mPeriod.equals("week")) {
                album = WEEK_ALBUMS.get(position);
            } else if (mPeriod.equals("month")) {
                album = MONTH_ALBUMS.get(position);
            } else if (mPeriod.equals("year")) {
                album = YEAR_ALBUMS.get(position);
            } else if (mPeriod.equals("overall")) {
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

    public class AlbumFragmentTab extends ListFragment {

        private boolean dataCalled = false;
        private String mPeriod;

        public AlbumFragmentTab(String period) {
            mPeriod = period;
            if (mPeriod.equals("7day")) {
                WEEK_ALBUMS = new ArrayList<Album>();
            } else if (mPeriod.equals("1month")) {
                MONTH_ALBUMS = new ArrayList<Album>();
            } else if (mPeriod.equals("12month")) {
                YEAR_ALBUMS = new ArrayList<Album>();
            } else if (mPeriod.equals("overall")) {
                OVERALL_ALBUMS = new ArrayList<Album>();
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            if (!dataCalled) {
                dataCalled = true;
                new FetchDataTask().execute();
            }
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_top, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchAlbums().fetchAlbums(50, mPeriod);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (mPeriod.equals("7day")) {
                    setListAdapter(new TopAlbumsAdapter(WEEK_ALBUMS, "week"));
                } else if (mPeriod.equals("1month")) {
                    setListAdapter(new TopAlbumsAdapter(MONTH_ALBUMS, "month"));
                } else if (mPeriod.equals("12month")) {
                    setListAdapter(new TopAlbumsAdapter(YEAR_ALBUMS, "year"));
                } else if (mPeriod.equals("overall")) {
                    setListAdapter(new TopAlbumsAdapter(OVERALL_ALBUMS, "overall"));
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
