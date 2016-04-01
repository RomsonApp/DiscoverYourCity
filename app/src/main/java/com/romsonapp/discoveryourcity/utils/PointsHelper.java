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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PointsHelper {
    private String id;
    private String status;
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

    public ArrayList<Point> getPoints(String account_id) {
        Call<ArrayList<Point>> call = api.getPoints(account_id);
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

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public PointsHelper parseImageDescription(String description) {
        String pattern = "status:(.*),id:(.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(description);
        System.out.println(m.toString());
        if(m.find()) {
            status = m.group(1);
            System.out.println("Status: " + status);
            id = m.group(2);
            System.out.println("Id: " + id);
        }
        return this;
    }
}
