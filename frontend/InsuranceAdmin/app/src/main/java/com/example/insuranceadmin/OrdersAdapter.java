package com.example.insuranceadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
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
        Order order = orders.get(i);
        myViewHolder.tvId.setText(order.getId());
        myViewHolder.tvCompany.setText(order.getCompany());
//        myViewHolder.tvTimeStart.setText(new Date(order.getTimeStart()).toString());
//        myViewHolder.tvTimeEnd.setText(new Date(order.getTimeEnd()).toString());
        myViewHolder.tvUserID.setText(order.getUserInfo().getIdentityCard());
        myViewHolder.tvBienSo.setText(order.getUserInfo().getLicensePlate());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId, tvCompany, tvTimeStart, tvTimeEnd, tvUserID, tvBienSo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id_order);
            tvCompany = itemView.findViewById(R.id.tv_company_order);
            tvTimeStart = itemView.findViewById(R.id.tv_timestart_order);
            tvTimeEnd = itemView.findViewById(R.id.tv_timeend_order);
            tvUserID = itemView.findViewById(R.id.tv_userid_order);
            tvBienSo = itemView.findViewById(R.id.tv_bienso_order);

        }
    }
}
