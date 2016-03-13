package com.example.arthurpc.gps_application.model;

import java.util.Comparator;

public class TrackerComparator implements Comparator<Tracker> {
    @Override
    public int compare(Tracker t1, Tracker t2) {
        return t1.getLabel().compareTo(t2.getLabel());
    }
}
