package com.oliviabecht.obechtbustracker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class stopsViewHolder extends RecyclerView.ViewHolder {

    ConstraintLayout itemStopsCS;
    TextView stopsTxtProx;
    TextView stopsTxtAddress;

    public stopsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemStopsCS = itemView.findViewById(R.id.itemStopsCS);
        stopsTxtProx = itemView.findViewById(R.id.itemStopsTxtProx);
        stopsTxtAddress = itemView.findViewById(R.id.itemStopsTxtAddress);

    }

    public ConstraintLayout getConstraintLayout() {
        return itemStopsCS;
    }

    public TextView getStopsTxtProx() {
        return stopsTxtProx;
    }

    public TextView getStopsTxtAddress() {
        return stopsTxtAddress;
    }


}
