package com.example.myapplication.UserPassword;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.UserAndModule;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnLogup;
    private EditText  edtEmail, edtPassword, edtConfirmPassword;
    public static final String UserId = "UserId";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);

        mAuth = FirebaseAuth.getInstance();
        innit();
        btnLogup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                register();
            }
        });

    }
    private void register(){
        String email    = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword))
            Toast.makeText(LogupActivity.this,
                    "user name or password or confirm password empty", Toast.LENGTH_LONG).show();
        else if (!password.equals(confirmPassword))
            Toast.makeText(LogupActivity.this, "password is not equals", Toast.LENGTH_LONG).show();
        else if (password.equals(confirmPassword)){
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() ) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                String[] splits = email.split("@gmail.com");
                                StringBuilder stringBuilder = new StringBuilder();
                                for (String item: splits)
                                    stringBuilder.append(item);
                                String build = stringBuilder.toString();
                                Toast.makeText(LogupActivity.this, "You have successfully registered", Toast.LENGTH_LONG).show();
                                Intent signInActivity = new Intent(LogupActivity.this, LoginActivity.class);
                                DatabaseReference userAndDevice = FirebaseDatabase.getInstance().getReference("User").child(build);
                                String userId = userAndDevice.push().getKey();
                                UserAndModule userDevice = new UserAndModule(userId,email,"0","0","0","0");
                                userAndDevice.child(userId).setValue(userDevice);
                                DatabaseReference temH = FirebaseDatabase.getInstance().getReference("module").child("module1");
                                temH.child("0").child("humidity").setValue(0);
                                temH.child("0").child("temperature").setValue(0);
                                startActivity(signInActivity);
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LogupActivity.this, "Password is too short or account has been created", Toast.LENGTH_LONG).show();
                            }

                    }
                });
        }
    }
    private void innit() {
        btnLogup            = (Button)    findViewById(R.id.btnLogup);
        edtEmail            = (EditText)  findViewById(R.id.edtEmail);
        edtPassword         = (EditText)  findViewById(R.id.edtPassword);
        edtConfirmPassword  = (EditText)  findViewById(R.id.edtConfirmPassword);
    }
}


