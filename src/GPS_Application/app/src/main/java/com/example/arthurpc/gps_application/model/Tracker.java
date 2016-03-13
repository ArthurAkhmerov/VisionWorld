package com.example.arthurpc.gps_application.model;

import com.google.android.gms.maps.model.LatLng;

public class Tracker {
    private String label;
    private int imgId;
    private LatLng latLng;
    private int batteryLevel;

    public Tracker(String label, int imgId) {
        this.label = label;
        this.imgId = imgId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getLabel() {
        return label;
    }

    public int getImgId() {
        return imgId;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
}
