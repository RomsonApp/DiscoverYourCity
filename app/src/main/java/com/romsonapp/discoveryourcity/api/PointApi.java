package com.romsonapp.discoveryourcity.api;

import com.romsonapp.discoveryourcity.model.Point;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PointApi {
    @GET("api/account_id/{account_id}")
    Call<ArrayList<Point>> getPoints(@Path("account_id") String account_id);

    @GET("api/point/{id}")
    Call<Point> getPoint(@Path("id") int id);

    @GET("api/open/account_id/{account_id}/point_id/{point_id}/location/{location}")
    Call<Object> openPoint(@Path("account_id") String account_id, @Path("point_id") int point_id, @Path("location") String location);
}

