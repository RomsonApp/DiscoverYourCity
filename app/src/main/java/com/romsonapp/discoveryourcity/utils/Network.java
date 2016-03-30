package com.romsonapp.discoveryourcity.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.StrictMode;

import java.net.InetAddress;

/**
 * Created by romson on 3/21/16.
 */
public class Network {
    private static String _SERVER_NAME = "http://romsonapp.com";

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName(_SERVER_NAME);

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }



}
