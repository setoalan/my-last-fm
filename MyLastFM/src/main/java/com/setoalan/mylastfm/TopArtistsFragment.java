package com.setoalan.mylastfm;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TopArtistsFragment extends Fragment {

    ActionBar.Tab mTab, mTab2, mTab3, mTab4, mTab5;
    Fragment mFragment1 = new FragmentTab1();
    Fragment mFragment2 = new FragmentTab2();
    Fragment mFragment3 = new FragmentTab3();
    Fragment mFragment4 = new FragmentTab4();
    Fragment mFragment5 = new FragmentTab5();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTab = actionBar.newTab().setText("1");
        mTab2 = actionBar.newTab().setText("2");
        mTab3 = actionBar.newTab().setText("3");
        mTab4 = actionBar.newTab().setText("4");
        mTab5 = actionBar.newTab().setText("5");

        mTab.setTabListener(new MyTabListener(mFragment1));
        mTab2.setTabListener(new MyTabListener(mFragment2));
        mTab3.setTabListener(new MyTabListener(mFragment3));
        mTab4.setTabListener(new MyTabListener(mFragment4));
        mTab5.setTabListener(new MyTabListener(mFragment5));

        actionBar.addTab(mTab);
        actionBar.addTab(mTab2);
        actionBar.addTab(mTab3);
        actionBar.addTab(mTab4);
        actionBar.addTab(mTab5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_artists_container, container, false);
        return view;
    }

    public class FragmentTab1 extends Fragment {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
            return view;
        }

    }

    public class FragmentTab2 extends Fragment {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
            return view;
        }

    }

    public class FragmentTab3 extends Fragment {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
            return view;
        }

    }

    public class FragmentTab4 extends Fragment {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
            return view;
        }

    }

    public class FragmentTab5 extends Fragment {

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
            return view;
        }

    }

    public class MyTabListener implements ActionBar.TabListener {

        Fragment fragment;

        public MyTabListener(Fragment fragment) {
            this.fragment = fragment;
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.remove(fragment);
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

    }

}
