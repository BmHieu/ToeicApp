package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.congnghejava.toeicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    Button btnBack, btnSendLink;
    EditText edtEmailReset;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnbackresetpasswordtologin);
        btnSendLink = findViewById(R.id.btnResetPassword);
        edtEmailReset = findViewById(R.id.edtEmailResetPassWord);
        mAuth = FirebaseAuth.getInstance();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmailReset.getText().toString().matches("")){
                    Toast.makeText(ResetPasswordActivity.this,"Vui lòng nhập email vào", Toast.LENGTH_SHORT).show();
                } else{
                    ResetPassword();
                    Intent intent = new Intent (ResetPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void ResetPassword(){
        String emailReset = edtEmailReset.getText().toString();
        mAuth.sendPasswordResetEmail(emailReset).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this,"Mail reset password đã được gửi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}