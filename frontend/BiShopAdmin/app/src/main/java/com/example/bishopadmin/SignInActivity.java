package com.example.bishopadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private Button btnSignIn;
    private EditText edtUserName, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn = (Button) findViewById(R.id.btn_signin_login);
        edtUserName = (EditText) findViewById(R.id.txt_signin_username);
        edtPassword = (EditText) findViewById(R.id.txt_signin_password);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = ApiUtils.getApiService();
                apiService.LogIn(edtUserName.getText().toString(), edtPassword.getText().toString())
                        .enqueue(new Callback<LogInResponse>() {
                            @Override
                            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                                if (response.body().getError() == null) {
                                    Toast.makeText(SignInActivity.this, "successful", Toast.LENGTH_SHORT).show();
                                    Common.AccessToken = response.body().getToken();
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(SignInActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<LogInResponse> call, Throwable t) {
                                Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
