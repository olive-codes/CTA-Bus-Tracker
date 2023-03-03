package com.oliviabecht.obechtbustracker;

import android.location.Location;
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

public class predictionsVolley {

    private static final String Url = "http://www.ctabustracker.com/bustime/api/v2/getpredictions";
    private static final ArrayList<prediction> predictList = new ArrayList<>();
    private static final String TAG = "predictionsVolley";

    public static void downloadPredictions (PredictionsActivity predictionsActivityIn, String routeNumChosen, String stpid) {

        RequestQueue queue = Volley.newRequestQueue(predictionsActivityIn);

        Uri.Builder buildURL = Uri.parse(Url).buildUpon();
        buildURL.appendQueryParameter("key", predictionsActivityIn.getString(R.string.cta_bus_key));
        buildURL.appendQueryParameter("format", "json");
        buildURL.appendQueryParameter("rt", routeNumChosen);
        buildURL.appendQueryParameter("stpid", stpid);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener = response -> {
            try {
                handleSuccess(response.toString(), predictionsActivityIn);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        Response.ErrorListener error = predictionsVolley::handleFail;

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);


    }

    private static void handleSuccess(String responseText, PredictionsActivity predictionsActivityIn) throws JSONException {
        JSONObject response = new JSONObject(responseText);

        JSONObject jsonObject = response.getJSONObject("bustime-response");
        JSONArray Jpredict = jsonObject.getJSONArray("prd");
        predictList.clear();
        for (int i = 0; i < Jpredict.length(); i++) {
            JSONObject route = Jpredict.getJSONObject(i);
            String predictvid = route.getString("vid");
            String predictrtdir = route.getString("rtdir");
            String predictdes = route.getString("des");
            String predictprdtm = route.getString("prdtm");
            String predictdly = route.getString("dly");
            String predictprdctdn = route.getString("prdctdn");

           prediction predictObj = new prediction(predictvid, predictrtdir, predictdes, predictprdtm, predictdly, predictprdctdn);
           predictList.add(predictObj);


        }

        predictionsActivityIn.runOnUiThread(() -> PredictionsActivity.acceptPredictions(predictList));

    }


    private static void handleFail(VolleyError ve) {
        Log.d(TAG, "handleFail: " + ve.getMessage());
    }


}
