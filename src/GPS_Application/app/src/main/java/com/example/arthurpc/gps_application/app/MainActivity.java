package com.example.arthurpc.gps_application.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.arthurpc.gps_application.R;
import com.example.arthurpc.gps_application.services.GetTrackersResponseHandler;
import com.example.arthurpc.gps_application.services.NavixyService;
import com.example.arthurpc.gps_application.services.dto.TrackerDTO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends BaseAuthenticatedActivity implements OnMapReadyCallback {
	//protected Toolbar toolbar;

	private static final String TAG = "GPS_Application";
	private GoogleMap mMap;
	private NavixyService navixyService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void setUpToolbar() {

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Gps Application");
		toolbar.inflateMenu(R.menu.menu_main);
	}

	private void setUpDrawer() {

		NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drwr_fragment);
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerFragment.setUpDrawer(R.id.nav_drwr_fragment, drawerLayout, toolbar);
	}

	public void GoToLocation(LatLng latlng, String title) {

			SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);
			GoogleMap gmap = mapFragment.getMap();
			if(gmap != null) {
				gmap.addMarker(new MarkerOptions().position(latlng).title("Marker by " + title));
				gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f));
			}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;


	}

	@Override
	protected void onGpsAppCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);

		setUpToolbar();
		setUpDrawer();

		final MainActivity mainActivityTemp = this;

		if(application.getTrackersDtos() == null) {
			application.LoadTrackers(new GetTrackersResponseHandler() {
				@Override
				public void OnSuccess(final TrackerDTO[] trackers) {
					mainActivityTemp.startActivity(new Intent(mainActivityTemp, MainActivity.class));
				}
			});
		}
	}
}