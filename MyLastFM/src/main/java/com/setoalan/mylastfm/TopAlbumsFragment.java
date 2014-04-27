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

import com.setoalan.mylastfm.activities.AlbumActivity;
import com.setoalan.mylastfm.datastructures.Album;
import com.setoalan.mylastfm.fetchservices.FetchAlbums;

import java.util.ArrayList;

public class TopAlbumsFragment extends Fragment {

    public static Album album;

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment, mMonthFragment, mYearFragment, mOverallFragment;
    ImageView albumIV;
    TextView albumTV, artistTV, playCountTV, rankTV;
    View loadingV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if (actionBar.getTabCount() != 0)
            actionBar.removeAllTabs();

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
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        return view;
    }

    private class TopAlbumsAdapter extends ArrayAdapter<Album> {

        private String mPeriod;


        public TopAlbumsAdapter(ArrayList<Album> data, String period) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
            mPeriod = period;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_detail, null);
            }

            Album album = null;
            if (mPeriod.equals("week"))
                album = MyLastFMActivity.WEEK_ALBUMS.get(position);
            else if (mPeriod.equals("month"))
                album =  MyLastFMActivity.MONTH_ALBUMS.get(position);
            else if (mPeriod.equals("year"))
                album =  MyLastFMActivity.YEAR_ALBUMS.get(position);
            else if (mPeriod.equals("overall"))
                album =  MyLastFMActivity.OVERALL_ALBUMS.get(position);

            albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
            rankTV = (TextView) convertView.findViewById(R.id.rank_tv);
            artistTV = (TextView) convertView.findViewById(R.id.artist_tv);
            albumTV = (TextView) convertView.findViewById(R.id.name_tv);
            playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

            albumIV.setImageDrawable(album.getImage());
            rankTV.setText(album.getRank() + "");
            artistTV.setText(album.getArtist());
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
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            if (!dataCalled) {
                dataCalled = true;
                if (mPeriod.equals("7day")) {
                    if (MyLastFMActivity.WEEK_ALBUMS.isEmpty())
                        new FetchDataTask().execute();
                    else
                        setListAdapter(new TopAlbumsAdapter(MyLastFMActivity.WEEK_ALBUMS, "week"));
                } else if (mPeriod.equals("1month")) {
                    if (MyLastFMActivity.MONTH_ALBUMS.isEmpty())
                        new FetchDataTask().execute();
                    else
                        setListAdapter(new TopAlbumsAdapter(MyLastFMActivity.MONTH_ALBUMS,
                                "month"));
                } else if (mPeriod.equals("12month")) {
                    if (MyLastFMActivity.YEAR_ALBUMS.isEmpty())
                        new FetchDataTask().execute();
                    else
                        setListAdapter(new TopAlbumsAdapter(MyLastFMActivity.YEAR_ALBUMS, "year"));
                } else if (mPeriod.equals("overall")) {
                    if (MyLastFMActivity.OVERALL_ALBUMS.isEmpty())
                        new FetchDataTask().execute();
                    else
                        setListAdapter(new TopAlbumsAdapter(MyLastFMActivity.OVERALL_ALBUMS,
                                "overall"));
                }
            }
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_list, container, false);
            loadingV = view.findViewById(R.id.loading_container);
            if (getActivity().getActionBar().getSelectedTab().getPosition() == 0) {
                if (MyLastFMActivity.WEEK_ALBUMS.isEmpty())
                    loadingV.setVisibility(View.VISIBLE);
            } else if (getActivity().getActionBar().getSelectedTab().getPosition() == 1) {
                if (MyLastFMActivity.MONTH_ALBUMS.isEmpty())
                    loadingV.setVisibility(View.VISIBLE);
            } else if (getActivity().getActionBar().getSelectedTab().getPosition() == 2) {
                if (MyLastFMActivity.YEAR_ALBUMS.isEmpty())
                    loadingV.setVisibility(View.VISIBLE);
            } else if (getActivity().getActionBar().getSelectedTab().getPosition() == 3) {
                if (MyLastFMActivity.OVERALL_ALBUMS.isEmpty())
                    loadingV.setVisibility(View.VISIBLE);
            }
            return view;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            if (getActivity().getActionBar().getSelectedTab().getPosition() == 0)
                album = MyLastFMActivity.WEEK_ALBUMS.get(position);
            else if (getActivity().getActionBar().getSelectedTab().getPosition() == 1)
                album = MyLastFMActivity.MONTH_ALBUMS.get(position);
            else if (getActivity().getActionBar().getSelectedTab().getPosition() == 2)
                album = MyLastFMActivity.YEAR_ALBUMS.get(position);
            else if (getActivity().getActionBar().getSelectedTab().getPosition() == 3)
                album = MyLastFMActivity.OVERALL_ALBUMS.get(position);
            startActivity(new Intent(getActivity(), AlbumActivity.class));
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
                loadingV.setVisibility(View.INVISIBLE);
                if (isVisible()) {
                    if (mPeriod.equals("7day"))
                        setListAdapter(new TopAlbumsAdapter(MyLastFMActivity.WEEK_ALBUMS, "week"));
                    else if (mPeriod.equals("1month"))
                        setListAdapter(new TopAlbumsAdapter(MyLastFMActivity.MONTH_ALBUMS, "month"));
                    else if (mPeriod.equals("12month"))
                        setListAdapter(new TopAlbumsAdapter(MyLastFMActivity.YEAR_ALBUMS, "year"));
                    else if (mPeriod.equals("overall"))
                        setListAdapter(new TopAlbumsAdapter(MyLastFMActivity.OVERALL_ALBUMS,
                                "overall"));
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
