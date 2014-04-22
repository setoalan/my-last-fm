package com.setoalan.mylastfm.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.setoalan.mylastfm.ArtistFragment;
import com.setoalan.mylastfm.R;

public class ArtistActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_container);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new ArtistFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
