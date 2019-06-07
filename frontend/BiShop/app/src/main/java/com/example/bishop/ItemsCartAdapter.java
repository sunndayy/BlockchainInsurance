package com.example.bishop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemsCartAdapter extends RecyclerView.Adapter<ItemsCartAdapter.MyViewHolder> {

    private Context context;
    private List<Item> itemList;

    public ItemsCartAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cart, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Item item = itemList.get(i);
        myViewHolder.itemId.setText(item.getId());
        myViewHolder.itemName.setText(item.getName());
        myViewHolder.itemPrice.setText(Common.beautifyPrice(item.getPrice()));
        myViewHolder.itemProducer.setText(item.getProducer());

        if (item.getType() == 0) {
            myViewHolder.itemType.setText("Xe số");
        } else {
            if (item.getType() == 1) {
                myViewHolder.itemType.setText("Xe tay ga");
            } else {
                if (item.getType() == 2) {
                    myViewHolder.itemType.setText("Xe côn tay");
                }
            }
        }

        Glide.with(context).load(item.getImage()).into(myViewHolder.imgItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName, itemPrice, itemId, itemProducer, itemType;
        public ImageView imgItem;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.txt_item_cart_name);
            itemPrice = (TextView) itemView.findViewById(R.id.txt_item_cart_price);
            itemId = (TextView) itemView.findViewById(R.id.txt_item_cart_id);
            itemType = (TextView) itemView.findViewById(R.id.txt_item_cart_type);
            itemProducer = (TextView) itemView.findViewById(R.id.txt_item_cart_producer);
            imgItem = (ImageView) itemView.findViewById(R.id.ic_item_cart_bike);

            viewBackground = (RelativeLayout) itemView.findViewById(R.id.item_cart_view_background);
            viewForeground = (LinearLayout) itemView.findViewById(R.id.item_cart_view_foreground);
        }
    }
}
