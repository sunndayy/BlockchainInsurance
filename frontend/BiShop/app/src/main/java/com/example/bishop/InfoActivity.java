package com.example.bishop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView tvUserName, tvName, tvId, tvBd, tvPhone, tvEmail, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvUserName = (TextView) findViewById(R.id.tv_info_username);
        tvName = (TextView) findViewById(R.id.tv_info_name);
        tvId = (TextView) findViewById(R.id.tv_info_id);
        tvBd = (TextView) findViewById(R.id.tv_info_birthday);
        tvPhone = (TextView) findViewById(R.id.tv_info_phone);
        tvEmail = (TextView) findViewById(R.id.tv_info_email);
        tvAddress = (TextView) findViewById(R.id.tv_info_address);

        if (Common.user != null) {
            tvUserName.setText(Common.user.getUsername());
            tvName.setText(Common.user.getName());
            tvId.setText(Common.user.getIdentityCard());
            tvBd.setText("" + Common.user.getBirthday().getDay() + "/"
                    + Common.user.getBirthday().getMonth() + "/"
                    + Common.user.getBirthday().getYear());
            tvPhone.setText(Common.user.getPhoneNumber());
            tvEmail.setText(Common.user.getEmail());
            tvAddress.setText(Common.user.getAddress());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
