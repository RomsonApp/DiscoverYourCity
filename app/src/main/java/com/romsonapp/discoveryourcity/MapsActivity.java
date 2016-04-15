package com.romsonapp.discoveryourcity;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.romsonapp.discoveryourcity.model.Point;
import com.romsonapp.discoveryourcity.utils.Network;
import com.romsonapp.discoveryourcity.utils.PermissionUtils;
import com.romsonapp.discoveryourcity.utils.PointsHelper;
import com.romsonapp.discoveryourcity.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private static final int DEFAULT_ZOOM = 15;
    private PointsHelper pointsHelper;
    private String account_id;
    private int point_id;
    private int strokeColor = 0xffff0000; //red outline
    private int shadeColor = 0x44ff0000; //opaque red fill

    private Point point = null;
    Circle circle;
    ArrayList<Point> points;
    HashMap<String, Point> markers;
    Button openLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SharedPreferencesHelper preferencesHelper = new SharedPreferencesHelper(this);
        account_id = preferencesHelper.getPreferences().getString("account_id", "");
        Bundle intent = getIntent().getExtras();
        point_id = intent.getInt("point_id");

        if (point_id > 0) {
            pointsHelper = new PointsHelper();
            point = pointsHelper.getPoint(point_id);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (Network.isInternetAvailable(this)) {
            pointsHelper = new PointsHelper();
            points = pointsHelper.getPoints(account_id);
        }

        openLocationButton = (Button) findViewById(R.id.openLocationButton);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        enableMyLocation();

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String s = locationManager.getBestProvider(criteria, true);

            Location location = locationManager.getLastKnownLocation(s);
            LatLng latLng = new LatLng(Double.parseDouble("49.0723413"), Double.parseDouble("33.4026085"));
            if (location == null) {
                Log.d("location", ":(((((((");

            } else {
                markers = new HashMap<String, Point>();
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Marker marker = null;

                if (point != null) {
                    latLng = new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude()));
                    marker = addMarker(mMap, latLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    markers.put(marker.getId(), point);
                    circle = addCircle(mMap, latLng);


                    Log.d("location", String.valueOf(circle.getRadius()));

                } else {
                    for (Point point : points) {
                        BitmapDescriptor icon;
                        if (point.getStatus() == 0) {
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                        } else {
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        }
                        LatLng markerPos = new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude()));
                        marker = addMarker(mMap, markerPos, icon);
                        markers.put(marker.getId(), point);

                    }
                }


            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(DEFAULT_ZOOM)
                    .build();


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private Circle addCircle(GoogleMap map, LatLng latLng) {
        return map.addCircle(new CircleOptions()
                .center(latLng)
                .radius(30)
                .strokeColor(strokeColor)
                .fillColor(shadeColor));
    }


    private Marker addMarker(GoogleMap map, LatLng latLng, BitmapDescriptor icon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(icon);
        return map.addMarker(markerOptions);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        final Point p = markers.get(marker.getId());
        Log.d("marker", String.valueOf(p.getId()));
        openLocationButton.setVisibility(View.VISIBLE);
        openLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;

               if (!pointsHelper.openPoint(account_id, p.getId(), p.getLatitude() + ":" + p.getLongitude())) {
                    toast = Toast.makeText(getApplicationContext(), "Далеко", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(getApplicationContext(), "Норм", Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        openLocationButton.setVisibility(View.INVISIBLE);
    }
}
