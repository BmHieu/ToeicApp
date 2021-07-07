package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.congnghejava.toeicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edtConfirmPassword, edtNewPassword, edtOldPassword;
    Button btnSave, btnBack;
    FirebaseUser cUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().hide();

        edtConfirmPassword = findViewById(R.id.edtconfirmnewpassword);
        edtNewPassword = findViewById(R.id.edtnewpassword);
        edtOldPassword = findViewById(R.id.edtoldpassword);
        btnSave = findViewById(R.id.btnSaveNewPassword);
        btnBack = findViewById(R.id.btnbacktosettings);
        cUser = FirebaseAuth.getInstance().getCurrentUser();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirmPassword = edtConfirmPassword.getText().toString();
                String newPassword = edtNewPassword.getText().toString();
                String oldPassword = edtOldPassword.getText().toString();
                if (newPassword.equals(confirmPassword) && newPassword.length() >= 6){
                    AuthCredential credential = EmailAuthProvider.getCredential(cUser.getEmail(), oldPassword);
                    cUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                cUser.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplication(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                    FirebaseAuth.getInstance().signOut();
                                                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(getApplication(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (newPassword.length() < 6){
                    Toast.makeText(getApplication(), "Mật khẩu chưa đủ 6 ký tự", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "Mật khẩu xác thực không chính xác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}