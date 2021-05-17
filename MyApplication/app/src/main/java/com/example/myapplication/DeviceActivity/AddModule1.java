package com.example.myapplication.DeviceActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.UserAndModule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;

public class AddModule1 extends AppCompatActivity {

    private EditText addMac;
    private Button addDevice;
    private List<UserAndModule> userAndDeviceList;
    public static final String Adr= "device1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module1);

        innit();
        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void innit() {
        addMac = findViewById(R.id.edtAddDevice);
        addDevice = findViewById(R.id.btnAddDevice);
        userAndDeviceList = new ArrayList<>();
    }
    private void check() {
        Intent intent = getIntent();
        String email = intent.getStringExtra(MainActivity.Email);
        String AddMac = addMac.getText().toString().trim();
        String[] splits = email.split("@gmail.com");
        StringBuilder stringBuilder = new StringBuilder();
        for (String item: splits)
            stringBuilder.append(item);
        String build = stringBuilder.toString();
        Query query = FirebaseDatabase.getInstance().getReference("module").child("macadr")
                .orderByChild("macadr").equalTo(AddMac);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                    Toast.makeText(getApplicationContext(), "device invalid", Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                    Query query = FirebaseDatabase.getInstance().getReference("User").child(build)
                            .orderByChild("userName").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userAndDeviceList.clear();
                            if(snapshot.exists()) {
                                Toast.makeText(getApplicationContext(), "oke", Toast.LENGTH_LONG).show();
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    UserAndModule userControlRelay = data.getValue(UserAndModule.class);
                                    userAndDeviceList.add(userControlRelay);
                                }
                                for (UserAndModule uAD : userAndDeviceList) {
                                    String userId = uAD.getuserId();
                                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("User").child(build);
                                    databaseReference.child(userId).child("module1").setValue(AddMac);
                                    Toast.makeText(getApplicationContext(), "successfully", Toast.LENGTH_LONG).show();
                                    Intent Main_Activity = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(Main_Activity);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}