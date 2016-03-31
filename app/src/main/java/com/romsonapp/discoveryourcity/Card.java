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
import com.romsonapp.discoveryourcity.utils.Network;
import com.romsonapp.discoveryourcity.utils.PointsHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class Card extends AppCompatActivity {
    private PointsHelper pointsHelper = new PointsHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Bundle intent = getIntent().getExtras();

        int id = intent.getInt("id");
        if(Network.isInternetAvailable(this)){
            Point point = pointsHelper.getPoint(id);

            ImageView cardImage = (ImageView) findViewById(R.id.card_image);
            TextView cardTitle = (TextView) findViewById(R.id.card_title);
            TextView tvBody = (TextView) findViewById(R.id.tvBody);

            Picasso.with(this).load(point.getImage()).into(cardImage);

            cardTitle.setText(point.getTitle());
            tvBody.setText(point.getBody());

        }
    }
}
