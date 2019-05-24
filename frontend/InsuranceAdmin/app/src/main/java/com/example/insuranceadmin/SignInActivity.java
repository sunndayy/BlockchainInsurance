package com.example.insuranceadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private Button btnSignIn;
    private EditText edtUserName, edtPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn = (Button) findViewById(R.id.btn_signin_login);
        edtUserName = (EditText) findViewById(R.id.txt_signin_username);
        edtPassword = (EditText) findViewById(R.id.txt_signin_password);
        progressBar = (ProgressBar) findViewById(R.id.progress_signin);

        progressBar.setVisibility(View.GONE);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                if (edtUserName.getText().toString().equals("")
                        || edtPassword.getText().toString().equals("")) {
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập đủ thông tin",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ApiService apiService = ApiUtils.getApiService();
                    apiService.LogIn(edtUserName.getText().toString(), edtPassword.getText().toString())
                            .enqueue(new Callback<LogInResponse>() {
                                @Override
                                public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                                    progressBar.setVisibility(View.GONE);
                                    if (response.body().getError() == null) {
                                        Common.AccessToken = response.body().getToken();
                                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(SignInActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<LogInResponse> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
