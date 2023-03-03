package com.oliviabecht.obechtbustracker;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class directionsDownloader {

    //TODO NEED TO PASS IN CHOSEN ROUTE

    private static final String Url = "http://www.ctabustracker.com/bustime/api/v2/getdirections";
    private static final ArrayList<String> directions = new ArrayList<>();
    private static final String TAG = "directionsDownloader";

    public static void download(String routeNumChosen, MainActivity mainActivityIn) {

        RequestQueue queue = Volley.newRequestQueue(mainActivityIn);

        Uri.Builder buildURL = Uri.parse(Url).buildUpon();
        buildURL.appendQueryParameter("key", mainActivityIn.getString(R.string.cta_bus_key));
        buildURL.appendQueryParameter("format", "json");
        buildURL.appendQueryParameter("rt", routeNumChosen);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener = response -> {
            try {
                handleSuccess(response.toString(), mainActivityIn);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        Response.ErrorListener error = directionsDownloader::handleFail;

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);


    }

    private static void handleSuccess(String responseText, MainActivity mainActivityIn) throws JSONException {
        JSONObject response = new JSONObject(responseText);

        JSONObject jsonObject = response.getJSONObject("bustime-response");
        JSONArray directs = jsonObject.getJSONArray("directions");
        directions.clear();
        for (int i = 0; i < directs.length(); i++) {
            JSONObject route = directs.getJSONObject(i);
            String direct = route.getString("dir");

            directions.add(direct);
        }

        mainActivityIn.runOnUiThread(() -> MainActivity.buildPopup(directions, mainActivityIn));
    }

    private static void handleFail(VolleyError ve) {
        Log.d(TAG, "handleFail: " + ve.getMessage());
    }




}
