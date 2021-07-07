package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText edtUserName, edtEmail, edtPassword;
    CheckBox checkBoxAccess;
    Button btnSignUp, btnBack;
    String sEmail, sPassword, sUsername;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        AnhXa();
        btnSignUp.setVisibility(View.GONE);
        checkBoxAccess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    btnSignUp.setVisibility(View.VISIBLE);
                } else {
                    btnSignUp.setVisibility(View.GONE);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEmail = edtEmail.getText().toString();
                sPassword = edtPassword.getText().toString();
                sUsername = edtUserName.getText().toString();
                if (isEmptyText(sUsername, sEmail,sPassword)==false){
                    DangKy();
                }   else {
                    Toast.makeText(SignUpActivity.this, "Vui lòng không bỏ trống thông tin",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void DangKy() {
        sEmail = edtEmail.getText().toString();
        sPassword = edtPassword.getText().toString();
        String photoUrl = "https://firebasestorage.googleapis.com/v0/b/toeicapp-ee2c6.appspot.com/o/UserAvatar%2Favatar-default.jpg?alt=media&token=1711b89c-de1b-4c07-bafd-ae8897be155d";
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            updateUserProfile(fUser,edtUserName.getText().toString()); // update user profile on firebaseAuth
                            User user = new User(mAuth.getCurrentUser().getUid(),edtUserName.getText().toString(),fUser.getEmail(),photoUrl,"0","0","user");
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            ref.child(fUser.getUid().toString()).setValue(user); // update user profile on realtime database
                            updateUI();
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            if (edtPassword.length()< 6)
                                Toast.makeText(SignUpActivity.this, "Lỗi! Password chưa đủ 6 ký tự",
                                        Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SignUpActivity.this, "Lỗi! Mail sai định dạng",
                                        Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI() {
        mAuth.signOut();
        Intent intent = new Intent(SignUpActivity.this,SignUpSuccessActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUserProfile(FirebaseUser fUser, String toString) {
        String photoUrl = "https://firebasestorage.googleapis.com/v0/b/toeicapp-ee2c6.appspot.com/o/UserAvatar%2Favatar-default.jpg?alt=media&token=1711b89c-de1b-4c07-bafd-ae8897be155d";
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(sUsername)
                .setPhotoUri(Uri.parse(photoUrl))
                .build();

        fUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
    }

    private boolean isEmptyText(String sUsername, String sEmail, String sPassword) {
        if (sUsername.matches("")||sEmail.matches("")||sPassword.matches(""))
            return true;
        return false;
    }

    private void AnhXa() {
        edtUserName = findViewById(R.id.edtSignUpUserName);
        edtEmail = findViewById(R.id.edtSingUpEmail);
        edtPassword = findViewById(R.id.edtSingUpPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnBack = findViewById(R.id.btnbacksignuptologin);
        checkBoxAccess =findViewById(R.id.checkboxAccess);
        mAuth = FirebaseAuth.getInstance();
    }
}