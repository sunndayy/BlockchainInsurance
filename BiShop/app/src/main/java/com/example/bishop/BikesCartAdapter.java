package com.example.bishop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

public class BikesCartAdapter extends RecyclerView.Adapter<BikesCartAdapter.MyViewHolder> {

    public Context context;
    public List<Bike> bikeList;

    public BikesCartAdapter(Context mContext, List<Bike> bikeList) {
        this.context = mContext;
        this.bikeList = bikeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bike_cart, viewGroup, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Bike bike = bikeList.get(i);
        myViewHolder.bikeName.setText(bike.getmName());
        myViewHolder.bikePrice.setText(bike.getmPrice() + "đ");


        // loading album cover using Glide library
        Glide.with(context).load(bike.getMthumbnail()).into(myViewHolder.imgBike);
    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    public void removeItem(int position) {
        bikeList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Bike bike, int position) {
        bikeList.add(position, bike);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView bikeName, bikePrice;
        public ImageView imgBike;
        public RelativeLayout viewBackground, viewForeground;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bikeName = (TextView) itemView.findViewById(R.id.txtbikename);
            bikePrice = (TextView) itemView.findViewById(R.id.txtprice);
            imgBike = (ImageView) itemView.findViewById(R.id.imgbikecart);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }
}
