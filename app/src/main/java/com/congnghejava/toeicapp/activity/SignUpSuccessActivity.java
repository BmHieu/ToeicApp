package com.congnghejava.toeicapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.congnghejava.toeicapp.R;

public class SignUpSuccessActivity extends AppCompatActivity {

    Button btnSignInNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_success);
        getSupportActionBar().hide();

        btnSignInNow = findViewById(R.id.btnSigninNow);

        btnSignInNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}