package com.example.myapplication.UserPassword;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private EditText edtNewPassword, edtNewConfirmPassword,edtEmail,edtPassword;
    private Button btnUpdatePassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        innit();
        mAuth = FirebaseAuth.getInstance();

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePass();
            }
        });

    };
    private void ChangePass(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String NewPassword = edtNewPassword.getText().toString().trim();
        String NewConfirmPassword = edtNewConfirmPassword.getText().toString().trim();
        String Email= edtEmail.getText().toString().trim();
        String Password= edtPassword.getText().toString().trim();

        if ( TextUtils.isEmpty(NewPassword)
                || TextUtils.isEmpty(NewConfirmPassword))
            Toast.makeText(ChangePassword.this,
                    "user name or password or confirm password empty", Toast.LENGTH_LONG).show();
        else if (!NewPassword.equals(NewConfirmPassword))
            Toast.makeText(ChangePassword.this, "password is not equals", Toast.LENGTH_LONG).show();
        else  {
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.updatePassword(NewPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangePassword.this, "Change Password successfully", Toast.LENGTH_LONG).show();
                                                    Intent signInActivity = new Intent(ChangePassword.this, MainActivity.class);
                                                    startActivity(signInActivity);
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(ChangePassword.this, "Email or password not true", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void innit(){
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtNewConfirmPassword = (EditText)findViewById(R.id.edtNewConfirmPassword);
        btnUpdatePassword = (Button)findViewById(R.id.btnUpdatePassword);
        edtEmail=(EditText)findViewById(R.id.edtEmailtoChange);
        edtPassword=(EditText)findViewById(R.id.edtOldPassword);
    }
}
