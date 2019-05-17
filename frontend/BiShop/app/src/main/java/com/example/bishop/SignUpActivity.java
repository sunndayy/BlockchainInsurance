package com.example.bishop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private Button btnSignUp;
    private EditText edtUserName, edtName, edtDob, edtIdNumber, edtAddress,
            edtEmail, edtPhone, edtPassword, edtRepassword;
    private TextView txtvLogin;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private BirthDay birthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = (Button) findViewById(R.id.btn_signup_signup);
        txtvLogin = (TextView) findViewById(R.id.txt_signup_login);
        edtUserName = (EditText) findViewById(R.id.txt_signup_username);
        edtName = (EditText) findViewById(R.id.txt_signup_name);
        edtDob = (EditText) findViewById(R.id.txt_signup_birthday);
        edtIdNumber = (EditText) findViewById(R.id.txt_signup_id);
        edtAddress = (EditText) findViewById(R.id.txt_signup_address);
        edtEmail = (EditText) findViewById(R.id.txt_signup_email);
        edtPhone = (EditText) findViewById(R.id.txt_signup_phonenumber);
        edtPassword = (EditText) findViewById(R.id.txt_signup_password);
        edtRepassword = (EditText) findViewById(R.id.txt_signup_repassword);


        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = null;
                int year = 1, month = 1,day = 1970;
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
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                edtDob.setText(date);

                birthDay = new BirthDay(dayOfMonth, month + 1, year);
            }
        };



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                finish();

                ApiService apiService = ApiUtils.getApiService();
                apiService.SignUp(
                        edtUserName.getText().toString(),
                        edtPassword.getText().toString(),
                        edtIdNumber.getText().toString(),
                        birthDay,
                        edtAddress.getText().toString(),
                        edtPhone.getText().toString(),
                        edtEmail.getText().toString()
                ).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body().getError() == null) {
                            Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
