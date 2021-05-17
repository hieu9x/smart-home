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

import com.example.myapplication.DeviceActivity.AddModule4;
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

public class Kitchen extends AppCompatActivity {
    protected static final int RESULT_SPEECH = 1;
    private ImageButton btnSpeak;
    private TextView tvText,tvUser,tvLogout,tvAddDevice;
    private TextToSpeech mTTS;
    private List<UserAndModule> userAndDeviceList;
    private Button btnOnLight,btnOffLight,btnOnStove,btnOffStove,btnOnFan,btnOffFan,btnBack;
    private ImageView imgLight,imgFan,imgStove;
    public static final String Email="Email";
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

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
                Intent AddDeviceActivity = new Intent(getApplicationContext(), AddModule4.class);
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
                Intent Main_Activity = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(), "successfully", Toast.LENGTH_LONG).show();
                startActivity(Main_Activity);
            }
        });

    }


    private void innit(){
        tvText = findViewById(R.id.tvText4);
        btnSpeak = findViewById(R.id.btnSpeak4);
        tvUser = findViewById(R.id.TextViewUser4);
        tvLogout= findViewById(R.id.TextViewLogout4);
        tvAddDevice=findViewById(R.id.TextViewdevice4);
        btnBack=findViewById(R.id.btnBack3);
        btnOnLight= findViewById(R.id.btnOnLight3);
        btnOffLight= findViewById(R.id.btnOffLight3);
        btnOnStove=findViewById(R.id.btnOnStove);
        btnOffStove= findViewById(R.id.btnOffStove);
        btnOffFan= findViewById(R.id.btnOffFan3);
        btnOnFan=findViewById(R.id.btnOnFan3);
        imgLight=findViewById(R.id.imageViewLight3);
        imgFan=findViewById(R.id.imageViewFan3);
        imgStove=findViewById(R.id.imageViewStove);
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
                        String module4 = uAD.getModule4();
                        DatabaseReference data_user = FirebaseDatabase.getInstance().getReference("module/module2").child(module4);
                        if(resText.equals("OK, turn on the light!")) {
                            data_user.child("Light").setValue(1);
                            imgLight.setBackgroundColor(Color.YELLOW);
                        }
                        else if(resText.equals("OK, turn off the light!")) {
                            data_user.child("Light").setValue(0);
                            imgLight.setBackgroundColor(Color.WHITE);
                        }
                        else if(resText.equals("OK, turn on the stove!")) {
                            data_user.child("TV").setValue(1);
                            imgStove.setBackgroundColor(Color.YELLOW);
                        }
                        else if(resText.equals("OK, turn off the stove!")) {
                            data_user.child("TV").setValue(0);
                            imgStove.setBackgroundColor(Color.WHITE);
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
                        String module4 = uAD.getModule4();
                        DatabaseReference data_user = FirebaseDatabase.getInstance().getReference("module/module2").child(module4);
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
                        btnOnStove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data_user.child("Stove").setValue(1);
                                imgStove.setBackgroundColor(Color.YELLOW);
                            }
                        });
                        btnOffStove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data_user.child("Stove").setValue(0);
                                imgStove.setBackgroundColor(Color.WHITE);
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
                    if (text.contains("turn on the stove")||text.contains("turn on stove")) {

                        String resText = "OK, turn on the stove!";
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
                    if (text.contains("turn off the stove")||text.contains("turn off stove")) {

                        String resText = "OK, turn off the stove!";
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
