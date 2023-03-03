package com.oliviabecht.obechtbustracker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class predictionsViewHolder extends RecyclerView.ViewHolder {

    ConstraintLayout itemPredictCS;
    TextView predictTxtTime;
    TextView predictTxtMinutes;
    TextView predictTxtDirection;
    TextView predictTxtBusNumber;

    public predictionsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemPredictCS = itemView.findViewById(R.id.itemStopsCS);
        predictTxtTime = itemView.findViewById(R.id.itemPredictTxtTime);
        predictTxtMinutes = itemView.findViewById(R.id.itemPredictTxtMinutes);
        predictTxtDirection = itemView.findViewById(R.id.itemPredictTxtDirection);
        predictTxtBusNumber = itemView.findViewById(R.id.itemPredictTxtBusNumber);

    }

    public ConstraintLayout getConstraintLayout() {
        return itemPredictCS;
    }

    public TextView getPredictTxtTime() {
        return predictTxtTime;
    }

    public TextView getPredictTxtMinutes() {
        return predictTxtMinutes;
    }

    public TextView getPredictTxtDirection() {
        return predictTxtDirection;
    }

    public TextView getPredictTxtBusNumber() {
        return predictTxtBusNumber;
    }


}
