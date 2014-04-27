package com.setoalan.mylastfm;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Album;
import com.setoalan.mylastfm.fetchservices.FetchAlbumBio;
import com.setoalan.mylastfm.fetchservices.FetchAlbumInfo;

import java.text.NumberFormat;
import java.util.Locale;

public class AlbumFragment extends Fragment {

    public static Album mAlbum;

    ActionBar.Tab mInfoTab, mTracksTab, mBioTab;
    Fragment mInfoFragment, mTracksFragment, mBioFragment;
    View loadingV;

    public AlbumFragment() {
        mAlbum = TopAlbumsFragment.album;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mAlbum.getName());

        mInfoFragment = new InfoFragmentTab();
        mTracksFragment = new TracksFragmentTab();
        mBioFragment = new BioFragmentTab();

        mInfoTab = actionBar.newTab().setText("Info");
        mTracksTab = actionBar.newTab().setText("Tracks");
        mBioTab = actionBar.newTab().setText("Bio");

        mInfoTab.setTabListener(new MyTabListener(mInfoFragment));
        mTracksTab.setTabListener(new MyTabListener(mTracksFragment));
        mBioTab.setTabListener(new MyTabListener(mBioFragment));

        actionBar.addTab(mInfoTab);
        actionBar.addTab(mTracksTab);
        actionBar.addTab(mBioTab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        return view;
    }

    public class InfoFragmentTab extends Fragment {

        ImageView albumIV;
        TextView albumTV, playsTV, listenersTV;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_info, container, false);

            loadingV = view.findViewById(R.id.loading_container);
            albumIV = (ImageView) view.findViewById(R.id.image_iv);
            albumTV = (TextView) view.findViewById(R.id.name_tv);
            playsTV = (TextView) view.findViewById(R.id.plays_tv);
            listenersTV = (TextView) view.findViewById(R.id.listeners_tv);

            if (AlbumFragment.mAlbum.getLargeImage() == null) {
                loadingV.setVisibility(View.VISIBLE);
                new FetchDataTask().execute();
            } else {
                albumIV.setImageDrawable(AlbumFragment.mAlbum.getLargeImage());
                playsTV.setText(NumberFormat.getNumberInstance(Locale.US)
                        .format(AlbumFragment.mAlbum.getPlays()) + " PLAYS");
                listenersTV.setText(NumberFormat.getNumberInstance(Locale.US)
                        .format(AlbumFragment.mAlbum.getListeners()) + " LISTENERS");
                albumTV.setText(AlbumFragment.mAlbum.getName());
            }

            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchAlbumInfo().fetchAlbumInfo();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadingV.setVisibility(View.INVISIBLE);
                albumIV.setImageDrawable(AlbumFragment.mAlbum.getLargeImage());
                playsTV.setText(NumberFormat.getNumberInstance(Locale.US)
                        .format(AlbumFragment.mAlbum.getPlays()) + " PLAYS");
                listenersTV.setText(NumberFormat.getNumberInstance(Locale.US)
                        .format(AlbumFragment.mAlbum.getListeners()) + " LISTENERS");
                albumTV.setText(AlbumFragment.mAlbum.getName());
            }

        }

    }

    public class TracksFragmentTab extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

    }

    public class BioFragmentTab extends Fragment {

        ImageView albumIV;
        TextView bioTV;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_bio, container, false);

            loadingV = view.findViewById(R.id.loading_container);
            albumIV = (ImageView) view.findViewById(R.id.artist_iv);
            bioTV = (TextView) view.findViewById(R.id.biography_tv);

            if (mAlbum.getLargeImage() == null) {
                loadingV.setVisibility(View.VISIBLE);
                new FetchDataTask().execute();
            } else {
                albumIV.setImageDrawable(mAlbum.getLargeImage());
                if (mAlbum.getSummary() != null) {
                    bioTV.setText(Html.fromHtml(mAlbum.getSummary()));
                    bioTV.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }

            return view;
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchAlbumBio().fetchAlbumBio();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadingV.setVisibility(View.INVISIBLE);
                albumIV.setImageDrawable(mAlbum.getLargeImage());
                if (mAlbum.getSummary() != null) {
                    bioTV.setText(Html.fromHtml(mAlbum.getSummary()));
                    bioTV.setMovementMethod(LinkMovementMethod.getInstance());
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
