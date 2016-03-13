package com.example.arthurpc.gps_application.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthurpc.gps_application.R;
import com.example.arthurpc.gps_application.adapter.NavigationDrawerAdapter;
import com.example.arthurpc.gps_application.infrastructure.GpsApplication;
import com.example.arthurpc.gps_application.model.NavigationDrawerItem;
import com.example.arthurpc.gps_application.model.Tracker;
import com.example.arthurpc.gps_application.services.NavixyService;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerFragment extends Fragment {

	private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private BroadcastReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

	    setUpRecyclerView(view);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setUpRecyclerView(view);
            }
        };

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver((receiver),new IntentFilter(NavixyService.NAVIXY_RESULT));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(receiver);
        super.onStop();
    }

	private void setUpRecyclerView(View view) {
		final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);

        GpsApplication application = (GpsApplication) getActivity().getApplication();

        List<Tracker> trackers = application.getTrackers();
        if(trackers != null && trackers.size() > 0 ) {
            List<NavigationDrawerItem> items = new ArrayList<>();
            for (Tracker tracker :trackers){
                NavigationDrawerItem drawerItem = new NavigationDrawerItem(tracker.getLabel(), tracker.getImgId(), tracker.getLatLng(), tracker.getBatteryLevel());
                items.add(drawerItem);
            }

            NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(getActivity(), items);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

	}

	public void setUpDrawer(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
	            // Do something of Slide of Drawer
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }
}
