package com.example.walkwalkrevolution.ui.main.routeslist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.R;


public class RoutesHeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView headerTitle;

    public RoutesHeaderViewHolder(@NonNull final View view) {
        super(view);
        headerTitle = view.findViewById(R.id.routeHeaderTitle);
    }
}
