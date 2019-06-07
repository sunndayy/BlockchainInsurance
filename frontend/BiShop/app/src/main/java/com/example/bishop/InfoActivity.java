package com.example.bishop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {

    private TextView tvUserName, tvName, tvId, tvBd, tvPhone, tvEmail, tvAddress;
    private CircleImageView avatar;
    private ImageView btnChangeAvatar;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        avatar = (CircleImageView) findViewById(R.id.img_info_avatar);

        btnChangeAvatar = (ImageView) findViewById(R.id.btn_info_change_avatar);

        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermistion();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE);
            }
        });

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

            Glide.with(this)
                    .load(ApiUtils.BASE_URL + "/user-avatar/" + Common.user.getUsername())
                    .into(avatar);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == InfoActivity.this.RESULT_OK) {
            //TODO: action
            Glide.with(InfoActivity.this).load(data.getData())
                    .into(avatar);
//            uploadFile = new File(Common.getPathFromUri(AddProductActivity.this, data.getData()));
        }
    }

    private void checkPermistion() {
        if (ContextCompat.checkSelfPermission(InfoActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(InfoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(InfoActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }
}
