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

    public static ArrayList<Artist> WEEK_ARTISTS, MONTH_ARTISTS, YEAR_ARTISTS, OVERALL_ARTISTS;

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment, mMonthFragment, mYearFragment, mOverallFragment;
    ImageView artistIV;
    TextView artistTV, playCountTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_container, container, false);
        return view;
    }

    private class TopArtistsAdapter extends ArrayAdapter<Artist> {

        private String mPeriod;

        public TopArtistsAdapter(ArrayList<Artist> data, String time) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
            mPeriod = time;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_main, null);
            }

            Artist artist = null;
            if (mPeriod.equals("week")) {
                artist = WEEK_ARTISTS.get(position);
            } else if (mPeriod.equals("month")) {
                artist = MONTH_ARTISTS.get(position);
            } else if (mPeriod.equals("year")) {
                artist = YEAR_ARTISTS.get(position);
            } else if (mPeriod.equals("overall")) {
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

    public class ArtistFragmentTab extends ListFragment {

        private boolean dataCalled = false;
        private String mPeriod;

        public ArtistFragmentTab(String period) {
            mPeriod = period;
            if (mPeriod.equals("7day")) {
                WEEK_ARTISTS = new ArrayList<Artist>();
            } else if (mPeriod.equals("1month")) {
                MONTH_ARTISTS = new ArrayList<Artist>();
            } else if (mPeriod.equals("12month")) {
                YEAR_ARTISTS = new ArrayList<Artist>();
            } else if (mPeriod.equals("overall")) {
                OVERALL_ARTISTS = new ArrayList<Artist>();
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
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top, container, false);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchArtists().fetchArtists(50, mPeriod);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (mPeriod.equals("7day")) {
                    setListAdapter(new TopArtistsAdapter(WEEK_ARTISTS, "week"));
                } else if (mPeriod.equals("1month")) {
                    setListAdapter(new TopArtistsAdapter(MONTH_ARTISTS, "month"));
                } else if (mPeriod.equals("12month")) {
                    setListAdapter(new TopArtistsAdapter(YEAR_ARTISTS, "year"));
                } else if (mPeriod.equals("overall")) {
                    setListAdapter(new TopArtistsAdapter(OVERALL_ARTISTS, "overall"));
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
