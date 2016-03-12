package com.example.arthurpc.gps_application.model;

import java.util.ArrayList;
import java.util.List;

import com.example.arthurpc.gps_application.R;
import com.google.android.gms.maps.model.LatLng;

public class NavigationDrawerItem {

	private String title;
	private int imageId;
	private LatLng latLng;
	private int batteryLevel;


	public NavigationDrawerItem(String title, int imageId, LatLng latLng, int batteryLevel) {
		this.title = title;
		this.imageId = imageId;
		this.latLng = latLng;
		this.batteryLevel = batteryLevel;
	}


	public LatLng getLatLng() {
		return latLng;
	}

	public String getTitle() {
		return title;
	}

	public int getImageId() {
		return imageId;
	}
}
