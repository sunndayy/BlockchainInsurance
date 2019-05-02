package com.example.bishop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HistoriesAdapter extends RecyclerView.Adapter<HistoriesAdapter.MyViewHolder> {
    private Context context;
    private List<History> historyList;

    public HistoriesAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_history, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        History history = historyList.get(i);
        myViewHolder.txtId.setText(history.getId());
        myViewHolder.txtPrice.setText(Common.beautifyPrice(history.getTotalPrice()));
        myViewHolder.txtDate.setText(history.getDate());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtId, txtDate, txtPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txt_history_id);
            txtDate = (TextView) itemView.findViewById(R.id.txt_history_date);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_history_totalprice);
        }
    }
}
