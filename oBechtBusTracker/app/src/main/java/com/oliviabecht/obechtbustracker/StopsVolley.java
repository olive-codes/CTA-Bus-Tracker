package com.oliviabecht.obechtbustracker;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import androidx.core.math.MathUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StopsVolley {

    //TODO NEED TO PASS IN CHOSEN ROUTE

    private static final String Url = "http://www.ctabustracker.com/bustime/api/v2/getstops";
    private static final ArrayList<Stop> stopList = new ArrayList<>();
    private static final String TAG = "stopsVolley";

    public static void downloadStops (StopsActivity stopsActivityIn, String routeNumChosen, String directionChosen) {

        RequestQueue queue = Volley.newRequestQueue(stopsActivityIn);

        Uri.Builder buildURL = Uri.parse(Url).buildUpon();
        buildURL.appendQueryParameter("key", stopsActivityIn.getString(R.string.cta_bus_key));
        buildURL.appendQueryParameter("format", "json");
        buildURL.appendQueryParameter("rt", routeNumChosen);
        buildURL.appendQueryParameter("dir", directionChosen);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener = response -> {
            try {
                handleSuccess(response.toString(), stopsActivityIn);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        Response.ErrorListener error = StopsVolley::handleFail;

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);


    }

    private static void handleSuccess(String responseText, StopsActivity stopsActivityIn) throws JSONException {
        JSONObject response = new JSONObject(responseText);

        JSONObject jsonObject = response.getJSONObject("bustime-response");
        JSONArray Jstop = jsonObject.getJSONArray("stops");
        stopList.clear();
        for (int i = 0; i < Jstop.length(); i++) {
            JSONObject route = Jstop.getJSONObject(i);
            String stopID = route.getString("stpid");
            String stopName = route.getString("stpnm");
            String stopLat = route.getString("lat");
            String stopLon = route.getString("lon");

            float[] distanceCalc = new float[1];

            try {
                Double stopLatValue  = Double.parseDouble(stopLat);
                Double stopLonValue = Double.parseDouble(stopLon);

                Location.distanceBetween(MainActivity.userLat, MainActivity.userLon, stopLatValue, stopLonValue, distanceCalc);

                String horDirection = stopLonValue - MainActivity.userLon > 0 ? "east" : "west";
                String verticalDirection = stopLatValue - MainActivity.userLat > 0 ? "north" : "south";
                String direction = verticalDirection + horDirection;

                float distanceBetweenValue = Math.abs(distanceCalc[0]);
                Integer distanceRounded = Math.round(distanceBetweenValue);

                if(distanceBetweenValue <= 1000) {
                    //TODO add distance in constructor for stop
                    Stop stopObj = new Stop(stopID, stopName, stopLat, stopLon, direction, distanceRounded);
                    stopList.add(stopObj);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        stopsActivityIn.runOnUiThread(() -> StopsActivity.acceptStops(stopList));

    }


    private static void handleFail(VolleyError ve) {
        Log.d(TAG, "handleFail: " + ve.getMessage());
    }


}
