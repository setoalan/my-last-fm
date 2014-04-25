package com.setoalan.mylastfm;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.setoalan.mylastfm.datastructures.Event;

import java.util.ArrayList;

public class EventFragment extends ListFragment {

    public static Event mEvent;
    public static int mPosition;

    private ArrayList<String> mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<String>();
        for (int i=0; i<mEvent.getArtists().size()+2; i++)
            mList.add("");
        setListAdapter(new EventAdapter(mList));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    private class EventAdapter extends ArrayAdapter<String> {

        Button websiteBTN, phoneBTN, mapBTN;
        ImageView eventIV;
        TextView artistTV, dateTV, headerTV, titleTV, venueTV;

        public EventAdapter(ArrayList<String> data) {
            super(getActivity(), android.R.layout.simple_list_item_1, data);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_event, null);
                eventIV = (ImageView) convertView.findViewById(R.id.image_iv);
                titleTV = (TextView) convertView.findViewById(R.id.title_tv);
                dateTV = (TextView) convertView.findViewById(R.id.date_tv);
                venueTV = (TextView) convertView.findViewById(R.id.venue_tv);

                eventIV.setImageDrawable(mEvent.getImage());
                titleTV.setText(mEvent.getTitle());
                dateTV.setText(mEvent.getStartDate().toString());
                venueTV.setText(mEvent.getVenue());
            } else if (position == 1) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_header, null);
                headerTV = (TextView) convertView.findViewById(R.id.header_tv);
                headerTV.setText("Lineup");
            } else {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_artist, null);
                artistTV = (TextView) convertView.findViewById(R.id.artist_tv);
                artistTV.setText(mEvent.getArtists().get(position - 2));
            }

            return convertView;
        }

    }

}
