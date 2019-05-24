package com.example.bishop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        final History history = historyList.get(i);
        myViewHolder.txtId.setText(String.valueOf(history.getId()));

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA).parse(history.getTime());
            String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
            myViewHolder.txtDate.setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (history.getStatus()) {
            myViewHolder.txtStatus.setText("Đã xử lý");
            myViewHolder.txtStatus.setTextColor(Color.rgb(31, 168, 63));
        } else {
            myViewHolder.txtStatus.setText("Chưa xử lý");
            myViewHolder.txtStatus.setTextColor(Color.RED);
        }

        long sum = 0;

        for (int k = 0; k < history.getItems().size(); k++) {
            sum += history.getItems().get(k).getPrice();
        }

        myViewHolder.txtSumprice.setText(Common.beautifyPrice(sum));

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_list_history, null);


                RecyclerView recyclerView = popupView.findViewById(R.id.popup_recycler);
                List<ItemPopup> itemPopups = new ArrayList<>();
                ItemsPopupAdapter itemsPopupAdapter = new ItemsPopupAdapter(popupView.getContext(), itemPopups);
                LinearLayoutManager linearLayoutManager
                        = new LinearLayoutManager(popupView.getContext(), LinearLayoutManager.VERTICAL, false);

                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(itemsPopupAdapter);

                for (int i = 0; i < history.getItems().size(); i++) {
                    ItemPopup itemPopup = new ItemPopup(
                            ApiUtils.BASE_URL + "/product-image/" + history.getItems().get(i).getProduct().getId(),
                            history.getItems().get(i).getProduct().getName(),
                            history.getItems().get(i).getProduct().getPrice()
                    );

                    itemPopups.add(itemPopup);
                }

                itemsPopupAdapter.notifyDataSetChanged();

                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, false);

                Button btnBuyInsurace = (Button) popupView.findViewById(R.id.btn_popup_his_buy_insurance);

                if (!history.getStatus()) {
                    btnBuyInsurace.setVisibility(View.GONE);
                }

                btnBuyInsurace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        context.startActivity(new Intent(context, InsuraceOrderActivity.class));
                    }
                });

                Button btnClose = (Button) popupView.findViewById(R.id.btn_popup_his_close);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);

            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtId, txtDate, txtStatus, txtSumprice;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txt_history_id);
            txtDate = (TextView) itemView.findViewById(R.id.txt_history_date);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_history_status);
            txtSumprice = (TextView) itemView.findViewById(R.id.txt_history_sumprice);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }
}
