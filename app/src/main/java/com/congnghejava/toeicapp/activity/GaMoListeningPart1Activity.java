package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.handle.HandleListeningActivity;
import com.congnghejava.toeicapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class GaMoListeningPart1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ga_mo_listening_part1);
        getSupportActionBar().hide();

        Button btnBack = (Button) findViewById(R.id.btngamop1backtolc);
        Button btnBai1 = (Button) findViewById(R.id.btnlisgamop1b1);
        Button btnBai2= (Button) findViewById(R.id.btnlisgamop1b2);
        Button btnBai3 = (Button) findViewById(R.id.btnlisgamop1b3);
        Button btnBai4 = (Button) findViewById(R.id.btnlisgamop1b4);
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GaMoListeningPart1Activity.this,GaMoListeningActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnBai1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (GaMoListeningPart1Activity.this, HandleListeningActivity.class);
                intent.putExtra("level", "1");
                intent.putExtra("part","1");
                intent.putExtra("test","1");
                startActivity(intent);
            }
        });

        btnBai2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (GaMoListeningPart1Activity.this, HandleListeningActivity.class);
                intent.putExtra("level", "1");
                intent.putExtra("part","1");
                intent.putExtra("test","2");
                startActivity(intent);
            }
        });

        btnBai3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (GaMoListeningPart1Activity.this, HandleListeningActivity.class);
                intent.putExtra("level", "1");
                intent.putExtra("part","1");
                intent.putExtra("test","3");
                startActivity(intent);
            }
        });

        btnBai4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (GaMoListeningPart1Activity.this, HandleListeningActivity.class);
                intent.putExtra("level", "1");
                intent.putExtra("part","1");
                intent.putExtra("test","4");
                startActivity(intent);
            }
        });
    }
}