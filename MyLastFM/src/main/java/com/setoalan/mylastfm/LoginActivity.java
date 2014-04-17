package com.setoalan.mylastfm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    Button goBTN;
    EditText usernameET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        usernameET = (EditText) findViewById(R.id.username_et);
        goBTN = (Button) findViewById(R.id.go_btn);
        goBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLastFMFragment.USERNAME = usernameET.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", MyLastFMFragment.USERNAME);
                editor.commit();
                finish();
            }
        });
    }

}
