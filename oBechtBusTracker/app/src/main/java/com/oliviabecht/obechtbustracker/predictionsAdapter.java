package com.oliviabecht.obechtbustracker;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class predictionsAdapter extends RecyclerView.Adapter<predictionsViewHolder> {

    private final ArrayList<prediction> predictList;
    private final PredictionsActivity predictionsActivity;

    public predictionsAdapter(PredictionsActivity predictionsActivity, ArrayList<prediction> predictList) {
        this.predictionsActivity = predictionsActivity;
        this.predictList = predictList;
    }

    @NonNull
    @Override
    public predictionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_predictions, parent, false);
        return new predictionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull predictionsViewHolder holder, int position) {

        //TODO: SET INFORMATION FOR EACH ITEM
        prediction predict = predictList.get(position);

        holder.getPredictTxtBusNumber().setText("Bus #" + predict.getPredictvid());
        holder.getPredictTxtDirection().setText(predict.getPredictrtdir() + " to " + predict.getPredictdes());
        holder.getPredictTxtMinutes().setText("Due in " + predict.getPredictprdctdn() + " mins at");

        String timeString = predict.getPredictprdtm();
        String timeSubString = timeString.substring(timeString.indexOf(" ") + 1);
        String hourSubString = timeSubString.substring(0, timeSubString.indexOf(":"));
        Double hour = Double.parseDouble(hourSubString);
        String marker = "AM";
        if (hour >= 12) {
            marker = "PM";
        }
        holder.getPredictTxtTime().setText(timeSubString + marker);

    }

    @Override
    public int getItemCount() {
        return predictList.size();
    }



}
