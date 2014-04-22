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
import android.widget.ListView;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Artist;
import com.setoalan.mylastfm.fetchservices.FetchArtists;

import java.util.ArrayList;

public class TopArtistsFragment extends Fragment {

    ActionBar.Tab mWeekTab, mMonthTab, mYearTab, mOverallTab;
    Fragment mWeekFragment, mMonthFragment, mYearFragment, mOverallFragment;
    FragmentTransaction fragmentTransaction;
    ImageView artistIV;
    TextView artistTV, playCountTV, rankTV;
    View loadingV;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_default, null);
            }

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
            artistTV = (TextView) convertView.findViewById(R.id.artist_tv);
            playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

            artistIV.setImageDrawable(artist.getImage());
            rankTV.setText(artist.getRank() + "");
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
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            if (!dataCalled) {
                dataCalled = true;
                if (mPeriod.equals("7day")) {
                    if (MyLastFMActivity.WEEK_ARTISTS.isEmpty())
                        new FetchDataTask().execute();
                    else
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.WEEK_ARTISTS,
                                "week"));
                } else if (mPeriod.equals("1month")) {
                    if (MyLastFMActivity.MONTH_ARTISTS.isEmpty())
                        new FetchDataTask().execute();
                    else
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.MONTH_ARTISTS,
                                "month"));
                } else if (mPeriod.equals("12month")) {
                    if (MyLastFMActivity.YEAR_ARTISTS.isEmpty())
                        new FetchDataTask().execute();
                    else
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.YEAR_ARTISTS,
                                "year"));
                } else if (mPeriod.equals("overall")) {
                    if (MyLastFMActivity.OVERALL_ARTISTS.isEmpty())
                        new FetchDataTask().execute();
                    else
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.OVERALL_ARTISTS,
                                "overall"));
                }
            }
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_list, container, false);
            loadingV = view.findViewById(R.id.loading_container);
            if (getActivity().getActionBar().getTabCount() == 0) {
                getActivity().getActionBar().addTab(mWeekTab);
                getActivity().getActionBar().addTab(mMonthTab);
                getActivity().getActionBar().addTab(mYearTab);
                getActivity().getActionBar().addTab(mOverallTab);
            }
            if (getActivity().getActionBar().getSelectedTab().getPosition() == 0) {
                if (MyLastFMActivity.WEEK_ARTISTS.isEmpty())
                    loadingV.setVisibility(View.VISIBLE);
            } else if (getActivity().getActionBar().getSelectedTab().getPosition() == 1) {
                if (MyLastFMActivity.MONTH_ARTISTS.isEmpty())
                    loadingV.setVisibility(View.VISIBLE);
            } else if (getActivity().getActionBar().getSelectedTab().getPosition() == 2) {
                if (MyLastFMActivity.YEAR_ARTISTS.isEmpty())
                    loadingV.setVisibility(View.VISIBLE);
            } else if (getActivity().getActionBar().getSelectedTab().getPosition() == 3) {
                if (MyLastFMActivity.OVERALL_ARTISTS.isEmpty())
                    loadingV.setVisibility(View.VISIBLE);
            }
            return view;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Artist artist = MyLastFMActivity.WEEK_ARTISTS.get(position);
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_right,
                    R.animator.slide_out_right,
                    R.animator.slide_out_left,
                    R.animator.slide_in_left)
                    .replace(R.id.fragment_container, new ArtistFragment(artist))
                    .addToBackStack(null)
                    .commit();
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
                loadingV.setVisibility(View.INVISIBLE);
                if (isVisible()) {
                    if (mPeriod.equals("7day"))
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.WEEK_ARTISTS, "week"));
                    else if (mPeriod.equals("1month"))
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.MONTH_ARTISTS, "month"));
                    else if (mPeriod.equals("12month"))
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.YEAR_ARTISTS, "year"));
                    else if (mPeriod.equals("overall"))
                        setListAdapter(new TopArtistsAdapter(MyLastFMActivity.OVERALL_ARTISTS,
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
