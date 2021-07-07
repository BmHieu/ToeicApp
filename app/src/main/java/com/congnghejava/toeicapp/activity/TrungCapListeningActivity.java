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

public class TrungCapListeningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trung_cap_listening);
        getSupportActionBar().hide();

        Button btnBackHome = (Button) findViewById(R.id.btnbacklistohome);
        Button btnLisP1 = (Button) findViewById(R.id.btnlistrungcapp1);
        Button btnLisP2= (Button) findViewById(R.id.btnlistrungcapp2);
        Button btnLisP3 = (Button) findViewById(R.id.btnlistrungcapp3);
        Button btnLisP4 = (Button) findViewById(R.id.btnlistrungcapp4);

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
                Intent intent = new Intent(TrungCapListeningActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });

        btnLisP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrungCapListeningActivity.this, TrungCapListeningPart1Activity.class);
                startActivity(intent);
            }
        });

        btnLisP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrungCapListeningActivity.this, TrungCapListeningPart2Activity.class);
                startActivity(intent);
            }
        });

        btnLisP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrungCapListeningActivity.this, TrungCapListeningPart3Activity.class);
                startActivity(intent);
            }
        });

        btnLisP4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrungCapListeningActivity.this, TrungCapListeningPart4Activity.class);
                startActivity(intent);
            }
        });
    }
}