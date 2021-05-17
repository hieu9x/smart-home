package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DeviceActivity.AddModule1;
import com.example.myapplication.Model.TempHum;
import com.example.myapplication.Model.UserAndModule;
import com.example.myapplication.Room.BedRoom;
import com.example.myapplication.Room.Kitchen;
import com.example.myapplication.Room.LivingRoom;
import com.example.myapplication.UserPassword.LoginActivity;
import com.example.myapplication.UserPassword.UserActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    protected static final int RESULT_SPEECH = 1;
    private ImageButton btnSpeak;
    private TextView tvText,tvUser,tvLogout,tvlivingroom,tvbedroom,tvkitchen;
    private TextToSpeech mTTS;
    private LineChart lineChart;
    private TextView textTemp, textHum,textAirQ, textAddDevice;
    private List<UserAndModule> userAndDeviceList;
    LineData lineData;
    LineDataSet tempSet, humSet;
    private float i = 0;
    public static final String Email="Email";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        check();
        innit();
        Intent intent = getIntent();
        String email = intent.getStringExtra(LoginActivity.Email);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent signInActivity = new Intent(MainActivity.this, LoginActivity.class);
                signInActivity.setFlags(signInActivity.FLAG_ACTIVITY_NEW_TASK|signInActivity.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signInActivity);
                finish();
            }
        });
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent User_Activity = new Intent(MainActivity.this, UserActivity.class);
                startActivity(User_Activity);
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try{
                    startActivityForResult(intent, RESULT_SPEECH);
                    tvText.setText("");
                }
                catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
        textAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddDeviceActivity = new Intent(MainActivity.this, AddModule1.class);
                AddDeviceActivity.putExtra(Email,email);
                startActivity(AddDeviceActivity);
            }
        });
        tvbedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BedRoom = new Intent(MainActivity.this, BedRoom.class);
                BedRoom.putExtra(Email,email);
                startActivity(BedRoom);
            }
        });
        tvkitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Kitchen = new Intent(MainActivity.this, Kitchen.class);
                Kitchen.putExtra(Email,email);
                startActivity(Kitchen);
            }
        });
        tvlivingroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LivingRoomActivity = new Intent(MainActivity.this, LivingRoom.class);
                LivingRoomActivity.putExtra(Email,email);
                startActivity(LivingRoomActivity);
            }
        });
    }
    private void innit(){
        tvText = findViewById(R.id.tvTest);
        btnSpeak = findViewById(R.id.btnSpeak);
        tvUser = findViewById(R.id.TextViewUser);
        tvLogout= findViewById(R.id.TextViewLogout);
        tvlivingroom= findViewById(R.id.TextViewLivingRoom);
        tvbedroom= findViewById(R.id.TextViewBedRoom);
        tvkitchen= findViewById(R.id.TextViewKitchen);
        textHum= findViewById(R.id.TextViewHumidity);
        textTemp= findViewById(R.id.TextViewTemperature);
        textAirQ= findViewById(R.id.TextViewAirQuality);
        lineChart = findViewById(R.id.lineChart);
        textAddDevice= findViewById(R.id.TextViewdevice);
        userAndDeviceList = new ArrayList<>();
    }
    private  void check() {
        Intent intent = getIntent();
        String email = intent.getStringExtra(LoginActivity.Email);
        String[] splits = email.split("@gmail.com");
        StringBuilder stringBuilder = new StringBuilder();
        for (String item: splits)
            stringBuilder.append(item);
        String build = stringBuilder.toString();
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
                        String module1 = uAD.getModule1();
                        DatabaseReference data_user = FirebaseDatabase.getInstance().getReference("module/module1").child(module1);
                        ArrayList<Entry> yTemp, yHum;
                        yTemp = new ArrayList<>();
                        yHum = new ArrayList<>();
                        data_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                TempHum tH = snapshot.getValue(TempHum.class);
                                Log.d("tag", tH.toString());
                                int temperature, humidity;
                                temperature = tH.getTemperature();
                                humidity = tH.getHumidity();
                                yHum.add(new Entry(i, humidity));
                                yTemp.add(new Entry(i, temperature));
                                i += 0.25;
                                textTemp.setText(String.valueOf(temperature));
                                textHum.setText(String.valueOf(humidity));
                                tempSet = new LineDataSet(yTemp, "Temperature");
                                tempSet.setColor(Color.RED);
                                tempSet.setLineWidth(2f);
                                tempSet.setDrawValues(false);
                                tempSet.setCircleColor(Color.BLACK);
                                humSet = new LineDataSet(yHum, "Humidity");
                                humSet.setColor(Color.BLUE);
                                humSet.setLineWidth(2f);
                                humSet.setCircleColor(Color.BLACK);
                                humSet.setDrawValues(false);
                                lineData = new LineData(tempSet, humSet);
                                lineChart.setData(lineData);
                                lineChart.setDrawGridBackground(false);
                                lineChart.invalidate();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "click to add device", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tvText.setText(text.get(0));
                    if (text.contains("open the kitchen")||text.contains("open kitchen")) {

                        String resText = "OK, open the kitchen!";
                        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status == TextToSpeech.SUCCESS) {
                                    int result = mTTS.setLanguage(Locale.ENGLISH);
                                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                                        Log.e("TTS", "Language not found");
                                    }
                                    mTTS.speak(resText, TextToSpeech.QUEUE_FLUSH, null);

                                } else {
                                    Log.e("TTS", "Initialization failed");
                                }

                            }

                        });

                    }
                    if (text.contains("open the living room")||text.contains("open living room")||text.contains("go to the living room")||text.contains("go to living room")) {

                        String resText = "OK, open the living room!";
                        Intent intent = getIntent();
                        String email = intent.getStringExtra(LoginActivity.Email);
                        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status == TextToSpeech.SUCCESS) {
                                    int result = mTTS.setLanguage(Locale.ENGLISH);
                                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                                        Log.e("TTS", "Language not found");
                                    }
                                    mTTS.speak(resText, TextToSpeech.QUEUE_FLUSH, null);
                                } else {
                                    Log.e("TTS", "Initialization failed");
                                }

                            }

                        });
                        Intent AddDeviceActivity = new Intent(MainActivity.this, LivingRoom.class);
                        AddDeviceActivity.putExtra(Email,email);
                        startActivity(AddDeviceActivity);
                    }if (text.contains("open the bedroom")||text.contains("open bedroom")) {

                        String resText = "OK, open the bedroom!";
                        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status == TextToSpeech.SUCCESS) {
                                    int result = mTTS.setLanguage(Locale.ENGLISH);
                                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                                        Log.e("TTS", "Language not found");
                                    }
                                    mTTS.speak(resText, TextToSpeech.QUEUE_FLUSH, null);
                                } else {
                                    Log.e("TTS", "Initialization failed");
                                }

                            }

                        });

                    }
                    break;
                }
        }
    }
}