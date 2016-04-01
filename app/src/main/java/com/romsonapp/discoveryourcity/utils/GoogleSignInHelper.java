package com.romsonapp.discoveryourcity.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

/**
 * Created by jomedia on 3/31/16.
 */
public class GoogleSignInHelper implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private int RC_SIGN_IN = 9001;

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public GoogleSignInHelper(Context context) {
        this.context = context;
        this.mGoogleApiClient = setmGoogleApiClient();
    }

    private GoogleApiClient setmGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();

        return new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public GoogleSignInResult start() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(opr.isDone()){
            Log.d("play", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            return result;
        }


        return null;
    }

    public Intent login() {
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }
}
