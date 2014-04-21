package com.setoalan.mylastfm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.setoalan.mylastfm.MyLastFMFragment;
import com.setoalan.mylastfm.R;
import com.setoalan.mylastfm.RecentTracksFragment;
import com.setoalan.mylastfm.TopAlbumsFragment;
import com.setoalan.mylastfm.TopArtistsFragment;

public class MyLastFMActivity extends Activity {

    public static DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;

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

        getActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new MyLastFMFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment;
            switch (position) {
                case 0:
                    getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    fragment = new MyLastFMFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .commit();
                    break;
                case 1:
                    getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    fragment = new RecentTracksFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .commit();
                    break;
                case 2:
                    fragment = new TopArtistsFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .commit();
                    break;
                case 3:
                    fragment = new TopArtistsFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .commit();
                    break;
                case 4:
                    fragment = new TopAlbumsFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .commit();
                    break;
            }
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }

    }

}
