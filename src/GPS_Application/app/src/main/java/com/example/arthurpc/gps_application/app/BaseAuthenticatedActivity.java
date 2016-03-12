package com.example.arthurpc.gps_application.app;

import android.content.Intent;
import android.os.Bundle;

import com.example.arthurpc.gps_application.infrastructure.Auth;

public abstract class BaseAuthenticatedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!application.getAuth().getUser().isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        onGpsAppCreate(savedInstanceState);
    }

    protected abstract void onGpsAppCreate(Bundle savedInstanceState);


}
