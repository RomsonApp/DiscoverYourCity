package com.romsonapp.discoveryourcity.api;

import com.romsonapp.discoveryourcity.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.Callback;

public interface PointApi {
    @GET("/api")
    Call<ArrayList<Point>> getPoints();

    @GET("/api/point/{id}")
    Call<Point> getPoint(@Path("id") int id);
}