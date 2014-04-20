package com.setoalan.mylastfm;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {

    SharedPreferences sharedPreferences;

    Button goBTN;
    EditText usernameET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        usernameET = (EditText) view.findViewById(R.id.username_et);
        goBTN = (Button) view.findViewById(R.id.go_btn);
        goBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLastFMFragment.USERNAME = usernameET.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", MyLastFMFragment.USERNAME);
                editor.commit();
                getActivity().finish();
            }
        });

        return view;
    }
}
