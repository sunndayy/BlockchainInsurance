package com.example.garaadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrderActivity extends AppCompatActivity {

    private TextView tvName, tvBirthday, tvId, tvSex, tvAddress, tvPhone, tvEmail;
    private Button btnAddForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvName = (TextView) findViewById(R.id.tv_add_name);
        tvId = (TextView) findViewById(R.id.tv_add_id);
        tvAddress = (TextView) findViewById(R.id.tv_add_address);
        tvPhone = (TextView) findViewById(R.id.tv_add_phone);
        tvEmail = (TextView) findViewById(R.id.tv_add_email);

        btnAddForm = (Button) findViewById(R.id.btn_add_form);

        btnAddForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = ApiUtils.getApiService();

                Item item = new Item();
                item.getUser().setName(tvName.getText().toString());
                item.getUser().getBirthday().setDay(1);
                item.getUser().getBirthday().setMonth(1);
                item.getUser().getBirthday().setYear(200);
                item.getUser().setSex(true);
                item.getUser().setIdentityCard(tvId.getText().toString());
                item.getUser().setAddress(tvAddress.getText().toString());
                item.getUser().setPhoneNumber(tvPhone.getText().toString());
                item.getUser().setEmail(tvEmail.getText().toString());

                apiService.CreateOrder(Common.AccessToken, item)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(AddOrderActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(AddOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
