package com.romsonapp.discoveryourcity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
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
import com.romsonapp.discoveryourcity.model.Point;
import com.romsonapp.discoveryourcity.utils.Network;
import com.romsonapp.discoveryourcity.utils.PermissionUtils;
import com.romsonapp.discoveryourcity.utils.PointsHelper;
import com.romsonapp.discoveryourcity.utils.SharedPreferencesHelper;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SharedPreferencesHelper preferencesHelper = new SharedPreferencesHelper(this);
        account_id = preferencesHelper.getPreferences().getString("account_id", "");
        Bundle intent = getIntent().getExtras();
        point_id = intent.getInt("point_id");
        Log.d("map", "Point ID: " + point_id);
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String s = locationManager.getBestProvider(criteria, false);

            Location location = locationManager.getLastKnownLocation(s);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


            if (point != null) {
                latLng = new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude()));
                circle = addMarker(mMap, latLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED), true);
            } else {
                for (Point point : points) {
                    BitmapDescriptor icon;
                    if (point.getStatus() == 0) {
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                    } else {
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    }

                    LatLng markerPos = new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude()));
                    addMarker(mMap, markerPos, icon, false);

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


    private Circle addMarker(GoogleMap map, LatLng latLng, BitmapDescriptor icon, Boolean isCircle) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(icon));

        if (isCircle)
            return addCircle(map, latLng);
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));

        Log.d("map", "work");
        if (point != null) {
            Log.d("map", "point");
            float[] distance = new float[2];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), circle.getCenter().latitude, circle.getCenter().longitude, distance);

            if( distance[0] > circle.getRadius() ){
                Toast.makeText(getBaseContext(), "Outside, distance from center: " + distance[0] + " radius: " + circle.getRadius(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Inside, distance from center: " + distance[0] + " radius: " + circle.getRadius() , Toast.LENGTH_LONG).show();
            }
        }

    }
}
