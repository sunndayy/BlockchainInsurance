package com.example.bishopadmin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private Context context;
    private List<Item> itemList;

    public ItemsAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtvName, txtvPrice;
        public ImageView imgView;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvName = (TextView) itemView.findViewById(R.id.tv_name_product);
            txtvPrice = (TextView) itemView.findViewById(R.id.tv_price_product);
            imgView = (ImageView) itemView.findViewById(R.id.ic_product);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
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
        final Item item = itemList.get(i);
        myViewHolder.txtvName.setText(item.getName());
        myViewHolder.txtvPrice.setText(Long.toString(item.getPrice()));

        Glide.with(context).load(item.getImage()).into(myViewHolder.imgView);

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Log.d("tag", String.valueOf(position));
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.popup_window, null);

                TextView tvId = (TextView) popupView.findViewById(R.id.popup_id);
                tvId.setText("Ma san pham: " + itemList.get(position).getId());

                TextView tvName = (TextView) popupView.findViewById(R.id.popup_name);
                tvName.setText("Ten san pham: " + itemList.get(position).getName());

                TextView tvDescribe = (TextView) popupView.findViewById(R.id.popup_describe);
                tvDescribe.setText("Mo ta: " + itemList.get(position).getDescribe());

                TextView tvType = (TextView) popupView.findViewById(R.id.popup_type);
                tvType.setText("Loai: " + String.valueOf(itemList.get(position).getType()));

                TextView tvPrice = (TextView) popupView.findViewById(R.id.popup_price);
                tvPrice.setText("Gia: " + String.valueOf(itemList.get(position).getPrice()));

                TextView tvAmount = (TextView) popupView.findViewById(R.id.popup_amount);
                tvAmount.setText("So luong: " + String.valueOf(itemList.get(position).getAmount()));

                TextView tvProducer = (TextView) popupView.findViewById(R.id.popup_producer);
                tvProducer.setText("Nha san xuat: " + itemList.get(position).getProducer());

                ImageView imageView = (ImageView) popupView.findViewById(R.id.popup_image);


                Glide.with(popupView).load(itemList.get(position).getImage())
                        .into(imageView);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
