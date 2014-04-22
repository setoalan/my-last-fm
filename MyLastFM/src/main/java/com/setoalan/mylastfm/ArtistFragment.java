package com.setoalan.mylastfm;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Artist;
import com.setoalan.mylastfm.fetchservices.FetchArtist;

import java.text.NumberFormat;
import java.util.Locale;

public class ArtistFragment extends Fragment {

    public static Artist mArtist;

    ActionBar.Tab mInfoTab, mPopularTab, mBioTab;
    Fragment mInfoFragment, mPopularFragment, mBioFragment;
    ImageView artistIV;
    TextView artistTV, playsTV, listenersTV;
    View loadingV;

    public ArtistFragment(Artist artist) {
        mArtist = artist;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.removeAllTabs();

        mInfoFragment = new InfoFragmentTab();
        mPopularFragment = new PopularFragmentTab();
        mBioFragment = new BioFragmentTab();

        mInfoTab = actionBar.newTab().setText("Info");
        mPopularTab = actionBar.newTab().setText("Popular");
        mBioTab = actionBar.newTab().setText("Bio");

        mInfoTab.setTabListener(new MyTabListener(mInfoFragment));
        mPopularTab.setTabListener(new MyTabListener(mPopularFragment));
        mBioTab.setTabListener(new MyTabListener(mBioFragment));

        actionBar.addTab(mInfoTab);
        actionBar.addTab(mPopularTab);
        actionBar.addTab(mBioTab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        return view;
    }

    public class InfoFragmentTab extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_artist_info, container, false);

            loadingV = view.findViewById(R.id.loading_container);
            artistIV = (ImageView) view.findViewById(R.id.artist_iv);
            artistTV = (TextView) view.findViewById(R.id.artist_tv);
            playsTV = (TextView) view.findViewById(R.id.plays_tv);
            listenersTV = (TextView) view.findViewById(R.id.listeners_tv);

            new FetchDataTask().execute();

            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                loadingV.setVisibility(View.VISIBLE);
                new FetchArtist().fetchArtist();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadingV.setVisibility(View.INVISIBLE);
                artistIV.setImageDrawable(mArtist.getLargeImage());
                playsTV.setText(NumberFormat.getNumberInstance(Locale.US).format(mArtist.getPlays()) + " PLAYS");
                listenersTV.setText(NumberFormat.getNumberInstance(Locale.US).format(mArtist.getListeners()) + " LISTENERS");
                artistTV.setText(mArtist.getName());
            }

        }

    }

    public class PopularFragmentTab extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_artist_popular, container, false);
            return view;
        }
    }

    public class BioFragmentTab extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_artist_bio, container, false);
            return view;
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
