package com.example.bishopadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Item> itemList;
    private List<Item> itemListFiltered;

    public ItemsAdapter(Context context, List<Item> itemList, List<Item> itemListFiltered) {
        this.context = context;
        this.itemList = itemList;
        this.itemListFiltered = itemListFiltered;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    itemListFiltered = itemList;
                } else {
                    List<Item> filteredList = new ArrayList<>();
                    for (Item row : itemList) {

                        if (row.getId().toLowerCase().contains(charString.toLowerCase())
                                || row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    itemListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemListFiltered = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        };
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
        final Item item = itemListFiltered.get(i);
        myViewHolder.txtvName.setText(item.getName());
        myViewHolder.txtvPrice.setText(Common.beautifyPrice(item.getPrice()));

        Glide.with(context).load(item.getImage()).into(myViewHolder.imgView);

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.popup_window, null);

                TextView tvId = (TextView) popupView.findViewById(R.id.popup_id);
                tvId.setText(itemListFiltered.get(position).getId());

                TextView tvName = (TextView) popupView.findViewById(R.id.popup_name);
                tvName.setText(itemListFiltered.get(position).getName());

                TextView tvDescribe = (TextView) popupView.findViewById(R.id.popup_describe);
                tvDescribe.setText(itemListFiltered.get(position).getDescribe());

                TextView tvType = (TextView) popupView.findViewById(R.id.popup_type);

                if (itemListFiltered.get(position).getType() == 0) {
                    tvType.setText("Xe số");
                } else {
                    if (itemListFiltered.get(position).getType() == 1) {
                        tvType.setText("Xe tay ga");
                    } else {
                        if (itemListFiltered.get(position).getType() == 2) {
                            tvType.setText("Xe côn tay");
                        }
                    }
                }

                TextView tvPrice = (TextView) popupView.findViewById(R.id.popup_price);
                tvPrice.setText(Common.beautifyPrice(itemListFiltered.get(position).getPrice()));

                TextView tvProducer = (TextView) popupView.findViewById(R.id.popup_producer);
                tvProducer.setText(itemListFiltered.get(position).getProducer());

                ImageView imageView = (ImageView) popupView.findViewById(R.id.popup_image);

                Glide.with(popupView).load(itemListFiltered.get(position).getImage())
                        .into(imageView);

                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, false);

                Button btnClose = (Button) popupView.findViewById(R.id.btn_popupwindow_close);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }
}
