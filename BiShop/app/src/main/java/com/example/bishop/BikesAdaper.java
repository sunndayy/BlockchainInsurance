package com.example.bishop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class BikesAdaper extends RecyclerView.Adapter<BikesAdaper.MyViewHolder> {
    private Context mContext;
    private List<Bike> bikeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        public CardView parent_layout;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            parent_layout = (CardView) view.findViewById(R.id.card_view);
        }
    }


    public BikesAdaper(Context mContext, List<Bike> bikeList) {
        this.mContext = mContext;
        this.bikeList = bikeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bike_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Bike bike = bikeList.get(position);
        holder.title.setText(bike.getmName());
        holder.count.setText(Application.beaufityPrice(bike.getmPrice()));

        // loading album cover using Glide library
        Glide.with(mContext).load(bike.getMthumbnail()).into(holder.thumbnail);


        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, BikeDetailActivity.class));
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, BikeDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }
}
