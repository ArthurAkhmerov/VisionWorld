package com.example.arthurpc.gps_application.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.arthurpc.gps_application.services.dto.AuthResponseDTO;
import com.example.arthurpc.gps_application.services.dto.GetStateResponseDTO;
import com.example.arthurpc.gps_application.services.dto.GetTrackersResponseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class NavixyService extends Service {

    private static final String TAG = "NavixyService";
    private String defaultUploadWebsite;

    public NavixyService(String defaultUploadWebsite){
        this.defaultUploadWebsite = defaultUploadWebsite;
    }

    public void GetTrackers(String hash, final GetTrackersResponseHandler getTrackersResponseHandler){
        final RequestParams requestParams = new RequestParams();
        requestParams.put("hash", hash);

        LoopjHttpClient.get(defaultUploadWebsite + "/tracker/list", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new Gson();
                    GetTrackersResponseDTO getTrackersResponseDTO = gson.fromJson(new String(responseBody), GetTrackersResponseDTO.class);
                    getTrackersResponseHandler.OnSuccess(getTrackersResponseDTO.list);

                } catch(JsonSyntaxException e) {
                    getTrackersResponseHandler.OnError(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                getTrackersResponseHandler.OnError(e);
            }
        });
    }

    public void GetState(String hash, String trackerid, final GetStateHandler getStateHandler){
        final RequestParams requestParams = new RequestParams();
        requestParams.put("hash", hash);
        requestParams.put("tracker_id", trackerid);

        LoopjHttpClient.get(defaultUploadWebsite + "/tracker/get_state", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new Gson();
                    GetStateResponseDTO getStateResponseDTO = gson.fromJson(new String(responseBody), GetStateResponseDTO.class);
                    getStateHandler.OnSuccess(getStateResponseDTO.state);

                } catch(JsonSyntaxException e) {
                    getStateHandler.OnError(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                getStateHandler.OnError(e);
            }
        });
    }

    public void Authorize(String login, String password, final AuthResponseHandler authResponseHandler){
        final RequestParams requestParams = new RequestParams();
        requestParams.put("login", login);
        requestParams.put("password", password);

        LoopjHttpClient.get(defaultUploadWebsite + "/user/auth", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new Gson();
                    AuthResponseDTO authResultDTO = gson.fromJson(new String(responseBody), AuthResponseDTO.class);
                    authResponseHandler.OnSuccess(authResultDTO);
                } catch(JsonSyntaxException e) {
                    authResponseHandler.OnError(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                authResponseHandler.OnError(e);
                LoopjHttpClient.debugLoopJ(TAG, "authorize - failure", defaultUploadWebsite, requestParams, responseBody, headers, statusCode, e);

            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
