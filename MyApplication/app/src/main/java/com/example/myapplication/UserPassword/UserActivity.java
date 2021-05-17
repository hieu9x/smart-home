package com.example.myapplication.UserPassword;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity{

    private TextView tvchangepass,tvforgotpass;
    private Button btnLogout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tvchangepass = findViewById(R.id.ChangePassword2);
        tvforgotpass= findViewById(R.id.ForgotPassword2);
        btnLogout = findViewById(R.id.btnLogout);

        tvchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Login_ChangePassword_Activity = new Intent(UserActivity.this, ChangePassword.class);
                startActivity(Login_ChangePassword_Activity);
            }
        });
        tvforgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Login_ResetPassword_Activity = new Intent(UserActivity.this, ResetPasswordActivity.class);
                startActivity(Login_ResetPassword_Activity);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                onDestroy();
                Intent signInActivity = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(signInActivity);
            }
        });
    }
}
