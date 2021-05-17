package com.example.myapplication.UserPassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnLogin, btnLoginSignUp;
    private EditText edtLoginUser, edtLoginPassword;
    private TextView ForgotPass, ChangePass;
    public static final String Email = "Email";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        innit();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLogin();
            }
        });

        btnLoginSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpActivity = new Intent(LoginActivity.this, LogupActivity.class);
                startActivity(signUpActivity);
            }
        });
        ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "successfully", Toast.LENGTH_LONG).show();
                Intent Login_ResetPassword_Activity = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(Login_ResetPassword_Activity);
            }
        });
        ChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "successfully", Toast.LENGTH_LONG).show();
                Intent Login_ChangePassword_Activity = new Intent(LoginActivity.this, ChangePassword.class);
                startActivity(Login_ChangePassword_Activity);
            }
        });

    }
    private void CheckLogin() {
        Intent intent = getIntent();
        String userId = intent.getStringExtra(LogupActivity.UserId);
        String email = edtLoginUser.getText().toString().trim();
        String password = edtLoginPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
            Toast.makeText(LoginActivity.this,
                    "user name or password empty", Toast.LENGTH_LONG).show();
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "You have sigined successfully", Toast.LENGTH_LONG).show();
                                Intent Main_Activity = new Intent(LoginActivity.this, MainActivity.class);
                                Main_Activity.putExtra(Email,email);
                                startActivity(Main_Activity);
                            } else {
                                Toast.makeText(LoginActivity.this, "The account or password not true", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void innit() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLoginSignUp = (Button) findViewById(R.id.btnLoginSignUp);
        edtLoginUser = (EditText) findViewById(R.id.edtLoginUser);
        edtLoginPassword = (EditText) findViewById(R.id.edtLoginPassword);
        ForgotPass = (TextView) findViewById(R.id.ForgotPassword);
        ChangePass = (TextView) findViewById(R.id.ChangePassword);
    }
}