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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemsMainAdapter extends RecyclerView.Adapter<ItemsMainAdapter.MyViewHolder> {
    private Context context;
    private List<Item> itemList;



    public ItemsMainAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_main, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Item item = itemList.get(i);
        myViewHolder.txtvName.setText(item.getName());
        myViewHolder.txtvPrice.setText(Common.beautifyPrice(item.getPrice()));

        Glide.with(context).load(item.getImage()).into(myViewHolder.imgView);

        myViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("name", item.getName());
                intent.putExtra("type", item.getType());
                intent.putExtra("price", item.getPrice());
                intent.putExtra("producer", item.getProducer());
                intent.putExtra("describe", item.getDescribe());

                context.startActivity(intent);
            }
        });

        myViewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("name", item.getName());
                intent.putExtra("type", item.getType());
                intent.putExtra("price", item.getPrice());
                intent.putExtra("amount", item.getAmount());
                intent.putExtra("producer", item.getProducer());
                intent.putExtra("describe", item.getDescribe());
                intent.putExtra("image", item.getImage());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtvName, txtvPrice;
        public ImageView imgView;
        public CardView parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvName = (TextView) itemView.findViewById(R.id.item_main_name);
            txtvPrice = (TextView) itemView.findViewById(R.id.item_main_price);
            imgView = (ImageView) itemView.findViewById(R.id.item_main_imgview);
            parentLayout = (CardView) itemView.findViewById(R.id.item_main_card);
        }
    }
}
