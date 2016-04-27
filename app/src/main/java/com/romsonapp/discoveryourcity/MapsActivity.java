package com.romsonapp.discoveryourcity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.romsonapp.discoveryourcity.model.Point;
import com.romsonapp.discoveryourcity.utils.Network;
import com.romsonapp.discoveryourcity.utils.PermissionUtils;
import com.romsonapp.discoveryourcity.utils.PointsHelper;
import com.romsonapp.discoveryourcity.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, LocationListener {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int DEFAULT_ZOOM = 15;
    private PointsHelper pointsHelper;
    private String account_id;
    private int point_id;
    private int strokeColor = 0xffff0000; //red outline
    private int shadeColor = 0x44ff0000; //opaque red fill
    double latitude;
    double longtitude;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;
    Location location;
    private Point point = null;
    Circle circle;
    ArrayList<Point> points;
    HashMap<String, Point> markers;
    Button openLocationButton;
    int permissionCheck;

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
        permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setMyLocationEnabled(true);
        enableMyLocation();

    }

    private void enableMyLocation() {
        Location location = getLocation();
        LatLng latLng = new LatLng(Double.parseDouble("49.0723413"), Double.parseDouble("33.4026085"));

        if (location != null) {
            markers = new HashMap<String, Point>();
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Marker marker = null;

            if (point != null) {
                latLng = new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude()));
                marker = addMarker(mMap, latLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                markers.put(marker.getId(), point);
                circle = addCircle(mMap, latLng);
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
        openLocationButton.setVisibility(View.VISIBLE);
        openLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;

                if (!pointsHelper.openPoint(account_id, p.getId(), location.getLatitude() + ":" + location.getLongitude())) {
                    toast = Toast.makeText(getApplicationContext(), R.string.so_far, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Card.class);
                    intent.putExtra("point_id", p.getId());
                    startActivity(intent);
                    MapsActivity.this.finish();
                }
            }
        });

        return false;
    }

    public Location getLocation() {
        location = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longtitude = location.getLongitude();
                            }
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        openLocationButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
