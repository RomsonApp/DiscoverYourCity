package com.romsonapp.discoveryourcity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.romsonapp.discoveryourcity.api.PointApi;
import com.romsonapp.discoveryourcity.model.Point;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class Card extends AppCompatActivity {
    private final String URL = "http://romsonapp.com/android/public/";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private PointApi api = retrofit.create(PointApi.class);
    private ImageView cardImage;
    private TextView cardTitle;
    private Point point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle intent = getIntent().getExtras();
        int id = intent.getInt("id");
        Call<Point> call = api.getPoint(id);
        try {
            point = call.execute().body();
            cardImage = (ImageView) findViewById(R.id.card_image);
            cardTitle = (TextView) findViewById(R.id.card_title);

            Picasso.with(this).load(point.getImage()).into(cardImage);
            cardTitle.setText(point.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
