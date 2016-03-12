package com.example.arthurpc.gps_application.services;


import com.example.arthurpc.gps_application.services.dto.TrackerDTO;

public class GetTrackersResponseHandler {
    public void OnSuccess(TrackerDTO[] trackers){}
    protected void OnError(Throwable e){}
}
