package com.example.myapplication.Room;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DeviceActivity.AddModule3;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.UserAndModule;
import com.example.myapplication.R;
import com.example.myapplication.UserPassword.LoginActivity;
import com.example.myapplication.UserPassword.UserActivity;
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

public class BedRoom extends AppCompatActivity {
    protected static final int RESULT_SPEECH = 1;
    private ImageButton btnSpeak;
    private TextView tvText,tvUser,tvLogout,tvAddDevice;
    private TextToSpeech mTTS;
    private List<UserAndModule> userAndDeviceList;
    private Button btnOnLight,btnOffLight,btnOnTV,btnOffTV,btnOnFan,btnOffFan,btnBack;
    private ImageView imgLight,imgFan,imgTV;
    public static final String Email="Email";
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_room);

        Intent intent = getIntent();
        String email = intent.getStringExtra(MainActivity.Email);
        innit();
        check();
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent User_Activity = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(User_Activity);
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent signInActivity = new Intent(getApplicationContext(), LoginActivity.class);
                signInActivity.setFlags(signInActivity.FLAG_ACTIVITY_NEW_TASK | signInActivity.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signInActivity);
                finish();
            }
        });
        tvAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddDeviceActivity = new Intent(getApplicationContext(), AddModule3.class);
                AddDeviceActivity.putExtra(Email, email);
                startActivity(AddDeviceActivity);
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    tvText.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "successfully", Toast.LENGTH_LONG).show();
                Intent Main_Activity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Main_Activity);
            }
        });

    }


    private void innit(){
        tvText = findViewById(R.id.tvText3);
        btnSpeak = findViewById(R.id.btnSpeak3);
        tvUser = findViewById(R.id.TextViewUser3);
        tvLogout= findViewById(R.id.TextViewLogout3);
        tvAddDevice=findViewById(R.id.TextViewdevice3);
        btnBack=findViewById(R.id.btnBack2);
        btnOnLight= findViewById(R.id.btnOnLight2);
        btnOffLight= findViewById(R.id.btnOffLight2);
        btnOnTV=findViewById(R.id.btnOnTV2);
        btnOffTV= findViewById(R.id.btnOffTV2);
        btnOffFan= findViewById(R.id.btnOffFan2);
        btnOnFan=findViewById(R.id.btnOnFan2);
        imgLight=findViewById(R.id.imageViewLight2);
        imgFan=findViewById(R.id.imageViewFan2);
        imgTV=findViewById(R.id.imageViewTV2);
        userAndDeviceList = new ArrayList<>();
    }
    private void check2(String resText){
        Intent intent = getIntent();
        String email = intent.getStringExtra(MainActivity.Email);
        String[] splits = email.split("@gmail.com");
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : splits)
            stringBuilder.append(item);
        String build = stringBuilder.toString();
        Query query = FirebaseDatabase.getInstance().getReference("User").child(build)
                .orderByChild("userName").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "oke", Toast.LENGTH_LONG).show();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        UserAndModule userControlRelay = data.getValue(UserAndModule.class);
                        userAndDeviceList.add(userControlRelay);
                    }
                    for (UserAndModule uAD : userAndDeviceList) {
                        String module3 = uAD.getModule3();
                        DatabaseReference data_user = FirebaseDatabase.getInstance().getReference("module/module2").child(module3);
                        if(resText.equals("OK, turn on the light!")) {
                            data_user.child("Light").setValue(1);
                            imgLight.setBackgroundColor(Color.YELLOW);
                        }
                        else if(resText.equals("OK, turn off the light!")) {
                            data_user.child("Light").setValue(0);
                            imgLight.setBackgroundColor(Color.WHITE);
                        }
                        else if(resText.equals("OK, turn on the television!")) {
                            data_user.child("TV").setValue(1);
                            imgTV.setBackgroundColor(Color.YELLOW);
                        }
                        else if(resText.equals("OK, turn off the television!")) {
                            data_user.child("TV").setValue(0);
                            imgTV.setBackgroundColor(Color.WHITE);
                        }
                        else if(resText.equals("OK, turn on the fan!")) {
                            data_user.child("Fan").setValue(1);
                            imgFan.setBackgroundColor(Color.YELLOW);
                        }
                        else if(resText.equals("OK, turn off the fan!")) {
                            data_user.child("Fan").setValue(0);
                            imgFan.setBackgroundColor(Color.WHITE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void check(){
        Intent intent = getIntent();
        String email = intent.getStringExtra(MainActivity.Email);
        String[] splits = email.split("@gmail.com");
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : splits)
            stringBuilder.append(item);
        String build = stringBuilder.toString();
        Query query = FirebaseDatabase.getInstance().getReference("User").child(build)
                .orderByChild("userName").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "oke", Toast.LENGTH_LONG).show();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        UserAndModule userControlRelay = data.getValue(UserAndModule.class);
                        userAndDeviceList.add(userControlRelay);
                    }
                    for (UserAndModule uAD : userAndDeviceList) {
                        String module3 = uAD.getModule3();
                        DatabaseReference data_user = FirebaseDatabase.getInstance().getReference("module/module2").child(module3);
                        btnOnLight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data_user.child("Light").setValue(1);
                                imgLight.setBackgroundColor(Color.YELLOW);
                            }
                        });
                        btnOffLight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data_user.child("Light").setValue(0);
                                imgLight.setBackgroundColor(Color.WHITE);
                            }
                        });
                        btnOnTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data_user.child("TV").setValue(1);
                                imgTV.setBackgroundColor(Color.YELLOW);
                            }
                        });
                        btnOffTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data_user.child("TV").setValue(0);
                                imgTV.setBackgroundColor(Color.WHITE);
                            }
                        });
                        btnOnFan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data_user.child("Fan").setValue(1);
                                imgFan.setBackgroundColor(Color.YELLOW);
                            }
                        });
                        btnOffFan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data_user.child("Fan").setValue(0);
                                imgFan.setBackgroundColor(Color.WHITE);
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "click to add module", Toast.LENGTH_LONG).show();
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
                    if (text.contains("turn on the light")||text.contains("turn on light")) {

                        String resText = "OK, turn on the light!";
                        check2(resText);
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
                    if (text.contains("turn off the light")||text.contains("turn off light")) {

                        String resText = "OK, turn off the light!";
                        check2(resText);
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
                    if (text.contains("turn on the TV")||text.contains("turn on TV")) {

                        String resText = "OK, turn on the television!";
                        check2(resText);
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
                    if (text.contains("turn off the TV")||text.contains("turn off TV")) {

                        String resText = "OK, turn off the television!";
                        check2(resText);
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
                    if (text.contains("turn on the fan")||text.contains("turn on fan")) {

                        String resText = "OK, turn on the fan!";
                        check2(resText);
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
                    if (text.contains("turn off the fan")||text.contains("turn off fan")) {

                        String resText = "OK, turn off the fan!";
                        check2(resText);
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
