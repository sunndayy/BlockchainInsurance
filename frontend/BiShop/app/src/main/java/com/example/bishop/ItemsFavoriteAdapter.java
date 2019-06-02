package com.example.bishop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsFavoriteAdapter extends RecyclerView.Adapter<ItemsFavoriteAdapter.MyViewHolder> {
    private Context context;
    private List<Item> items;

    public ItemsFavoriteAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemsFavoriteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_favorite, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsFavoriteAdapter.MyViewHolder myViewHolder, int i) {
        final Item item = items.get(i);
        myViewHolder.tvName.setText(item.getName());
        myViewHolder.tvPrice.setText(Common.beautifyPrice(item.getPrice()));
        Glide.with(context).load(item.getImage())
                .into(myViewHolder.imgItem);

        myViewHolder.setOptionClickListener(new OptionClickListener() {
            @Override
            public void onOptionClickLisntener(View v, int position) {
                switch (v.getId()) {
                    case R.id.btn_cart_fav:
                        if (Common.cart.size() > 0) {
                            Toast.makeText(context, "Bạn chỉ được chọn tối đa 1 sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                        } else {
                            Common.cart.add(item);
                            Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.btn_delete_fav:
                        ApiService apiService = ApiUtils.getApiService();
                        apiService.UnLikeProduct(Common.user.getToken(), item.getId())
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.errorBody() != null) {
                                            Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName, tvPrice;
        public ImageView btnDelete, btnAddCart, imgItem;

        private OptionClickListener optionClickListener;

        public void setOptionClickListener(OptionClickListener o) {
            this.optionClickListener = o;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName_fav);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice_fav);
            imgItem = (ImageView) itemView.findViewById(R.id.img_bike_fav);

            btnAddCart = (ImageView) itemView.findViewById(R.id.btn_cart_fav);
            btnDelete = (ImageView) itemView.findViewById(R.id.btn_delete_fav);

            btnAddCart.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.optionClickListener.onOptionClickLisntener(v, getAdapterPosition());
        }
    }

    public interface OptionClickListener {
        void onOptionClickLisntener(View v, int position);
    }
}
