package com.romsonapp.discoveryourcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.romsonapp.discoveryourcity.R;
import com.romsonapp.discoveryourcity.utils.GoogleSignInHelper;
import com.romsonapp.discoveryourcity.utils.PointsHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInHelper googleSignInHelper;
    private int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googleSignInHelper = new GoogleSignInHelper(this);

    }

    public void login(View view) {
        startActivityForResult(googleSignInHelper.login(), RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("play", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInResult result = googleSignInHelper.start();
        if(result != null && result.isSuccess()) {
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            System.out.println(result.getStatus().getStatusMessage());
            System.out.println(resultCode);
            System.out.println(requestCode);
            System.out.println(result.getSignInAccount().getDisplayName());
        }
    }

    public void signOut(View view) {

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });

    }
}
