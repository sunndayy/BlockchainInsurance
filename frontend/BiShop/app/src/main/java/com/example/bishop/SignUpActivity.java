package com.example.bishop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    private Button btnSignUp;
    private EditText edtUserName, edtName, edtIdNumber, edtAddress,
            edtEmail, edtPhone, edtPassword, edtRepassword;
    private TextView txtvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = (Button) findViewById(R.id.btn_signup_signup);
        txtvLogin = (TextView) findViewById(R.id.txt_signup_login);
        edtUserName = (EditText) findViewById(R.id.txt_signup_username);
        edtName = (EditText) findViewById(R.id.txt_signup_name);
        edtIdNumber = (EditText) findViewById(R.id.txt_signup_id);
        edtAddress = (EditText) findViewById(R.id.txt_signup_address);
        edtEmail = (EditText) findViewById(R.id.txt_signup_email);
        edtPhone = (EditText) findViewById(R.id.txt_signup_phonenumber);
        edtPassword = (EditText) findViewById(R.id.txt_signup_password);
        edtRepassword = (EditText) findViewById(R.id.txt_signup_repassword);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
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
