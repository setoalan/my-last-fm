package com.setoalan.mylastfm;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Album;
import com.setoalan.mylastfm.datastructures.Track;
import com.setoalan.mylastfm.fetchservices.FetchAlbumBio;
import com.setoalan.mylastfm.fetchservices.FetchAlbumInfo;
import com.setoalan.mylastfm.fetchservices.FetchAlbumTracks;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
            playsTV = (TextView) view.findViewById(R.id.plays_tv);
            listenersTV = (TextView) view.findViewById(R.id.listeners_tv);
            albumTV = (TextView) view.findViewById(R.id.name_tv);

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

    public class TracksFragmentTab extends ListFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (mAlbum.getTracks().isEmpty())
                new FetchDataTask().execute();
            else
                setListAdapter(new ArtistSimilarAdapter(mAlbum.getTracks()));
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchAlbumTracks().fetchAlbumTracks();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isVisible())
                    setListAdapter(new ArtistSimilarAdapter(mAlbum.getTracks()));
            }

        }

        private class ArtistSimilarAdapter extends ArrayAdapter<Track> {

            ImageView albumIV;
            TextView durationTV, rankTV, trackTV;

            public ArtistSimilarAdapter(ArrayList<Track> data) {
                super(getActivity(), android.R.layout.simple_list_item_1, data);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_default, null);
                }

                Track track = mAlbum.getTracks().get(position);

                albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                rankTV = (TextView) convertView.findViewById(R.id.rank_tv);
                trackTV = (TextView) convertView.findViewById(R.id.name_tv);
                durationTV = (TextView) convertView.findViewById(R.id.detail_tv);

                albumIV.setImageDrawable(track.getImage());
                rankTV.setText(track.getRank() + "");
                trackTV.setText(track.getName());
                long minute =  TimeUnit.SECONDS.toMinutes(track.getDuration());
                if ((track.getDuration() % 60) < 10)
                    durationTV.setText(minute + ":0" + track.getDuration() % 60);
                else
                    durationTV.setText(minute + ":" + track.getDuration() % 60);

                return convertView;
            }

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
