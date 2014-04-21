package com.setoalan.mylastfm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.setoalan.mylastfm.datastructures.Album;
import com.setoalan.mylastfm.datastructures.Artist;
import com.setoalan.mylastfm.datastructures.Track;
import com.setoalan.mylastfm.datastructures.UserInfo;

import java.util.ArrayList;

public class MyLastFMActivity extends Activity {

    public static UserInfo USERINFO;
    public static ArrayList<Track> THREE_RECENT_TRACKS, WEEKLY_TRACKS, RECENT_TRACKS, WEEK_TRACKS,
            MONTH_TRACKS, YEAR_TRACKS, OVERALL_TRACKS;
    public static ArrayList<Artist> WEEKLY_ARTISTS, WEEK_ARTISTS, MONTH_ARTISTS, YEAR_ARTISTS,
            OVERALL_ARTISTS;
    public static ArrayList<Album> WEEKLY_ALBUMS, WEEK_ALBUMS, MONTH_ALBUMS, YEAR_ALBUMS,
            OVERALL_ALBUMS;

    public static DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;

    FragmentManager fragmentManager;
    Fragment fragment;

    ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item,
                getResources().getStringArray(R.array.drawer_items)));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setItemChecked(0, true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        USERINFO = new UserInfo();
        THREE_RECENT_TRACKS = new ArrayList<Track>();
        WEEKLY_ARTISTS = new ArrayList<Artist>();
        WEEKLY_TRACKS = new ArrayList<Track>();
        WEEKLY_ALBUMS = new ArrayList<Album>();

        fragmentManager = getFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.fragment_container_main);

        if (fragment == null) {
            fragment = new MyLastFMFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_main, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    fragment = new MyLastFMFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_main, fragment)
                            .commit();
                    getActionBar().setTitle(getResources().getString(R.string.app_name));
                    break;
                case 1:
                    getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    if (RECENT_TRACKS == null)
                        RECENT_TRACKS = new ArrayList<Track>();
                    fragment = new RecentTracksFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_main, fragment)
                            .commit();
                    getActionBar().setTitle("Recent Tracks");
                    break;
                case 2:
                    if (WEEK_ARTISTS == null) {
                        WEEK_ARTISTS = new ArrayList<Artist>();
                        MONTH_ARTISTS = new ArrayList<Artist>();
                        YEAR_ARTISTS = new ArrayList<Artist>();
                        OVERALL_ARTISTS = new ArrayList<Artist>();
                    }
                    fragment = new TopArtistsFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_main, fragment)
                            .commit();
                    getActionBar().setTitle("Top Artists");
                    break;
                case 3:
                    if (WEEK_TRACKS == null) {
                        WEEK_TRACKS = new ArrayList<Track>();
                        MONTH_TRACKS = new ArrayList<Track>();
                        YEAR_TRACKS = new ArrayList<Track>();
                        OVERALL_TRACKS = new ArrayList<Track>();
                    }
                    fragment = new TopTracksFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_main, fragment)
                            .commit();
                    getActionBar().setTitle("Top Tracks");
                    break;
                case 4:
                    if (WEEK_ALBUMS == null) {
                        WEEK_ALBUMS = new ArrayList<Album>();
                        MONTH_ALBUMS = new ArrayList<Album>();
                        YEAR_ALBUMS = new ArrayList<Album>();
                        OVERALL_ALBUMS = new ArrayList<Album>();
                    }
                    fragment = new TopAlbumsFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_main, fragment)
                            .commit();
                    getActionBar().setTitle("Top Albums");
                    break;
            }
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }

    }

}
