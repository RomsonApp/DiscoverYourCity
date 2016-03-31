package com.romsonapp.discoveryourcity.utils;

import android.content.Context;
import android.provider.Settings;

public class Device {

    public static String getAndroid_id(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

}
