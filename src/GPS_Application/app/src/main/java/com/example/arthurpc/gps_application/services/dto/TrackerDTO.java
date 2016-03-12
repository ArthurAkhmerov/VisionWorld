package com.example.arthurpc.gps_application.services.dto;

public class TrackerDTO {
    public String id;
    public String label;
    public int group_id;
    public SourceDTO source;

    public class SourceDTO {
        public String id;
        public String device_id;
        public String model;
        public boolean blocked;
        public int tariff_id;
        public String phone;
    }
}

