package com.example.bishop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoriesAdapter extends RecyclerView.Adapter<HistoriesAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<History> historyList;
    private List<History> historyListFiltered;

    public HistoriesAdapter(Context context, List<History> historyList, List<History> historyListFiltered) {
        this.context = context;
        this.historyList = historyList;
        this.historyListFiltered = historyListFiltered;
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
        final History history = historyListFiltered.get(i);
        myViewHolder.txtId.setText(String.valueOf(history.getId()));

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA).parse(history.getTime());
            String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
            myViewHolder.txtDate.setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (history.getPoliceInfo() != null) {
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

                    if (history.getPoliceInfo() != null) {

                        if (history.getPoliceInfo().getLicensePlate() == null) {
                            ItemPopup itemPopup = new ItemPopup(
                                    ApiUtils.BASE_URL + "/product-image/" + history.getItems().get(i).getProduct().getId(),
                                    history.getItems().get(i).getProduct().getName(),
                                    history.getItems().get(i).getProduct().getPrice(),
                                    "Đang chờ cấp biển số"
                            );

                            itemPopups.add(itemPopup);
                        } else {
                            ItemPopup itemPopup = new ItemPopup(
                                    ApiUtils.BASE_URL + "/product-image/" + history.getItems().get(i).getProduct().getId(),
                                    history.getItems().get(i).getProduct().getName(),
                                    history.getItems().get(i).getProduct().getPrice(),
                                    history.getPoliceInfo().getLicensePlate()
                            );

                            itemPopups.add(itemPopup);
                        }

                    } else {
                        ItemPopup itemPopup = new ItemPopup(
                                ApiUtils.BASE_URL + "/product-image/" + history.getItems().get(i).getProduct().getId(),
                                history.getItems().get(i).getProduct().getName(),
                                history.getItems().get(i).getProduct().getPrice(),
                                "Chưa có biển số"
                        );

                        itemPopups.add(itemPopup);
                    }
                }

                itemsPopupAdapter.notifyDataSetChanged();

                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, false);

                final LinearLayout linearLayout = (LinearLayout) popupView.findViewById(R.id.info_insurance);

                final Button btnBuyInsurace = (Button) popupView.findViewById(R.id.btn_popup_his_buy_insurance);

                if (history.getPoliceInfo() == null || history.getPoliceInfo().getLicensePlate() == null) {
                    btnBuyInsurace.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
                } else {
                    ApiInsuranceService apiInsuranceService = new Retrofit.Builder()
                            .baseUrl("http://bcinsurence.herokuapp.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(ApiInsuranceService.class);

                    apiInsuranceService.GetInsuranceByLicensePlate(history.getPoliceInfo().getLicensePlate(),
                            Common.user.getToken())
                            .enqueue(new Callback<List<InsuranceOrder>>() {
                                @Override
                                public void onResponse(Call<List<InsuranceOrder>> call, Response<List<InsuranceOrder>> response) {
                                    if (response.body().size() != 0) {
                                        TextView tvInsuranceId = (TextView) popupView.findViewById(R.id.tv_popup_insurance_id);
                                        TextView tvInsuranceCompany = (TextView) popupView.findViewById(R.id.tv_popup_insurance_company);
                                        TextView tvInsuranceTimeStart = (TextView) popupView.findViewById(R.id.tv_popup_insurance_timestart);
                                        TextView tvInsuranceTimeEnd = (TextView) popupView.findViewById(R.id.tv_popup_insurance_timeeend);
                                        TextView tvInsurancePrice = (TextView) popupView.findViewById(R.id.tv_popup_insurance_price);
                                        TextView tvInsurancePer = (TextView) popupView.findViewById(R.id.tv_popup_insurance_percent);
                                        TextView tvInsuranceMaxRefund = (TextView) popupView.findViewById(R.id.tv_popup_insurance_max_refund);

                                        tvInsuranceId.setText(response.body().get(0).getId());
                                        tvInsuranceCompany.setText(response.body().get(0).getCompany());

                                        Date date = new Date(response.body().get(0).getExpireTime().getTimeStart());
                                        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
                                        tvInsuranceTimeStart.setText(str);

                                        date = new Date(response.body().get(0).getExpireTime().getTimeEnd());
                                        str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
                                        tvInsuranceTimeEnd.setText(str);

                                        tvInsurancePrice.setText(Common.beautifyPrice(response.body().get(0).getPlan().getTerm().getPricePerYear()));
                                        tvInsuranceMaxRefund.setText(Common.beautifyPrice(response.body().get(0).getPlan().getTerm().getMaxRefund()));
                                        tvInsurancePer.setText(Common.beautifyPercent(response.body().get(0).getPlan().getTerm().getPercentage()));


                                        btnBuyInsurace.setVisibility(View.GONE);
                                    } else {
                                        linearLayout.setVisibility(View.GONE);
                                    }
                                    popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
                                }

                                @Override
                                public void onFailure(Call<List<InsuranceOrder>> call, Throwable t) {
                                    linearLayout.setVisibility(View.GONE);
                                    popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
                                }
                            });
                }

                btnBuyInsurace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Intent intent = new Intent(context, InsuranceOrderActivity.class);
                        intent.putExtra("bienso", history.getPoliceInfo().getLicensePlate());
                        context.startActivity(intent);
                    }
                });

                Button btnClose = (Button) popupView.findViewById(R.id.btn_popup_his_close);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return historyListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    historyListFiltered = historyList;
                } else {
                    List<History> filteredList = new ArrayList<>();
                    for (History row : historyList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (String.valueOf(row.getId()).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    historyListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = historyListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                historyListFiltered = (ArrayList<History>) results.values;
                notifyDataSetChanged();
            }
        };
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
