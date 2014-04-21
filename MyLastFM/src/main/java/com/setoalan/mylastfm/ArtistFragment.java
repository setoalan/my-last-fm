package com.setoalan.mylastfm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Artist;

public class ArtistFragment extends Fragment {

    Artist mArtist;

    public ArtistFragment(Artist artist) {
        mArtist = artist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artist, container, false);
        getActivity().getActionBar().removeAllTabs();
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(mArtist.getName());
        return v;
    }

}
