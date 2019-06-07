package com.example.bishop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemsPopupAdapter  extends RecyclerView.Adapter<ItemsPopupAdapter.MyViewHolder> {
    private Context context;
    private List<ItemPopup> itemPopups;

    public ItemsPopupAdapter(Context context, List<ItemPopup> itemPopups) {
        this.context = context;
        this.itemPopups = itemPopups;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_popup, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ItemPopup itemPopup = itemPopups.get(i);
        myViewHolder.tvName.setText(itemPopup.getName());
        myViewHolder.tvPrice.setText(Common.beautifyPrice(itemPopup.getPrice()));
        myViewHolder.tvBienSo.setText(itemPopup.getBienSo());
        Glide.with(context).load(itemPopup.getImage()).into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemPopups.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvPrice, tvBienSo;
        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.txt_item_popup_name);
            tvPrice = (TextView) itemView.findViewById(R.id.txt_item_popup_price);
            tvBienSo = (TextView) itemView.findViewById(R.id.txt_item_popup_bienso);
            imageView = (ImageView) itemView.findViewById(R.id.ic_popup_image);
        }
    }

}
