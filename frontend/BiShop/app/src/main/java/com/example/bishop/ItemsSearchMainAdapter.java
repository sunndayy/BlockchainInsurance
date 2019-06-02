package com.example.bishop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ItemsSearchMainAdapter extends RecyclerView.Adapter<ItemsSearchMainAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<ItemSearchMain> itemSearchMains;
    private List<ItemSearchMain> itemSearchMainsFiltered;


    public ItemsSearchMainAdapter(Context context, List<ItemSearchMain> itemSearchMains,
                                  List<ItemSearchMain> itemSearchMainsFiltered) {
        this.context = context;
        this.itemSearchMains = itemSearchMains;
        this.itemSearchMainsFiltered = itemSearchMainsFiltered;
    }


    @NonNull
    @Override
    public ItemsSearchMainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_search, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsSearchMainAdapter.MyViewHolder myViewHolder, final int i) {
        final ItemSearchMain item = itemSearchMainsFiltered.get(i);
        Glide.with(context).load(item.getImage()).into(myViewHolder.imgItem);
        myViewHolder.tvPrice.setText(item.getPrice());
        myViewHolder.tvName.setText(item.getName());

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                for (Item k : Common.allItems) {
                    if (k.getName().equals(item.getName())) {
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra("id", k.getId());
                        intent.putExtra("name", k.getName());
                        intent.putExtra("type", k.getType());
                        intent.putExtra("price", k.getPrice());
                        intent.putExtra("amount", k.getAmount());
                        intent.putExtra("producer", k.getProducer());
                        intent.putExtra("describe", k.getDescribe());
                        intent.putExtra("image", k.getImage());

                        context.startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemSearchMainsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    itemSearchMainsFiltered = itemSearchMains;
                } else {
                    List<ItemSearchMain> filteredList = new ArrayList<>();
                    for (ItemSearchMain row : itemSearchMains) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    itemSearchMainsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemSearchMainsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemSearchMainsFiltered = (ArrayList<ItemSearchMain>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgItem;
        public TextView tvName, tvPrice;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = (ImageView) itemView.findViewById(R.id.img_bike);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener l) {
            this.itemClickListener = l;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }


}
