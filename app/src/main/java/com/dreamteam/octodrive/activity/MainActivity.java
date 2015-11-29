package com.dreamteam.octodrive.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.webservice.ParseWebservice;
import com.dreamteam.octodrive.webservice.WebserviceConstants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseWebservice.initialise(this, WebserviceConstants.kPARSE_APPLICATION_ID,
                                   WebserviceConstants.kPARSE_CLIENT_KEY);
    }
}
