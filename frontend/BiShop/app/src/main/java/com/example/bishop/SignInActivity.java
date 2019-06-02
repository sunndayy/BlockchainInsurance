package com.example.bishop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private Button btnSignIn;
    private EditText edtUserName, edtPassword;
    private TextView txtvSignUp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn = (Button) findViewById(R.id.btn_signin_login);
        edtUserName = (EditText) findViewById(R.id.txt_signin_username);
        edtPassword = (EditText) findViewById(R.id.txt_signin_password);
        txtvSignUp = (TextView) findViewById(R.id.txt_signin_signup);
        progressBar = (ProgressBar) findViewById(R.id.progress_signin);

        progressBar.setVisibility(View.GONE);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtUserName.getText().toString().equals("")
                        || edtPassword.getText().toString().equals("")) {
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập đủ thông tin",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);

                    final ApiService apiService = ApiUtils.getApiService();
                    apiService.SignIn(edtUserName.getText().toString(), edtPassword.getText().toString())
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, final Response<User> response) {
                                    if (response.body().getError() == null) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        apiService.GetUserInfo(response.body().getToken()).enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call1, Response<User> response1) {
                                                Common.user = response1.body();
                                                Common.user.setToken(response.body().getToken());
                                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<User> call1, Throwable t1) {
                                                Toast.makeText(SignInActivity.this, t1.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignInActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        txtvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });

    }
}
