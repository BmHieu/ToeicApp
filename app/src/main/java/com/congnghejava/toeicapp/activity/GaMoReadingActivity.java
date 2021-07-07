    package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

    public class GaMoReadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ga_mo_reading);
        getSupportActionBar().hide();
        Button btnBackHome = (Button) findViewById(R.id.btnbackreatohome);
        Button btnReaP5 = (Button) findViewById(R.id.btnreadgamo5);
        Button btnReaP6= (Button) findViewById(R.id.btnreadgamop6);
        ImageView imageUserAvatar = findViewById(R.id.avt);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference mUser = FirebaseDatabase.getInstance().getReference("Users");

        mUser.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.avtlink).into(imageUserAvatar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GaMoReadingActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });

        btnReaP5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GaMoReadingActivity.this, GaMoReadingPart5Activity.class);
                startActivity(intent);
            }
        });
        btnReaP6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GaMoReadingActivity.this, GaMoReadingPart6Activity.class);
                startActivity(intent);
            }
        });
    }
}