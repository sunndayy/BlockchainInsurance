package com.example.bishopadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList =orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_order, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Order order = orderList.get(i);
        myViewHolder.tvId.setText(order.getId());
        myViewHolder.tvDate.setText(order.getDate());
        myViewHolder.tvPrice.setText(order.getPrice());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId, tvPrice, tvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id_order);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price_order);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_order);
        }
    }
}
