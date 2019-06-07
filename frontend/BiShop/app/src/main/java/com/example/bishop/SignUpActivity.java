package com.example.bishop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private Button btnSignUp;
    private EditText edtUserName, edtName, edtIdNumber, edtAddress,
            edtEmail, edtPhone, edtPassword, edtRepassword;

    private TextView tvNgaySinh;
    private ImageView btnChooseCalendar;

    private TextView txtvLogin;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Birthday birthDay;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = (Button) findViewById(R.id.btn_signup_signup);
        txtvLogin = (TextView) findViewById(R.id.txt_signup_login);
        edtUserName = (EditText) findViewById(R.id.txt_signup_username);
        edtName = (EditText) findViewById(R.id.txt_signup_name);
        tvNgaySinh = (TextView) findViewById(R.id.tv_signup_ngaysinh);
        edtIdNumber = (EditText) findViewById(R.id.txt_signup_id);
        edtAddress = (EditText) findViewById(R.id.txt_signup_address);
        edtEmail = (EditText) findViewById(R.id.txt_signup_email);
        edtPhone = (EditText) findViewById(R.id.txt_signup_phonenumber);
        edtPassword = (EditText) findViewById(R.id.txt_signup_password);
        edtRepassword = (EditText) findViewById(R.id.txt_signup_repassword);
        progressBar = (ProgressBar) findViewById(R.id.progress_signup);

        progressBar.setVisibility(View.GONE);

        birthDay = new Birthday(1, 1, 1970);

        btnChooseCalendar = (ImageView) findViewById(R.id.btn_signup_choose_calendar);

        btnChooseCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = null;
                int year = 1, month = 1, day = 1970;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUpActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                tvNgaySinh.setText(date);

                birthDay = new Birthday(dayOfMonth, month, year);
            }
        };


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtUserName.getText().toString().equals("") ||
                        edtName.getText().toString().equals("") ||
                        edtIdNumber.getText().toString().equals("") ||
                        edtAddress.getText().toString().equals("") ||
                        edtEmail.getText().toString().equals("") ||
                        edtPhone.getText().toString().equals("") ||
                        edtPassword.getText().toString().equals("") ||
                        edtRepassword.getText().toString().equals("") ||
                        edtName.getText().toString().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ thông tin",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (!edtPassword.getText().toString().equals(edtRepassword.getText().toString())) {
                        Toast.makeText(SignUpActivity.this, "Mật khẩu nhập lại không khớp",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);

                        User user = new User(
                                edtUserName.getText().toString(),
                                edtPassword.getText().toString(),
                                edtIdNumber.getText().toString(),
                                edtName.getText().toString(),
                                birthDay,
                                edtAddress.getText().toString(),
                                edtPhone.getText().toString(),
                                edtEmail.getText().toString(),
                                "",
                                ""
                        );


                        ApiService apiService = ApiUtils.getApiService();

                        apiService.SignUp(user).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.body().getError() == null) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                    finish();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignUpActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        txtvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
}
