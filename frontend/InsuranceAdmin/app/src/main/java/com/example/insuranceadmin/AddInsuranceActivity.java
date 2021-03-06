package com.example.insuranceadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInsuranceActivity extends AppCompatActivity {

    private Button btnAdd;
    private EditText tvId, tvCompany, tvPrice, tvMaxRefund;
    private TextView tvPer;
    private CrystalSeekbar crystalSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_insurance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvId = (EditText) findViewById(R.id.edt_add_id);
        tvCompany = (EditText) findViewById(R.id.edt_add_company);
        tvPrice = (EditText) findViewById(R.id.edt_add_price);
        tvPer = (TextView) findViewById(R.id.tv_add_percent);
        tvMaxRefund = (EditText) findViewById(R.id.edt_add_maxrefund);

        crystalSeekbar = (CrystalSeekbar) findViewById(R.id.percent_pick);
        crystalSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                tvPer.setText(String.valueOf(value));
            }
        });

        btnAdd = (Button) findViewById(R.id.btn_form_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = ApiUtils.getApiService();
                InsurancePost insurancePost = new InsurancePost();
                insurancePost.setType("PLAN");
                insurancePost.getRef().setCompany(tvCompany.getText().toString());
                insurancePost.getRef().setId(tvId.getText().toString());
                insurancePost.getAction().getCreate().getTerm().setState(true);
                insurancePost.getAction().getCreate().getTerm().setMaxRefund(Integer.parseInt(tvMaxRefund.getText().toString()));
                insurancePost.getAction().getCreate().getTerm().setPricePerYear(Integer.parseInt(tvPrice.getText().toString()));

                Double per = Double.parseDouble(tvPer.getText().toString());
                per = per / 100;
                insurancePost.getAction().getCreate().getTerm().setPercentage(per);

                apiService.CreateInsurance(Common.AccessToken, insurancePost)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(AddInsuranceActivity.this,
                                        "Thêm gói bảo hiểm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(AddInsuranceActivity.this,
                                        t.getMessage(), Toast.LENGTH_SHORT).show();
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
