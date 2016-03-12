package com.example.arthurpc.gps_application.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.arthurpc.gps_application.infrastructure.Auth;
import com.example.arthurpc.gps_application.infrastructure.GpsApplication;

public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected GpsApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            application = (GpsApplication)getApplication();

        } catch(Exception e){
            Log.d("GPS_Application", e.getMessage());
        }
    }
}
