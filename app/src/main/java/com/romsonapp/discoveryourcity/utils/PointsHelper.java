package com.romsonapp.discoveryourcity.utils;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.romsonapp.discoveryourcity.R;
import com.romsonapp.discoveryourcity.api.PointApi;
import com.romsonapp.discoveryourcity.model.Point;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PointsHelper {
    private final String URL = "http://romsonapp.com/android/public/";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private PointApi api = retrofit.create(PointApi.class);

    public PointsHelper() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public ArrayList<Point> getPoints() {
        Call<ArrayList<Point>> call = api.getPoints();
        try {
            Response<ArrayList<Point>> execute = call.execute();
            if (execute.isSuccess())
                return execute.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Point getPoint(int id) {
        Call<Point> call = api.getPoint(id);
        try {
            Response<Point> execute = call.execute();
            if (execute.isSuccess())
                return execute.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field get(String className, String methodName) {
        Class c = className.getClass();
        try {
            return c.getField(methodName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
