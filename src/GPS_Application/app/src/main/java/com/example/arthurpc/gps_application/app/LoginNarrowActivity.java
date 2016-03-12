package com.example.arthurpc.gps_application.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arthurpc.gps_application.R;
import com.example.arthurpc.gps_application.services.AuthResponseHandler;
import com.example.arthurpc.gps_application.services.NavixyService;
import com.example.arthurpc.gps_application.services.dto.AuthResponseDTO;

public class LoginNarrowActivity extends BaseActivity implements View.OnClickListener {
    private View loginButton;
    private EditText usernameInput;
    private EditText passwordInput;
    private NavixyService navixyService;

    public LoginNarrowActivity(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String defaultUploadWebsite = getString(R.string.apiBaseAddress);
        this.navixyService = new NavixyService(defaultUploadWebsite);

        setContentView(R.layout.activity_login_narrow);

        loginButton = findViewById(R.id.fragment_login_loginButton);
        usernameInput = (EditText)findViewById(R.id.fragment_login_userName);
        passwordInput = (EditText)findViewById(R.id.fragment_login_password);

        if (loginButton != null) {
            loginButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            final NavixyService navixyServiceTemp = this.navixyService;
            final LoginNarrowActivity loginNarrowActivityTemp = this;
            String login = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            navixyService.Authorize(login, password, new AuthResponseHandler() {
                @Override
                protected void OnSuccess(final AuthResponseDTO authResult) {
                    application.getAuth().getUser().setLoggedIn(authResult.success);
                    application.getAuth().setHash(authResult.hash);
                    loginNarrowActivityTemp.startActivity(new Intent(loginNarrowActivityTemp, MainActivity.class));
                }
                @Override
                protected void OnError(Throwable e){
                    Toast.makeText(loginNarrowActivityTemp, "такая комбинация логина и пароля не найдена", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

