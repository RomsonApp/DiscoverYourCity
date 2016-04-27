package com.romsonapp.discoveryourcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.romsonapp.discoveryourcity.adapter.CardsAdapter;
import com.romsonapp.discoveryourcity.model.Point;
import com.romsonapp.discoveryourcity.utils.Network;
import com.romsonapp.discoveryourcity.utils.PointsHelper;
import com.romsonapp.discoveryourcity.utils.SharedPreferencesHelper;

import java.util.ArrayList;

public class Main extends AppCompatActivity {

    GridView gridView;
    PointsHelper pointsHelper;
    ArrayList<Point> points;
    String account_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Network.isInternetAvailable(this)) {
            SharedPreferencesHelper preferencesHelper = new SharedPreferencesHelper(this);
            account_id = preferencesHelper.getPreferences().getString("account_id", "");
            pointsHelper = new PointsHelper();
            points = pointsHelper.getPoints(account_id);
            renderCards(points);
        }
    }

    private void renderCards(ArrayList<Point> points) {
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new CardsAdapter(this, points));
    }

    @Override
    protected void onStart() {
        super.onStart();
        points = pointsHelper.getPoints(account_id);
        renderCards(points);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        PointsHelper helper = pointsHelper.parseImageDescription((String) view.getContentDescription());
        int point_id = Integer.parseInt(helper.getId());
        int status = Integer.parseInt(helper.getStatus());
        if (status == 0) {
            Intent map = new Intent(this, MapsActivity.class);
            map.putExtra("point_id", point_id);
            startActivity(map);
        } else {
            Intent intent = new Intent(this, Card.class);
            intent.putExtra("point_id", point_id);
            startActivity(intent);
        }
    }

    public void showMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("account_id", account_id);
        startActivity(intent);
    }
}
