package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button btnSignUp, btnSingIn,  btnResetPW;
    EditText edtEmail, edtPassword;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        AnhXa();
//        FirebaseAuth.getInstance().signOut();
        currentUser = mAuth.getCurrentUser();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (email.matches("")||password.matches("")){
                    Toast.makeText(LoginActivity.this, "Thông tin đăng nhập không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else {
                    DangNhap();
                }
            }
        });

        btnResetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void DangNhap() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser users) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.child(users.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.roles.equals("admin")){
                    Intent intent = new Intent (getApplication(),MainActivity.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }else {
                    Intent intent = new Intent (getApplication(), DashboardActivity.class);
                    intent.putExtra("uid",users.getUid());
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
//            currentUser.reload();
        }
    }

    private void AnhXa() {
        btnSignUp = (Button) findViewById(R.id.btnChoseSingUp);
        btnSingIn = (Button) findViewById(R.id.btnSignIn);
        edtEmail = (EditText) findViewById(R.id.edtSingInEmail);
        edtPassword = (EditText) findViewById(R.id.edtSingInPassword);
        mAuth = FirebaseAuth.getInstance();
        btnResetPW = (Button) findViewById(R.id.btnLossPassword);
        mData = FirebaseDatabase.getInstance().getReference("Users");
    }
}