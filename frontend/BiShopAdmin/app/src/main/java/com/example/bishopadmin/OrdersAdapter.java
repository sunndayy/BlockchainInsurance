package com.example.bishopadmin;

import android.content.Context;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
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

        Long total = Long.valueOf(0);
        for (int k = 0; k < order.getItems().size(); k++) {
            total += Long.valueOf(order.getItems().get(k).getPrice());
        }

        myViewHolder.tvPrice.setText(Common.beautifyPrice(total));
        myViewHolder.tvUser.setText(order.getUser().getName());
        myViewHolder.tvPhone.setText(order.getUser().getPhoneNumber());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA).parse(order.getTime());
            String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
            myViewHolder.tvDate.setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myViewHolder.checkBoxPay.setChecked(order.getStatus());

        myViewHolder.setButtonItemClickListener(new ButtonItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_info_order, null);

                ImageView imgItem = (ImageView) popupView.findViewById(R.id.ic_popup_order_item);
                Glide.with(context).load(ApiUtils.BASE_URL + "/product-image/"
                        + order.getItems().get(0).getProduct().getId()).into(imgItem);

                TextView tvId = (TextView) popupView.findViewById(R.id.tv_popup_order_id);
                tvId.setText(order.getItems().get(0).getProduct().getId());

                TextView tvName = (TextView) popupView.findViewById(R.id.tv_popup_order_name);
                tvName.setText(order.getItems().get(0).getProduct().getName());

                TextView tvType = (TextView) popupView.findViewById(R.id.tv_popup_order_type);

                if (order.getItems().get(0).getProduct().getType() == 0) {
                    tvType.setText("Xe số");
                } else {
                    if (order.getItems().get(0).getProduct().getType() == 1) {
                        tvType.setText("Xe tay ga");
                    } else {
                        if (order.getItems().get(0).getProduct().getType() == 2) {
                            tvType.setText("Xe côn tay");
                        }
                    }
                }

                TextView tvPrice = (TextView) popupView.findViewById(R.id.tv_popup_order_price);
                tvPrice.setText(Common.beautifyPrice(order.getItems().get(0).getProduct().getPrice()));

                TextView tvProducer = (TextView) popupView.findViewById(R.id.tv_popup_order_producer);
                tvProducer.setText(order.getItems().get(0).getProduct().getProducer());

                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, false);

                Button btnClose = (Button) popupView.findViewById(R.id.btn_popup_order_close);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            }
        });

        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_comfirm_order, null);

                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, focusable);

                final Spinner spinner = (Spinner) popupView.findViewById(R.id.spinner_popup);
                final List<String> categories = new ArrayList<String>();

                ApiInsurance apiInsurance = new Retrofit.Builder()
                        .baseUrl("http://bcinsurence.herokuapp.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(ApiInsurance.class);

                apiInsurance.GetInsuranceInfo(Common.AccessToken)
                        .enqueue(new Callback<List<InsuranceInfo>>() {
                            @Override
                            public void onResponse(Call<List<InsuranceInfo>> call, Response<List<InsuranceInfo>> response) {
                                if (response.body().size() != 0) {
                                    for (int i = 0; i < response.body().size(); i++) {
                                        String id = response.body().get(i).getId();
                                        categories.add(id);
                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(popupView.getContext(), android.R.layout.simple_spinner_item, categories);
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner.setAdapter(dataAdapter);
                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            Common.Buffer = parent.getItemAtPosition(position).toString();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<List<InsuranceInfo>> call, Throwable t) {
                                Toast.makeText(popupView.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                Button btnYes = (Button) popupView.findViewById(R.id.btn_confirm_yes);
                Button btnNo = (Button) popupView.findViewById(R.id.btn_confirm_no);

                final EditText edtBienSo = (EditText) popupView.findViewById(R.id.edt_popup_bienso);
                final EditText edtDur = (EditText) popupView.findViewById(R.id.edt_popup_dur);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ContractInfo contractInfo = new ContractInfo();
                        contractInfo.setContractId(Common.Buffer);
                        contractInfo.setCompany("phhoang");
                        contractInfo.setDuration(Integer.parseInt(edtDur.getText().toString()));
                        contractInfo.setLicensePlate(edtBienSo.getText().toString());
                        contractInfo.setStatus(true);

                        ApiService apiService = ApiUtils.getApiService();

                        apiService.UpdateOrders(order.getId(), Common.AccessToken, contractInfo)
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(popupView.getContext(), "Successful", Toast.LENGTH_SHORT).show();
                                        myViewHolder.checkBoxPay.setChecked(true);
                                        popupWindow.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(popupView.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    }
                                });
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
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
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvId, tvPrice, tvUser, tvPhone, tvDate;
        public CheckBox checkBoxPay;
        private ImageView btnInfo;
        private ItemClickListener itemClickListener;
        private ButtonItemClickListener buttonItemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id_order);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price_order);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date_order);
            tvUser = (TextView) itemView.findViewById(R.id.tv_user_order);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_userphone_order);

            checkBoxPay = (CheckBox) itemView.findViewById(R.id.check_pay);

            btnInfo = (ImageView) itemView.findViewById(R.id.btn_info_order);

            btnInfo.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setButtonItemClickListener(ButtonItemClickListener buttonItemClickListener) {
            this.buttonItemClickListener = buttonItemClickListener;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_info_order:
                    buttonItemClickListener.onClick(v, getAdapterPosition());
                    break;

                case R.id.item_order:
                    itemClickListener.onClick(v, getAdapterPosition());
                    break;
            }
        }
    }
}