package com.example.arthurpc.gps_application.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.example.arthurpc.gps_application.R;
import com.example.arthurpc.gps_application.services.AuthResponseHandler;
import com.example.arthurpc.gps_application.services.NavixyService;
import com.example.arthurpc.gps_application.services.dto.AuthResponseDTO;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private View loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.activity_login_login);

        if (loginButton != null) {
            loginButton.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == loginButton) {

            startActivity(new Intent(this,LoginNarrowActivity.class));
        }
    }
}

