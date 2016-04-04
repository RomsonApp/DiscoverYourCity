package com.romsonapp.discoveryourcity.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by jomedia on 4/4/16.
 */
public class SharedPreferencesHelper {
    private static final String SHARED_PREFERENCE_APP = "config";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCE_APP, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
    }

    public void apply() {
        editor.apply();
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
}
