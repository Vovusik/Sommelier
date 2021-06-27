package com.sommelier.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sommelier.R;
import com.sommelier.helper.RegionMapView;


public class RegionFragment extends Fragment {

    public static final String TAG = "RegionFragmentTag";

    private TextView mapName;
    public RegionFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.region_activity,
                container, false);

        RegionMapView mapView = view.findViewById(R.id.mapView);
        mapName = view.findViewById(R.id.address);
        mapView.setMapRes(R.raw.regions);
        mapView.loadMap();

        mapView.setOnMapItemListener(item -> mapName.setText(item.name + " - " + item.temperature + " °C"));

        return view;
    }
}

