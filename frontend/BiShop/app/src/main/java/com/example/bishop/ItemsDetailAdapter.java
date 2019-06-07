package com.example.bishop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemsDetailAdapter extends RecyclerView.Adapter<ItemsDetailAdapter.MyViewHolder> {

    private Context context;
    private List<Item> items;

    public ItemsDetailAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemsDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_detail, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsDetailAdapter.MyViewHolder myViewHolder, int i) {
        Item item = items.get(i);

        myViewHolder.tvName.setText(item.getName());

        Glide.with(context).load(item.getImage()).into(myViewHolder.imgBike);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView imgBike;
        public TextView tvName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBike = (CircleImageView) itemView.findViewById(R.id.ic_item_detail_img);
            tvName = (TextView) itemView.findViewById(R.id.tv_item_detail_name);
        }
    }
}
