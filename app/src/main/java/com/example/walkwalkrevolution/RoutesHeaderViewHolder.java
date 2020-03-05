package com.example.walkwalkrevolution;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RoutesHeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView headerTitle;

    public RoutesHeaderViewHolder(@NonNull final View view) {
        super(view);
        view.findViewById(R.id.routeHeaderTitle);
    }
}
