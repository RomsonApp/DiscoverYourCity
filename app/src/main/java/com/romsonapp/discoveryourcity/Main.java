package com.romsonapp.discoveryourcity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.romsonapp.discoveryourcity.adapter.CardsAdapter;
import com.romsonapp.discoveryourcity.api.PointApi;
import com.romsonapp.discoveryourcity.model.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class Main extends AppCompatActivity {

    GridView gridView;
    private final String URL = "http://192.168.0.4:8000";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private PointApi api = retrofit.create(PointApi.class);
    ArrayList<Point> points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        Call<ArrayList<Point>> call = api.getPoints();
        try {
            points = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        renderCards(points);
    }

    private void renderCards(ArrayList<Point> points) {
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new CardsAdapter(this, points));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openCard(View view) {
        Intent intent = new Intent(this, Card.class);
        intent.putExtra("id", Integer.parseInt((String) view.getContentDescription()));
        startActivity(intent);
    }
}
