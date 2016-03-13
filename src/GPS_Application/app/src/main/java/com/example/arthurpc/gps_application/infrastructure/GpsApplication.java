package com.example.arthurpc.gps_application.infrastructure;

import android.app.Application;
import android.util.Log;

import com.example.arthurpc.gps_application.R;
import com.example.arthurpc.gps_application.model.Tracker;
import com.example.arthurpc.gps_application.model.TrackerComparator;
import com.example.arthurpc.gps_application.services.AuthResponseHandler;
import com.example.arthurpc.gps_application.services.GetStateHandler;
import com.example.arthurpc.gps_application.services.GetTrackersResponseHandler;
import com.example.arthurpc.gps_application.services.NavixyService;
import com.example.arthurpc.gps_application.services.dto.AuthResponseDTO;
import com.example.arthurpc.gps_application.services.dto.StateDTO;
import com.example.arthurpc.gps_application.services.dto.TrackerDTO;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GpsApplication extends Application {
    private Auth auth;
    List<Tracker> trackers;

    public List<Tracker> getTrackers() {
        if(trackers == null) return null;
        Collections.sort(trackers, new TrackerComparator());
        return trackers;
    }

    public void addTracker(Tracker newTracker){
        if(this.trackers == null) this.trackers = new ArrayList<>();

        for (Tracker tracker: trackers){
            if(tracker.getLabel().equals(newTracker.getLabel())){
                this.trackers.remove(tracker);
                this.trackers.add(newTracker);
                return;
            }
        }

        this.trackers.add(newTracker);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.auth = new Auth(this);
    }

    public Auth getAuth() {
        return auth;
    }
}
