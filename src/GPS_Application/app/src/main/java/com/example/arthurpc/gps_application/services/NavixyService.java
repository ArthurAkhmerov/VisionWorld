package com.example.arthurpc.gps_application.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.arthurpc.gps_application.R;
import com.example.arthurpc.gps_application.infrastructure.GpsApplication;
import com.example.arthurpc.gps_application.model.Tracker;
import com.example.arthurpc.gps_application.services.dto.AuthResponseDTO;
import com.example.arthurpc.gps_application.services.dto.GetStateResponseDTO;
import com.example.arthurpc.gps_application.services.dto.GetTrackersResponseDTO;
import com.example.arthurpc.gps_application.services.dto.StateDTO;
import com.example.arthurpc.gps_application.services.dto.TrackerDTO;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class NavixyService extends Service {

    public NavixyService(){
        this.defaultUploadWebsite = "http://api.vision-world.org";
    }

    private static final String TAG = "NavixyService";
    private String defaultUploadWebsite;

    static final public String NAVIXY_RESULT = "com.example.arthurpc.gps_application.NavixyService.REQUEST_PROCESSED";

    public static final long UPDATE_INTERVAL = 10 * 1000; // 10 seconds
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private LocalBroadcastManager broadcaster;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);

        if(mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();

        mTimer.scheduleAtFixedRate(new TestUpdateUiTask(), 0, UPDATE_INTERVAL);

    }

    public void LoadTrackers(final GetTrackersResponseHandler getTrackersResponseHandler){
        final GpsApplication gpsApplication = (GpsApplication)getApplication();
        final NavixyService navixyServiceTemp = this;
        this.GetTrackers(gpsApplication.getAuth().getHash(), new GetTrackersResponseHandler() {
            @Override
            public void OnSuccess(TrackerDTO[] trackersDtos) {
                for (final TrackerDTO trackerDTO : trackersDtos) {
                    final TrackerDTO trackerDtoTemp = trackerDTO;
                    navixyServiceTemp.GetState(gpsApplication.getAuth().getHash(), trackerDTO.id, new GetStateHandler() {
                        @Override
                        public void OnSuccess(StateDTO state) {
                            Tracker newTracker = new Tracker(trackerDtoTemp.label, R.drawable.ic_birds);
                            newTracker.setLatLng(new LatLng(state.gps.location.lat, state.gps.location.lng));
                            newTracker.setBatteryLevel(state.battery_level);
                            Intent intent = new Intent(NAVIXY_RESULT);
                            gpsApplication.addTracker(newTracker);
                            broadcaster.sendBroadcast(intent);
                        }
                    });
                }
            }
        });
    }

    private void GetTrackers(String hash, final GetTrackersResponseHandler getTrackersResponseHandler){
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

    private void GetState(String hash, String trackerid, final GetStateHandler getStateHandler){
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

    class TestUpdateUiTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    LoadTrackers(new GetTrackersResponseHandler() {
                        @Override
                        public void OnSuccess(TrackerDTO[] trackers) {
                            Intent intent = new Intent(NAVIXY_RESULT);
                            broadcaster.sendBroadcast(intent);
                        }
                    });

                }
            });
        }
    }
}
