package com.example.arthurpc.gps_application.infrastructure;

import android.app.Application;
import android.util.Log;

import com.example.arthurpc.gps_application.R;
import com.example.arthurpc.gps_application.model.Tracker;
import com.example.arthurpc.gps_application.services.AuthResponseHandler;
import com.example.arthurpc.gps_application.services.GetStateHandler;
import com.example.arthurpc.gps_application.services.GetTrackersResponseHandler;
import com.example.arthurpc.gps_application.services.NavixyService;
import com.example.arthurpc.gps_application.services.dto.AuthResponseDTO;
import com.example.arthurpc.gps_application.services.dto.StateDTO;
import com.example.arthurpc.gps_application.services.dto.TrackerDTO;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GpsApplication extends Application {
    private static final String TAG = "GPS_Application";

    private Auth auth;
    private NavixyService navixyService;
    TrackerDTO[] trackersDtos;
    List<Tracker> trackers;

    public TrackerDTO[] getTrackersDtos() { return trackersDtos;}
    public List<Tracker> getTrackers() { return trackers;}

    public void addTracker(Tracker tracker){
        if(this.trackers == null) this.trackers = new ArrayList<Tracker>();
        this.trackers.add(tracker);
    }
    @Override
    public void onCreate() {
        super.onCreate();

        String defaultUploadWebsite = getString(R.string.apiBaseAddress);
        this.navixyService = new NavixyService(defaultUploadWebsite);

        this.auth = new Auth(this);
    }

    public void Authorize(String login, String password){
        final NavixyService navixyServiceTemp = this.navixyService;
        final GpsApplication gpsApplicationTemp = this;
        navixyService.Authorize(login, password, new AuthResponseHandler() {
            @Override
            protected void OnSuccess(final AuthResponseDTO authResult) {
                gpsApplicationTemp.auth.setHash(authResult.hash);
            }
        });

    }

    public void LoadTrackers(final GetTrackersResponseHandler getTrackersResponseHandler){
        final GpsApplication gpsApplicationTemp = this;
        final NavixyService navixyServiceTemp = this.navixyService;
        this.navixyService.GetTrackers(this.auth.getHash(), new GetTrackersResponseHandler() {
            @Override
            public void OnSuccess(TrackerDTO[] trackersDtos) {
                gpsApplicationTemp.trackersDtos = trackersDtos;

                for(final TrackerDTO trackerDTO:trackersDtos){
                    final TrackerDTO trackerDtoTemp = trackerDTO;
                        navixyServiceTemp.GetState(gpsApplicationTemp.getAuth().getHash(), trackerDTO.id, new GetStateHandler() {
                            @Override
                            public void OnSuccess(StateDTO state) {
                                Tracker newTracker = new Tracker(trackerDtoTemp.label, R.drawable.ic_birds);
                                newTracker.setLatLng(new LatLng(state.gps.location.lat, state.gps.location.lng));
                                newTracker.setBatteryLevel(state.battery_level);
                                gpsApplicationTemp.addTracker(newTracker);
                                if (gpsApplicationTemp.getTrackers() != null && gpsApplicationTemp.getTrackersDtos() != null &&
                                        gpsApplicationTemp.getTrackers().size() == gpsApplicationTemp.getTrackersDtos().length) {
                                    getTrackersResponseHandler.OnSuccess(gpsApplicationTemp.getTrackersDtos());
                                }
                            }
                        });
                }
            }
        });
    }

    public Auth getAuth() {
        return auth;
    }
}
