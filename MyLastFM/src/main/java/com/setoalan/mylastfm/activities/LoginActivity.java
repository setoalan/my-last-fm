package com.setoalan.mylastfm.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.setoalan.mylastfm.LoginFragment;
import com.setoalan.mylastfm.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container_main);

        if (fragment == null) {
            fragment = new LoginFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_main, fragment)
                    .commit();
        }
    }

}
