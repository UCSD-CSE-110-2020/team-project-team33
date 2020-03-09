package com.example.walkwalkrevolution.ui.main.routeslist;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.routemanagement.Route;

import java.util.Iterator;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public abstract class RouteSection extends Section {
   protected TabActivity tabActivity;
   protected ClickListener clickListener;
   private final String title;

    public RouteSection(TabActivity t, ClickListener clickListener, String title) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_route)
                .headerResourceId(R.layout.routes_header).
                build());
        tabActivity = t;
        this.clickListener = clickListener;
        this.title = title;
   }

    @Override
    public abstract int getContentItemsTotal();

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new RouteItemViewHolder(view);
    }

    @Override
    public abstract void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position);

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new RoutesHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        RoutesHeaderViewHolder headerHolder = (RoutesHeaderViewHolder) holder;

        headerHolder.headerTitle.setText(title);
    }

    public abstract void setRoutes(Iterator it);

    public String formatTime(long duration) {
        int seconds = (int)(duration % 60);
        int minutes = (int)((duration / 60) % 60);
        int hours = (int)(duration / (60 * 60)) % 24;
        return formatDigits(hours) + ":" + formatDigits(minutes) + ":" + formatDigits(seconds);
    }

    private String formatDigits(int x){
        return x < 10 ? "0" + x : String.valueOf(x);
    }

    public interface ClickListener {
        void onItemRootViewClicked(Route route);
    }

}
