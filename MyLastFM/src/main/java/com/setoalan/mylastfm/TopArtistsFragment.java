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

import com.setoalan.mylastfm.activities.ArtistActivity;
import com.setoalan.mylastfm.datastructures.Artist;
import com.setoalan.mylastfm.fetchservices.FetchArtists;

import java.util.ArrayList;

public class TopArtistsFragment extends Fragment {

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment, mMonthFragment, mYearFragment, mOverallFragment;

    ImageView artistIV;
    TextView artistTV, playCountTV, rankTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if (actionBar.getTabCount() != 0)
            actionBar.removeAllTabs();

        mWeekFragment = new ArtistFragmentTab("7day");
        mMonthFragment = new ArtistFragmentTab("1month");
        mYearFragment = new ArtistFragmentTab("12month");
        mOverallFragment = new ArtistFragmentTab("overall");

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

    private class TopArtistsAdapter extends ArrayAdapter<Artist> {

        private String mPeriod;

        public TopArtistsAdapter(ArrayList<Artist> data, String period) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
            mPeriod = period;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_default, null);

            Artist artist = null;
            if (mPeriod.equals("week"))
                artist = MyLastFMActivity.WEEK_ARTISTS.get(position);
            else if (mPeriod.equals("month"))
                artist = MyLastFMActivity.MONTH_ARTISTS.get(position);
            else if (mPeriod.equals("year"))
                artist = MyLastFMActivity.YEAR_ARTISTS.get(position);
            else if (mPeriod.equals("overall"))
                artist = MyLastFMActivity.OVERALL_ARTISTS.get(position);

            artistIV = (ImageView) convertView.findViewById(R.id.image_iv);
            rankTV = (TextView) convertView.findViewById(R.id.rank_tv);
            artistTV = (TextView) convertView.findViewById(R.id.name_tv);
            playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

            artistIV.setImageDrawable(artist.getImage());
            rankTV.setText(artist.getRank() + "");
            artistTV.setText(artist.getName());
            playCountTV.setText(artist.getPlayCount() + " plays");

            return convertView;
        }

    }

    public class ArtistFragmentTab extends ListFragment {

        TopArtistsAdapter mTopArtistsAdapter;

        private boolean dataCalled = false;
        private String mPeriod;

        public ArtistFragmentTab(String period) {
            mPeriod = period;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            if (!dataCalled) {
                dataCalled = true;
                if (mPeriod.equals("7day")) {
                    if (MyLastFMActivity.WEEK_ARTISTS.isEmpty()) {
                        new FetchDataTask().execute();
                    } else {
                        mTopArtistsAdapter = new TopArtistsAdapter(MyLastFMActivity.WEEK_ARTISTS, "week");
                        setListAdapter(mTopArtistsAdapter);
                    }
                } else if (mPeriod.equals("1month")) {
                    if (MyLastFMActivity.MONTH_ARTISTS.isEmpty()) {
                        new FetchDataTask().execute();
                    } else {
                        mTopArtistsAdapter = new TopArtistsAdapter(MyLastFMActivity.MONTH_ARTISTS, "month");
                        setListAdapter(mTopArtistsAdapter);
                    }
                } else if (mPeriod.equals("12month")) {
                    if (MyLastFMActivity.YEAR_ARTISTS.isEmpty()) {
                        new FetchDataTask().execute();
                    } else {
                        mTopArtistsAdapter = new TopArtistsAdapter(MyLastFMActivity.YEAR_ARTISTS, "year");
                        setListAdapter(mTopArtistsAdapter);
                    }
                } else if (mPeriod.equals("overall")) {
                    if (MyLastFMActivity.OVERALL_ARTISTS.isEmpty()) {
                        new FetchDataTask().execute();
                    } else {
                        mTopArtistsAdapter = new TopArtistsAdapter(MyLastFMActivity.OVERALL_ARTISTS, "overall");
                        setListAdapter(mTopArtistsAdapter);
                    }
                }
            }
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            if (getActivity().getActionBar().getSelectedTab().getPosition() == 0)
                ArtistFragment.mArtist = MyLastFMActivity.WEEK_ARTISTS.get(position);
            else if (getActivity().getActionBar().getSelectedTab().getPosition() == 1)
                ArtistFragment.mArtist = MyLastFMActivity.MONTH_ARTISTS.get(position);
            else if (getActivity().getActionBar().getSelectedTab().getPosition() == 2)
                ArtistFragment.mArtist = MyLastFMActivity.YEAR_ARTISTS.get(position);
            else if (getActivity().getActionBar().getSelectedTab().getPosition() == 3)
                ArtistFragment.mArtist = MyLastFMActivity.OVERALL_ARTISTS.get(position);
            startActivity(new Intent(getActivity(), ArtistActivity.class));
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchArtists().fetchArtists(50, mPeriod);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (isVisible()) {
                    if (mPeriod.equals("7day"))
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.WEEK_ARTISTS, "week"));
                    else if (mPeriod.equals("1month"))
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.MONTH_ARTISTS, "month"));
                    else if (mPeriod.equals("12month"))
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.YEAR_ARTISTS, "year"));
                    else if (mPeriod.equals("overall"))
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.OVERALL_ARTISTS, "overall"));
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
