package com.example.bishopadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final Order order = orderList.get(i);
        myViewHolder.tvId.setText(String.valueOf(order.getId()));
        myViewHolder.tvDate.setText(order.getTime());

        Long total = Long.valueOf(0);
        for (int k = 0; k < order.getItems().size(); k++) {
            total += Long.valueOf(order.getItems().get(k).getPrice());
        }

        myViewHolder.tvPrice.setText(String.valueOf(total));

        myViewHolder.checkBoxPay.setChecked(order.getStatus());

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_comfirm_order, null);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                Button btnYes = (Button) popupView.findViewById(R.id.btn_confirm_yes);
                Button btnNo = (Button) popupView.findViewById(R.id.btn_confirm_no);

                final EditText edtBienSo = (EditText) popupView.findViewById(R.id.edt_popup_bienso);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ApiService apiService = ApiUtils.getApiService();

                        apiService.UpdateOrders(
                                order.getId(),
                                Common.AccessToken,
                                true,
                                edtBienSo.getText().toString(),
                                "phhoang",
                                "BH1",
                                1
                                ).enqueue(
                                new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(popupView.getContext(), "successful", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(popupView.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        myViewHolder.checkBoxPay.setChecked(true);
                        popupWindow.dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                Spinner spinner = (Spinner) popupView.findViewById(R.id.spinner_popup);

                List<String> categories = new ArrayList<String>();
                categories.add("Bảo hiểm mất cắp loại 1");
                categories.add("Bảo hiểm mất cắp loại 2");
                categories.add("Bảo hiểm hư hỏng loại 1");
                categories.add("Bảo hiểm hư hỏng loại 1");

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(popupView.getContext(), android.R.layout.simple_spinner_item, categories);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // On selecting a spinner item
                        String item = parent.getItemAtPosition(position).toString();

                        // Showing selected spinner item
                        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvId, tvPrice, tvDate;
        public CheckBox checkBoxPay;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id_order);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price_order);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_order);
            checkBoxPay = (CheckBox) itemView.findViewById(R.id.check_pay);

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
}
