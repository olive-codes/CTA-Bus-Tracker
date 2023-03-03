package com.oliviabecht.obechtbustracker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class mainViewHolder extends RecyclerView.ViewHolder {

    ConstraintLayout itemMainCS;
    TextView mainRouteNum;
    TextView mainRouteName;

    public mainViewHolder(@NonNull View itemView) {
        super(itemView);
        itemMainCS = itemView.findViewById(R.id.itemMainCS);
        mainRouteNum = itemView.findViewById(R.id.itemMainNumber);
        mainRouteName = itemView.findViewById(R.id.itemMainName);
    }

    public ConstraintLayout getConstraintLayout() {
        return itemMainCS;
    }

    public TextView getMainRouteNum() {
        return mainRouteNum;
    }

    public TextView getMainRouteName() {
        return mainRouteName;
    }


}
