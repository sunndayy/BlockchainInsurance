package com.example.bishop;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderConfirmActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView imgBike;
    private TextView tvName, tvProducer, tvType, tvPrice;
    private Button btnConfirm;
    private Spinner spinnerPaymentType, spinnerVisaType, spinnerMonth, spinnerYear;
    private LinearLayout viewZalopay, viewVisa, viewBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgBike = (ImageView) findViewById(R.id.img_bike_order_confirm);

        Glide.with(this)
                .load(Common.cart.get(0).getImage())
                .into(imgBike);

        tvName = findViewById(R.id.txt_name_order_confirm);
        tvProducer = findViewById(R.id.txt_producer_order_confirm);
        tvType = findViewById(R.id.txt_type_order_confirm);
        tvPrice = findViewById(R.id.txt_price_order_confirm);

        tvName.setText(Common.cart.get(0).getName());
        tvProducer.setText(Common.cart.get(0).getProducer());

        int type = Common.cart.get(0).getType();

        if (type == 0) {
            tvType.setText("Xe số");
        } else if (type == 1) {
            tvType.setText("Xe tay ga");
        } else if (type == 2) {
            tvType.setText("Xe côn tay");
        }

        tvPrice.setText("Giá trị: " + Common.beautifyPrice(Common.cart.get(0).getPrice()));

        viewZalopay = findViewById(R.id.payment_zalopay_confirm);
        viewVisa = findViewById(R.id.payment_visa_confirm);
        viewBank = findViewById(R.id.payment_bank_confirm);

        viewZalopay.setVisibility(View.GONE);
        viewVisa.setVisibility(View.GONE);
        viewBank.setVisibility(View.GONE);

        //payment
        spinnerPaymentType = (Spinner) findViewById(R.id.spinner_payment_type);
        spinnerPaymentType.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Thanh toán khi nhận hàng");
        categories.add("Chuyển khoản ngân hàng");
        categories.add("Thanh toán bằng thẻ ghi nợ");
        categories.add("Thanh toán bằng ví zalopay");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPaymentType.setAdapter(dataAdapter);

        //visa
        spinnerVisaType = (Spinner) findViewById(R.id.spinner_visa_type);

        List<String> visaType = new ArrayList<String>();

        visaType.add("Visa");
        visaType.add("Master Card");
        visaType.add("JCB");

        ArrayAdapter<String> visaAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, visaType);

        visaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVisaType.setAdapter(visaAdapter);

        //month expire
        spinnerMonth = (Spinner) findViewById(R.id.spinner_month);

        List<String> month = new ArrayList<String>();

        month.add("T1");
        month.add("T2");
        month.add("T3");
        month.add("T4");
        month.add("T5");
        month.add("T6");
        month.add("T7");
        month.add("T8");
        month.add("T9");
        month.add("T10");
        month.add("T11");
        month.add("T12");

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, month);

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonth.setAdapter(monthAdapter);

        // year expire
        spinnerYear = (Spinner) findViewById(R.id.spinner_year);

        List<String> year = new ArrayList<String>();

        year.add("2019");
        year.add("2020");
        year.add("2021");
        year.add("2022");
        year.add("2023");
        year.add("2024");
        year.add("2025");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, year);

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(yearAdapter);


        btnConfirm = (Button) findViewById(R.id.btn_order_confirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.popup_confirm, null);


                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);

                Button btnYes = (Button) popupView.findViewById(R.id.btn_popup_confirm_yes);
                Button btnNo = (Button) popupView.findViewById(R.id.btn_popup_confirm_no);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                        List<Order> list = new ArrayList<>();

                        for (int i =0; i< Common.cart.size(); i++) {
                            Order order = new Order(Common.cart.get(i).getId(), Common.cart.get(i).getPrice());
                            list.add(order);
                        }

                        Orders orders = new Orders();
                        orders.setItems(list);

                        ApiService apiService = ApiUtils.getApiService();

                        User user = Common.user;

                        apiService.CreateOrder(Common.user.getToken(), orders).enqueue(
                                new Callback<Orders>() {
                                    @Override
                                    public void onResponse(Call<Orders> call, Response<Orders> response) {
                                        if (response.body().getError() == null) {
                                            Toast.makeText(OrderConfirmActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                            Common.cart.clear();
                                            finish();
                                        }
                                        else  {
                                            Toast.makeText(OrderConfirmActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Orders> call, Throwable t) {
                                        Toast.makeText(OrderConfirmActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                viewZalopay.setVisibility(View.GONE);
                viewVisa.setVisibility(View.GONE);
                viewBank.setVisibility(View.GONE);
                break;
            case 1:
                viewZalopay.setVisibility(View.GONE);
                viewVisa.setVisibility(View.GONE);
                viewBank.setVisibility(View.VISIBLE);
                break;
            case 2:
                viewZalopay.setVisibility(View.GONE);
                viewVisa.setVisibility(View.VISIBLE);
                viewBank.setVisibility(View.GONE);
                break;
            case 3:
                viewZalopay.setVisibility(View.VISIBLE);
                viewVisa.setVisibility(View.GONE);
                viewBank.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
