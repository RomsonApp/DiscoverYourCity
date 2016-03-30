package com.romsonapp.discoveryourcity.utils;

import android.app.DownloadManager;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by romson on 3/21/16.
 */
public class Request {
    public Request() {

    }

    public void get(String link) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url
                    .openConnection();
            StringBuilder buffer = new StringBuilder();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String resultJson = buffer.toString();
            JSONObject jsonObject = new JSONObject(resultJson);
            String response = (String) jsonObject.get("response");


        } catch (Exception e) {
            Log.d("db", e.getMessage());
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
