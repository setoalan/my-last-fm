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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Album;
import com.setoalan.mylastfm.datastructures.Artist;
import com.setoalan.mylastfm.datastructures.Track;
import com.setoalan.mylastfm.fetchservices.FetchArtistInfo;
import com.setoalan.mylastfm.fetchservices.FetchArtistPopular;
import com.setoalan.mylastfm.fetchservices.FetchArtistSimilar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ArtistFragment extends Fragment {

    public static Artist mArtist;

    ActionBar.Tab mInfoTab, mPopularTab, mBioTab, mSimilarTab;
    Fragment mInfoFragment, mPopularFragment, mBioFragment, mSimilarFragment;
    View loadingV;

    public ArtistFragment() {
        mArtist = TopArtistsFragment.artist;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mArtist.getName());

        mInfoFragment = new InfoFragmentTab();
        mPopularFragment = new PopularFragmentTab();
        mBioFragment = new BioFragmentTab();
        mSimilarFragment = new SimilarFragmentTab();

        mInfoTab = actionBar.newTab().setText("Info");
        mPopularTab = actionBar.newTab().setText("Popular");
        mBioTab = actionBar.newTab().setText("Bio");
        mSimilarTab = actionBar.newTab().setText("Similar");

        mInfoTab.setTabListener(new MyTabListener(mInfoFragment));
        mPopularTab.setTabListener(new MyTabListener(mPopularFragment));
        mBioTab.setTabListener(new MyTabListener(mBioFragment));
        mSimilarTab.setTabListener(new MyTabListener(mSimilarFragment));

        actionBar.addTab(mInfoTab);
        actionBar.addTab(mPopularTab);
        actionBar.addTab(mBioTab);
        actionBar.addTab(mSimilarTab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        return view;
    }

    public class InfoFragmentTab extends Fragment {

        ImageView artistIV;
        TextView artistTV, playsTV, listenersTV;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
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
                new FetchArtistInfo().fetchArtistInfo();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadingV.setVisibility(View.INVISIBLE);
                artistIV.setImageDrawable(mArtist.getLargeImage());
                playsTV.setText(NumberFormat.getNumberInstance(Locale.US)
                        .format(mArtist.getPlays()) + " PLAYS");
                listenersTV.setText(NumberFormat.getNumberInstance(Locale.US)
                        .format(mArtist.getListeners()) + " LISTENERS");
                artistTV.setText(mArtist.getName());
            }

        }

    }

    public class PopularFragmentTab extends ListFragment {

        private ArrayList<String> mList;

        ImageView albumIV;
        TextView albumTV, headerTV, playCountTV, trackTV;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchArtistPopular().fetchArtistPopular();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mList = new ArrayList<String>();
                for (int i=0; i<12; i++)
                    mList.add("");
                setListAdapter(new ArtistsPopularAdapter(mList));
            }

        }

        private class ArtistsPopularAdapter extends ArrayAdapter<String> {

            public ArtistsPopularAdapter(ArrayList<String> data) {
                super(getActivity(), android.R.layout.simple_list_item_1, data);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (position == 0) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_header, null);
                    headerTV = (TextView) convertView.findViewById(R.id.header_tv);

                    headerTV.setText("Top Tracks");
                } else if (position == 1 || position == 2 || position == 3 || position == 4 ||
                        position == 5) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_default, null);
                    Track track = mArtist.getTracks().get(position - 1);

                    albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                    trackTV = (TextView) convertView.findViewById(R.id.artist_tv);
                    playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

                    albumIV.setImageDrawable(track.getImage());
                    trackTV.setText(track.getName());
                    playCountTV.setText(NumberFormat.getNumberInstance(Locale.US)
                            .format(track.getPlayCount()) + " plays");
                } else if (position == 6) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_header, null);
                    headerTV = (TextView) convertView.findViewById(R.id.header_tv);

                    headerTV.setText("Top Albums");
                } else if (position == 7 || position == 8 || position == 9 || position == 10 ||
                        position == 11) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_default, null);
                    Album album = mArtist.getAlbums().get(position - 7);
                    albumIV = (ImageView) convertView.findViewById(R.id.image_iv);
                    albumTV = (TextView) convertView.findViewById(R.id.artist_tv);
                    playCountTV = (TextView) convertView.findViewById(R.id.detail_tv);

                    albumIV.setImageDrawable(album.getImage());
                    albumTV.setText(album.getName());
                    playCountTV.setText(NumberFormat.getNumberInstance(Locale.US)
                            .format(album.getPlayCount()) + " plays");
                }

                return convertView;
            }

        }

    }

    public class BioFragmentTab extends Fragment {

        ImageView artistIV;
        TextView bioTV;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_artist_bio, container, false);

            artistIV = (ImageView) view.findViewById(R.id.artist_iv);
            bioTV = (TextView) view.findViewById(R.id.bio_tv);

            artistIV.setImageDrawable(mArtist.getLargeImage());
            bioTV.setText(Html.fromHtml(mArtist.getSummary()));

            return view;
        }

    }

    public class SimilarFragmentTab extends ListFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            new FetchDataTask().execute();
        }

        private class FetchDataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                new FetchArtistSimilar().fetchArtistSimilar();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setListAdapter(new ArtistSimilarAdapter(mArtist.getSimilar()));
            }

        }

        private class ArtistSimilarAdapter extends ArrayAdapter<Artist> {

            ImageView artistIV;
            TextView artistTV;

            public ArtistSimilarAdapter(ArrayList<Artist> data) {
                super(getActivity(), android.R.layout.simple_list_item_1, data);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_detail, null);
                }

                Artist artist = mArtist.getSimilar().get(position);

                artistIV = (ImageView) convertView.findViewById(R.id.image_iv);
                artistTV = (TextView) convertView.findViewById(R.id.name_tv);

                artistIV.setImageDrawable(artist.getImage());
                artistTV.setText(artist.getName());

                return convertView;
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
