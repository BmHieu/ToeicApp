package com.congnghejava.toeicapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.congnghejava.toeicapp.R;

public class SettingsActivity extends AppCompatActivity {

    Button btnProfileSetting, btnChangePassword, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();


        btnProfileSetting = findViewById(R.id.btnprofilesettings);
        btnChangePassword = findViewById(R.id.btnchangepassword);
        btnBack = findViewById(R.id.btnbacktoprofile);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnProfileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}