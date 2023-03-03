package com.oliviabecht.obechtbustracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.oliviabecht.obechtbustracker.databinding.ActivityMainBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private ActivityMainBinding binding;
    private final ArrayList<Route> allRouteList = new ArrayList<>();
    private final ArrayList<Route> routeList = new ArrayList<>();
    private mainAdapter routeAdapter;
    public static SimpleDateFormat timeFormat;

    //DIRECTION STUFF
    private static ArrayList<String> directionsList = new ArrayList<>();
    private static String directionChosen;
    private static String routeNameChosen;
    private static String routeNumChosen;
    private static mainViewHolder directionHolder;
    //END OF DIRECTION STUFF

    //LOCATION STUFF
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private static String locationString = "Unspecified Location";
    private static TextView userLocation;
    private static String userZip;
    public static Double userLat;
    public static Double userLon;
    //END OF LOCATION STUFF



    //ADD STUFF________________________________________________________________________________
    private static final String TAG = "MainActivityTag";
    private AdView adView;
    private static final String adUnitId = "ca-app-pub-3940256099942544/6300978111";


    //END OF ADD STUFF_________________________________________________________________________


    //START OF ONCREATE________________________________________________________________________________________________________________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        //DO ADD STUFF HERE

        MobileAds.initialize(this, initializationStatus ->
                Log.d(TAG, "onInitializationComplete:"));
        adView = new AdView(this);
        adView.setAdUnitId(adUnitId);
        binding.mainAdFrame.addView(adView);
        binding.mainAdFrame.post(this::loadAdaptiveBanner);

        //END OF ADD STUFF


        setContentView(binding.getRoot());



        //LOCATION STUFF
        userLocation = findViewById(R.id.mainUserAddress);
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        determineLocation();
        //END OF LOCATION STUFF

        doNetworkCheck();
        if (!doNetworkCheck()) {
            networkAlertDialog();
        }


        timeFormat =
                new SimpleDateFormat("MM-dd-yyyy-HH:mm", Locale.getDefault());

        routeList.clear();
        binding.mainRec.setLayoutManager(new LinearLayoutManager(this));

        routeAdapter = new mainAdapter(this, routeList);
        binding.mainRec.setAdapter(routeAdapter);

        binding.mainTxtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Route> temp = new ArrayList<>();
                for (Route r : allRouteList) {
                    if (r.getRouteName().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            r.getRouteNumber().toLowerCase().contains(charSequence.toString().toLowerCase()))
                        temp.add(r);
                }
                int size = routeList.size();
                MainActivity.this.routeList.clear();
                routeAdapter.notifyItemRangeRemoved(0, size);

                MainActivity.this.routeList.addAll(temp);
                routeAdapter.notifyItemRangeChanged(0, routeList.size());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        RoutesVolley.downloadRoutes(this);

        binding.mainAlertIcon.setOnClickListener( view -> attribution());

    }

    //OUTSIDE OF ONCREATE ____________________________________________________________________________________________________________________

    public void acceptRoutes(ArrayList<Route> routeListIn) {
        int size = routeList.size();
        this.routeList.clear();
        routeAdapter.notifyItemRangeRemoved(0, size);
        this.routeList.addAll(routeListIn);
        routeAdapter.notifyItemRangeChanged(0, routeList.size());
        this.allRouteList.clear();
        this.allRouteList.addAll(this.routeList);
        routeAdapter.notifyItemRangeChanged(0, routeList.size());
    }

    public void clearSearch(View v) {
        binding.mainTxtInput.setText("");
    }

    @Override
    public void onClick(View view) {
        int pos = binding.mainRec.getChildLayoutPosition(view);
        Route selectedRoute = routeList.get(pos);

        Toast.makeText(
                this, selectedRoute.getRouteName() + " selected",
                Toast.LENGTH_SHORT).show();
    }


    //DO ADD STUFF HERE ________________________________________________________________________________________________________

    private void loadAdaptiveBanner() {

        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        // Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        adView.setAdListener(new BannerAdListener());

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


    //////

    static class BannerAdListener extends AdListener {
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

    //ALERT DIALOG STUFF FOR INFORMATION BUTTON________________________________________________________________________________________________________
    public void attribution () {

        //TODO: UPDATE ICON

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.alert_dialog_attribution);
        dialog.setCanceledOnTouchOutside(false);

        TextView TxtOkay = (TextView) dialog.findViewById(R.id.alertOkay);
        TxtOkay.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
        });

        TextView TxtURL = (TextView) dialog.findViewById(R.id.alertURL);
        TxtURL.setOnClickListener( v -> {
            String URL = "https://www.transitchicago.com/developers/bustracker/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(URL));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(i);
        });
        dialog.show();
    }
    //ALERT DIALOG STUFF ENDS HERE FOR INFO BUTTON___________________________________________________________________________________________________________________________

    //ALERT DIALOG NETWORK________________________________________________________________________________________________________________________________________________
    private boolean doNetworkCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            Toast.makeText(this, "Cannot access Connectivity Manager", Toast.LENGTH_SHORT).show();
            return false;
        }
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork == null) ? false : activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void networkAlertDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Bus Tracker - CTA");
        builder.setMessage("Unable to contact Bus Tracker API due to network problem. Please check your network connection.");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
            //close the alert dialog box
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //END OF ALERT DIALOG NETWORK_____________________________________________________________________________________________________________________________________

    //USER LOCATION STUFF HERE____________________________________________________________________________________________________________________________________

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                    return;
                }
            }
        }
        locationAlertDialog();
    }


    private void determineLocation() {
        if(checkMyLocPermission()) {
            Log.d(TAG, "determineLocation: I have a permission!");
            mFusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }

                @Override
                public boolean isCancellationRequested() {
                    return false;
                }
            }).addOnSuccessListener(this, location -> {
                if (location != null) {
                    Log.d(TAG, "determineLocation: " + location.getLatitude() + ", " + location.getLatitude());
                    locationString = getPlace(location);
                    ((TextView) findViewById(R.id.mainUserAddress)).setText(locationString);
                }
            }).addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                    e.getMessage(), Toast.LENGTH_LONG).show());
        }
        else {
            locationAlertDialog();
            Log.d(TAG, "determineLocation: I DO NOT have a permission! ");
        }
    }

    public String getPlace(Location loc) {
        Log.d(TAG, "getPlace: ");
        StringBuilder sb = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            Log.d(TAG, "getPlace: TRY");
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String postalCode = addresses.get(0).getPostalCode();
            userZip = postalCode;
            userLat = addresses.get(0).getLatitude();
            userLon = addresses.get(0).getLongitude();
            sb.append(String.format(
                    Locale.getDefault(),
                    "%s",
                    address));
        } catch (IOException e) {
            locationAlertDialog();
            sb.append("No address found");
            e.printStackTrace();
        }
        return sb.toString();
    }

    private boolean checkMyLocPermission() {
        Log.d(TAG, "checkMyLocPermission: ");
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkMyLocPermission: NOT YET GRANTED!");
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            locationAlertDialog();
            return false;
        }
        return true;
    }
    //END OF USER LOCATION STUFF________________________________________________________________________________________________________________________________________

    //ALERT DIALOG LOCATION__________________________________________________________________________________________________________________________________________
    public void locationAlertDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Bus Tracker - CTA");
        builder.setMessage("Unable to determine device location. Please allow this app to access this device location.");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
            //close the alert dialog box
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //END OF ALERT DIALOG LOCATION__________________________________________________________________________________________________________________________________________


    //DIRECTION STUFF__________________________________________________________________________________________________________________________________

    public static void setDirectionInfo(mainViewHolder holder, String rName, String rNum) {
        directionHolder = holder;
        routeNameChosen = rName;
        routeNumChosen = rNum;
    }

    public static void buildPopup(ArrayList<String> directList, MainActivity mainActivityIn) {

        PopupMenu popupMenu = new PopupMenu(mainActivityIn, directionHolder.itemView);
        popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, directList.get(0));
        popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, directList.get(1));

        popupMenu.setOnMenuItemClickListener(menuItem -> {

            directionChosen = menuItem.toString();

            Intent stopIntent = new Intent(mainActivityIn, StopsActivity.class);
            stopIntent.putExtra("rname", routeNameChosen);
            stopIntent.putExtra("rnum", routeNumChosen);
            stopIntent.putExtra("rdirection", directionChosen);
            stopIntent.putExtra("userLat", userLat);
            stopIntent.putExtra("userLon", userLon);
            //can get lat and lon as well to compare against lat on lon
            //also need m difference in what direction
            stopIntent.putExtra("userZ", userZip);
            mainActivityIn.startActivity(stopIntent);
            return true;

        });
        popupMenu.show();
    }
    //END OF DIRECTION STUFF_________________________________________________________________________________________________________________




}