package com.oliviabecht.obechtbustracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class stopsAdapter extends RecyclerView.Adapter<stopsViewHolder> {

    private final ArrayList<Stop> stopList;
    private final StopsActivity stopsActivity;

    public stopsAdapter(StopsActivity stopsActivity, ArrayList<Stop> stopList) {
        this.stopsActivity = stopsActivity;
        this.stopList = stopList;
    }

    @NonNull
    @Override
    public stopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stops, parent, false);
        view.setOnClickListener(stopsActivity);
        return new stopsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull stopsViewHolder holder, int position) {

        //TODO: SET INFORMATION FOR EACH ITEM

        Stop stop = stopList.get(position);
        holder.getStopsTxtAddress().setText(stop.getSName());
        holder.getStopsTxtProx().setText(stop.getDistanceBetweenUser() + " m " + stop.getDirection() + " of your location.");
        //have lat and lon of stop


        holder.itemStopsCS.setOnClickListener(view -> {

            //TODO: GO TO PREDICTIONS ACTIVITY AND BRING DATA OVER
            StopsActivity.goToPredictActivity(stopsActivity, stop.getSName(), stop.getSID());

        });

    }



    @Override
    public int getItemCount() {
        return stopList.size();
    }


}

