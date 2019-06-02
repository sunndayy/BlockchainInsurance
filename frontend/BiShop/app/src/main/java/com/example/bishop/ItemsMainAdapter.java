package com.example.bishop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (view.getId() == R.id.btn_add_favorite) {
                    ApiService apiService = ApiUtils.getApiService();
                    apiService.LikeProduct(Common.user.getToken(), item.getId())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.errorBody() != null) {
                                        Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtvName, txtvPrice;
        public ImageView imgView;
        public ImageView btnAddFavorite;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvName = (TextView) itemView.findViewById(R.id.item_main_name);
            txtvPrice = (TextView) itemView.findViewById(R.id.item_main_price);
            imgView = (ImageView) itemView.findViewById(R.id.item_main_imgview);
            btnAddFavorite = (ImageView) itemView.findViewById(R.id.btn_add_favorite);

            btnAddFavorite.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener l) {
            this.itemClickListener = l;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
