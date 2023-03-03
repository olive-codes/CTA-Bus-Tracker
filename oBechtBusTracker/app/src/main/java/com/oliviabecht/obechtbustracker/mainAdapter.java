package com.oliviabecht.obechtbustracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Menu;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mainAdapter extends RecyclerView.Adapter<mainViewHolder> {

    private final ArrayList<Route> routeList;
    private final MainActivity mainActivity;

    public mainAdapter(MainActivity mainActivity, ArrayList<Route> routeList) {
        this.mainActivity = mainActivity;
        this.routeList = routeList;
    }

    @NonNull
    @Override
    public mainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        view.setOnClickListener(mainActivity);
        return new mainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mainViewHolder holder, int position) {
        Route route = routeList.get(position);
        holder.getMainRouteNum().setText(route.getRouteNumber());
        holder.getMainRouteName().setText(route.getRouteName());

        int color = Color.parseColor(route.getRouteColor());

        if (Color.luminance(color) < 0.25) {
            holder.getMainRouteName().setTextColor(Color.WHITE);
            holder.getMainRouteName().setTextColor(Color.WHITE);
        } else {
            holder.getMainRouteName().setTextColor(Color.BLACK);
            holder.getMainRouteName().setTextColor(Color.BLACK);
        }

        holder.getConstraintLayout().setBackgroundColor(color);

        holder.itemMainCS.setOnClickListener(view -> {

            String rName = route.getRouteName();
            String rNum = route.getRouteNumber();
            MainActivity.setDirectionInfo(holder, rName, rNum);
            directionsDownloader.download(route.getRouteNumber(), mainActivity);

        });

    }

    

    @Override
    public int getItemCount() {
        return routeList.size();
    }


}
