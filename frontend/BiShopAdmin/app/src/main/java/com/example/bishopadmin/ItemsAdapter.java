package com.example.bishopadmin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private Context context;
    private List<Item> itemList;

    public ItemsAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtvName, txtvPrice;
        public ImageView imgView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvName = (TextView) itemView.findViewById(R.id.tv_name_product);
            txtvPrice = (TextView) itemView.findViewById(R.id.tv_price_product);
            imgView = (ImageView) itemView.findViewById(R.id.ic_product);
        }
    }

    @NonNull
    @Override
    public ItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_product, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.MyViewHolder myViewHolder, int i) {
        Item item = itemList.get(i);
        myViewHolder.txtvName.setText(item.getName());
        myViewHolder.txtvPrice.setText(Long.toString(item.getPrice()));

        Glide.with(context).load(item.getImage()).into(myViewHolder.imgView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
