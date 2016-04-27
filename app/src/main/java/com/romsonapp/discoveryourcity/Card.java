package com.romsonapp.discoveryourcity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.romsonapp.discoveryourcity.model.Point;
import com.romsonapp.discoveryourcity.utils.Network;
import com.romsonapp.discoveryourcity.utils.PointsHelper;
import com.squareup.picasso.Picasso;

public class Card extends AppCompatActivity {
    private PointsHelper pointsHelper = new PointsHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Bundle intent = getIntent().getExtras();

        int point_id = intent.getInt("point_id");
        if(Network.isInternetAvailable(this)){
            Point point = pointsHelper.getPoint(point_id);

            ImageView cardImage = (ImageView) findViewById(R.id.card_image);
            TextView cardTitle = (TextView) findViewById(R.id.card_title);
            WebView wvBody = (WebView) findViewById(R.id.wvBody);

            Picasso.with(this).load(point.getImage()).into(cardImage);

            cardTitle.setText(point.getTitle());
            wvBody.loadData(point.getBody(), "text/html", "utf-8");
        }
    }
}
