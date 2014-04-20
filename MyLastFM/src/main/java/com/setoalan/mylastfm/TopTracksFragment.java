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

    public static ArrayList<Track> WEEK_TRACKS, MONTH_TRACKS, YEAR_TRACKS, OVERALL_TRACKS;

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment, mMonthFragment, mYearFragment, mOverallFragment;
    ImageView albumIV;
    TextView artistTV, playCountTV, trackTV;
    View loadingV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_container, container, false);
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
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_tracks, null);
            }

            Track track = null;
            if (mPeriod.equals("week")) {
                track = WEEK_TRACKS.get(position);
            } else if (mPeriod.equals("month")) {
                track = MONTH_TRACKS.get(position);
            } else if (mPeriod.equals("year")) {
                track = YEAR_TRACKS.get(position);
            } else if (mPeriod.equals("overall")) {
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

    public class TrackFragmentTab extends ListFragment {

        private boolean dataCalled = false, fetchDone = false;
        private String mPeriod;

        public TrackFragmentTab(String period) {
            mPeriod = period;
            if (mPeriod.equals("7day")) {
                WEEK_TRACKS = new ArrayList<Track>();
            } else if (mPeriod.equals("1month")) {
                MONTH_TRACKS = new ArrayList<Track>();
            } else if (mPeriod.equals("12month")) {
                YEAR_TRACKS = new ArrayList<Track>();
            } else if (mPeriod.equals("overall")) {
                OVERALL_TRACKS = new ArrayList<Track>();
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

        @Override
        public void onResume() {
            super.onResume();
            if (fetchDone) {
                loadingV.setVisibility(View.INVISIBLE);
            } else {
                loadingV.setVisibility(View.VISIBLE);
            }
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top, container, false);
            loadingV = (View) view.findViewById(R.id.loading_container);
            loadingV.setVisibility(View.VISIBLE);
            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchTracks().fetchTracks(50, mPeriod);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadingV.setVisibility(View.INVISIBLE);
                fetchDone = true;
                if (mPeriod.equals("7day")) {
                    setListAdapter(new TopTracksAdapter(WEEK_TRACKS, "week"));
                } else if (mPeriod.equals("1month")) {
                    setListAdapter(new TopTracksAdapter(MONTH_TRACKS, "month"));
                } else if (mPeriod.equals("12month")) {
                    setListAdapter(new TopTracksAdapter(YEAR_TRACKS, "year"));
                } else if (mPeriod.equals("overall")) {
                    setListAdapter(new TopTracksAdapter(OVERALL_TRACKS, "overall"));
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
