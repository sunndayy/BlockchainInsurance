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

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InsuranceOrderActivity extends AppCompatActivity {

    private TextView tvBienSo, tvPrice;
    private Button btnConfirm;
    private Spinner spinner;
    private ImageView btnInfo;
    private ScrollableNumberPicker scrollableNumberPicker;
    private List<InsuranceInfo> insuranceInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvBienSo = (TextView) findViewById(R.id.tv_insurance_bienso);
        tvBienSo.setText(getIntent().getExtras().getString("bienso"));

        tvPrice = (TextView) findViewById(R.id.tv_insurance_price);

        scrollableNumberPicker = (ScrollableNumberPicker) findViewById(R.id.number_picker);
        scrollableNumberPicker.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {
                for (int i = 0; i < insuranceInfos.size(); i++) {
                    if (Common.InsuranceId.equals(insuranceInfos.get(i).getId())) {
                        tvPrice.setText(Common.beautifyPrice(value * insuranceInfos.get(i).getTerm().getPricePerYear()));
                        break;
                    }
                }
            }
        });

        btnInfo = (ImageView) findViewById(R.id.btn_insurance_detail);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View popupView = layoutInflater.inflate(R.layout.popup_insurance_info, null);

                TextView tvId = popupView.findViewById(R.id.popup_info_id);
                TextView tvCompany = popupView.findViewById(R.id.popup_info_company);
                TextView tvPrice = popupView.findViewById(R.id.popup_info_priceperyear);
                TextView tvPercent = popupView.findViewById(R.id.popup_info_percent);
                TextView tvMaxRefund = popupView.findViewById(R.id.popup_info_maxrefund);

                for (int i = 0; i < insuranceInfos.size(); i++) {
                    if (Common.InsuranceId.equals(insuranceInfos.get(i).getId())) {
                        tvId.setText(insuranceInfos.get(i).getId());
                        tvPrice.setText(Common.beautifyPrice(insuranceInfos.get(i).getTerm().getPricePerYear()));
                        tvCompany.setText(insuranceInfos.get(i).getCompany());
                        tvPercent.setText(Common.beautifyPercent(insuranceInfos.get(i).getTerm().getPercentage()));
                        tvMaxRefund.setText(Common.beautifyPrice(insuranceInfos.get(i).getTerm().getMaxRefund()));
                        break;
                    }
                }

                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupView, 600, height, false);

                Button btnClose = (Button) popupView.findViewById(R.id.btn_popup_info_close);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner_insurance);

        insuranceInfos = new ArrayList<>();

        final List<String> categories = new ArrayList<String>();


        final ApiInsuranceService apiInsuranceService = new Retrofit.Builder()
                .baseUrl("http://bcinsurence.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiInsuranceService.class);

        apiInsuranceService.GetInsuranceInfo(Common.user.getToken())
                .enqueue(new Callback<List<InsuranceInfo>>() {
                    @Override
                    public void onResponse(Call<List<InsuranceInfo>> call, Response<List<InsuranceInfo>> response) {
                        if (response.body().size() != 0) {
                            for (int i = 0; i < response.body().size(); i++) {
                                String id = response.body().get(i).getId();
                                categories.add(id);

                                InsuranceInfo item = response.body().get(i);
                                insuranceInfos.add(item);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InsuranceOrderActivity.this,
                                    android.R.layout.simple_spinner_item, categories);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Common.InsuranceId = parent.getItemAtPosition(position).toString();

                                    for (int i = 0; i < insuranceInfos.size(); i++) {
                                        if (Common.InsuranceId.equals(insuranceInfos.get(i).getId())) {
                                            Common.InsuranceCompany = insuranceInfos.get(i).getCompany();
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<InsuranceInfo>> call, Throwable t) {
                        Toast.makeText(InsuranceOrderActivity.this,
                                t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        btnConfirm = (Button) findViewById(R.id.btn_insurance_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContractInfo contractInfo = new ContractInfo();
                contractInfo.setType("CONTRACT");
                contractInfo.getRef().getPlan().setCompany(Common.InsuranceCompany);
                contractInfo.getRef().getPlan().setId(Common.InsuranceId);
                contractInfo.getRef().getUserInfo().setIdentityCard(Common.user.getIdentityCard());
                contractInfo.getRef().getUserInfo().setLicensePlate(tvBienSo.getText().toString());
                contractInfo.getRef().getUserInfo().setName(Common.user.getName());
                contractInfo.getRef().getUserInfo().setAddress(Common.user.getAddress());
                contractInfo.getRef().getUserInfo().setPhoneNumber(Common.user.getPhoneNumber());
                contractInfo.getRef().getUserInfo().setEmail(Common.user.getEmail());

                Date date = new Date();
                Long timestart = date.getTime();
                Long year = Long.valueOf(scrollableNumberPicker.getValue());
                Long timeend = timestart + 31622400000L * year;

                contractInfo.getRef().getExpireTime().setTimeStart(timestart);
                contractInfo.getRef().getExpireTime().setTimeEnd(timeend);

                apiInsuranceService.CreateInsuranceContract(Common.user.getToken(),
                        contractInfo)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(InsuranceOrderActivity.this, "Đặt mua bảo hiểm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(InsuranceOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
}
