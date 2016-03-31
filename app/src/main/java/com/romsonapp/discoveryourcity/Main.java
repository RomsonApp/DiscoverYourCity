package com.romsonapp.discoveryourcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.romsonapp.discoveryourcity.adapter.CardsAdapter;
import com.romsonapp.discoveryourcity.model.Point;
import com.romsonapp.discoveryourcity.utils.Device;
import com.romsonapp.discoveryourcity.utils.Network;
import com.romsonapp.discoveryourcity.utils.PointsHelper;

import java.util.ArrayList;


public class Main extends AppCompatActivity {

    GridView gridView;
    PointsHelper pointsHelper;
    ArrayList<Point> points;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (Network.isInternetAvailable(this)) {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d("play", "Failed");
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            pointsHelper = new PointsHelper();
            points = pointsHelper.getPoints();
            renderCards(points);
        }

    }

    private void renderCards(ArrayList<Point> points) {
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new CardsAdapter(this, points));
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
        Intent intent = new Intent(this, Card.class);
        int id = Integer.parseInt((String) view.getContentDescription());
        Log.d("device", Device.getAndroid_id(this));
        if (id != 0) {
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
}
