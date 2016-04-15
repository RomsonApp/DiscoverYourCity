package com.romsonapp.discoveryourcity.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.widget.Toast;

import com.romsonapp.discoveryourcity.NeedConection;
import com.romsonapp.discoveryourcity.R;

import java.net.InetAddress;

public class Network {
    /**
     * Чтоб не вылетало приложение при отсутствии интернета.
     * @param activity
     * @return
     */
    public static boolean isInternetAvailable(Activity activity) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toast toast;
        Intent intent = new Intent(activity, NeedConection.class);
        try {
            String _SERVER_NAME = "185.69.153.193";
            InetAddress ipAddr = InetAddress.getByName(_SERVER_NAME);

            if (ipAddr.equals("")) {
                toast = Toast.makeText(activity, activity.getApplicationContext()
                        .getResources().getString(R.string.server_dont_work), Toast.LENGTH_SHORT);
                toast.show();
                activity.startActivity(intent);
                activity.finish();

                return false;
            }
        } catch (Exception e) {
            toast = Toast.makeText(activity, activity.getResources().getString(R.string.internet), Toast.LENGTH_SHORT);
            toast.show();
            activity.startActivity(intent);
            activity.finish();

            return false;
        }

        return true;
    }
}
