package com.example.bishop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    private Button btnSignIn;
    private EditText edtUserName, edtPassword;
    private TextView txtvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn = (Button) findViewById(R.id.btn_signin_login);
        edtUserName = (EditText) findViewById(R.id.txt_signin_username);
        edtPassword = (EditText) findViewById(R.id.txt_signin_password);
        txtvSignUp = (TextView) findViewById(R.id.txt_signin_signup);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                finish();
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
