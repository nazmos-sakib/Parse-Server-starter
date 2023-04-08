package com.example.android_parse_basic_tutorial;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //Initializing connection at onCreate
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

}
