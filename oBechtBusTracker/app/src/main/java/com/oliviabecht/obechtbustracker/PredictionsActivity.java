package com.oliviabecht.obechtbustracker;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.timepicker.TimeFormat;
import com.oliviabecht.obechtbustracker.databinding.ActivityPredictionsBinding;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

public class PredictionsActivity extends AppCompatActivity {

    private ActivityPredictionsBinding binding;
    private String titleString;
    private String stopName;
    private String directionChosen;
    private String routeNum;
    private String stpid;
    private static predictionsAdapter predictAdapter;
    private static ArrayList<prediction> predictList = new ArrayList<>();
    private static final ArrayList<prediction> allPredictList = new ArrayList<>();


    //AD STUFF___________________________________
    private static final String TAG = "predictActivityTag";
    private AdView adView;
    private static final String adUnitId = "ca-app-pub-3940256099942544/6300978111";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityPredictionsBinding.inflate(getLayoutInflater());

        //DO ADD STUFF HERE
        MobileAds.initialize(this, initializationStatus ->
                Log.d(TAG, "onInitializationComplete:"));
        adView = new AdView(this);
        adView.setAdUnitId(adUnitId);
        binding.predictFrameLayout.addView(adView);
        binding.predictFrameLayout.post(this::loadAdaptiveBanner);
        //END OF ADD STUFF




        LocalTime time = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time = LocalTime.now();
        }
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String text = time.format(formatter);
            binding.predictionsTimeSubTitle.setText(text);
        }

        titleString = getIntent().getStringExtra("title");
        binding.predicitionsTitle.setText(titleString);
        //TODO: GET CURRENT TIME
        //binding.predictionsTimeSubTitle.setText();
        stopName = getIntent().getStringExtra("stopName");
        directionChosen = getIntent().getStringExtra("direction");
        binding.predictionsLocationSubTitle.setText(stopName + " (" + directionChosen + ")");
        setContentView(binding.getRoot());

        routeNum = getIntent().getStringExtra("routeNum");
        stpid = getIntent().getStringExtra("stpid");

        binding.predictionsRec.setLayoutManager(new LinearLayoutManager(this));
        predictAdapter = new predictionsAdapter(this, predictList);
        binding.predictionsRec.setAdapter(predictAdapter);


        predictionsVolley.downloadPredictions(this, routeNum, stpid);

    }


    //AD STUFF_____________________________________
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

    public static void acceptPredictions(ArrayList<prediction> predictListIn) {
        int size = predictList.size();
        predictList.clear();
        predictAdapter.notifyItemRangeRemoved(0, size);
        predictList.addAll(predictListIn);
        predictAdapter.notifyItemRangeChanged(0, predictList.size());
        allPredictList.clear();
        allPredictList.addAll(predictList);
        predictAdapter.notifyItemRangeChanged(0, predictList.size());
    }


}
