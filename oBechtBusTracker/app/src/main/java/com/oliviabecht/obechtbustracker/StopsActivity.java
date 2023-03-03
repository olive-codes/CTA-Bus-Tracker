package com.oliviabecht.obechtbustracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import com.oliviabecht.obechtbustracker.databinding.ActivityStopsBinding;

import java.util.ArrayList;


public class StopsActivity extends AppCompatActivity
        implements View.OnClickListener {

    private ActivityStopsBinding binding;
    private static String routeNum;
    private String routeName;
    private static String routeDirection;
    private String userZip;
    private static String titleString;
    private static stopsAdapter stopsAdapter;
    private static ArrayList<Stop> stopList = new ArrayList<>();
    private static final ArrayList<Stop> allStopList = new ArrayList<>();

    //ADD STUFF________________________________________________________________________________
    private static final String TAG = "StopsActivityTag";
    private AdView adView;
    private static final String adUnitId = "ca-app-pub-3940256099942544/6300978111";



    //END OF ADD STUFF_________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityStopsBinding.inflate(getLayoutInflater());

        //DO ADD STUFF HERE
        MobileAds.initialize(this, initializationStatus ->
                Log.d(TAG, "onInitializationComplete:"));
        adView = new AdView(this);
        adView.setAdUnitId(adUnitId);
        binding.stopsAdFrame.addView(adView);
        binding.stopsAdFrame.post(this::loadAdaptiveBanner);
        //END OF ADD STUFF

        routeNum = getIntent().getStringExtra("rnum");
        routeName = getIntent().getStringExtra("rname");
        routeDirection = getIntent().getStringExtra("rdirection");
        userZip = getIntent().getStringExtra("userZ");

        binding.stopsSubTitle.setText(routeDirection + " Stops");

        titleString = "Route " + routeNum + " - " + routeName;
        binding.stopsTitle.setText(titleString);


        setContentView(binding.getRoot());

        //REC VIEW STUFF STARTS HERE____________________________________________________________

        binding.stopsRec.setLayoutManager(new LinearLayoutManager(this));
        stopsAdapter = new stopsAdapter(this, stopList);
        binding.stopsRec.setAdapter(stopsAdapter);

        StopsVolley.downloadStops(this, routeNum, routeDirection);

    }

    //OUTSIDE OF ONCREATE

    public void onClick(View view) {
        int pos = binding.stopsRec.getChildLayoutPosition(view);
        Stop selectedStop = stopList.get(pos);

        Toast.makeText(
                this, selectedStop.getSName() + " selected",
                Toast.LENGTH_SHORT).show();
    }


    //DO ADD STUFF HERE ________________________________________________________________________________________________________

    private void loadAdaptiveBanner() {

        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        // Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        adView.setAdListener(new MainActivity.BannerAdListener());

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float adWidthPixels = adView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels;
        }

        float density = getResources().getDisplayMetrics().density;
        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);

    }

    class BannerAdListener extends AdListener {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            Log.d(TAG, "onAdClosed: ");
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            Log.d(TAG, "onAdFailedToLoad: " + loadAdError);
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Log.d(TAG, "onAdOpened: ");
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Log.d(TAG, "onAdLoaded: ");
        }

        @Override
        public void onAdClicked() {
            super.onAdClicked();
            Log.d(TAG, "onAdClicked: ");
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
            Log.d(TAG, "onAdImpression: ");
        }
    }

    //END OF ADD STUFF _________________________________________________________________________________________________________

    public static void acceptStops(ArrayList<Stop> stopListIn) {
        int size = stopList.size();
        stopList.clear();
        stopsAdapter.notifyItemRangeRemoved(0, size);
        stopList.addAll(stopListIn);
        stopsAdapter.notifyItemRangeChanged(0, stopList.size());
        allStopList.clear();
        allStopList.addAll(stopList);
        stopsAdapter.notifyItemRangeChanged(0, stopList.size());
    }

    public static void goToPredictActivity(StopsActivity stopsActivityIn, String sName, String sid) {

        Intent predictIntent = new Intent(stopsActivityIn, PredictionsActivity.class);
        predictIntent.putExtra("title", titleString);
        predictIntent.putExtra("direction", routeDirection);
        predictIntent.putExtra("stopName", sName);
        predictIntent.putExtra("routeNum", routeNum);
        predictIntent.putExtra("stpid", sid);
        stopsActivityIn.startActivity(predictIntent);


    }



}
