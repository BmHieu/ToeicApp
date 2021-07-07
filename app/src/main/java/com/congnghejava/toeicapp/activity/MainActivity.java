package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.handle.CustomAdminAdapter;
import com.congnghejava.toeicapp.handle.CustomPracticeAdapter;
import com.congnghejava.toeicapp.model.User;
import com.congnghejava.toeicapp.model.Vocabulary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity{

    ExpandableListView expandableListView;
    CustomAdminAdapter customAdminAdapter;
    List<User> listUserHeader;
    HashMap<User, User> listUserDataChild;
    DatabaseReference mData;
    Button btnLognout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        listUserHeader = new ArrayList<User>();
        listUserDataChild = new HashMap<User, User>();
        btnLognout = findViewById(R.id.adminlogout);
        mData = FirebaseDatabase.getInstance().getReference("Users");
        expandableListView = findViewById(R.id.expandableAdmin);

        SetStandardGroups();
        customAdminAdapter = new CustomAdminAdapter(MainActivity.this, listUserHeader, listUserDataChild);
        expandableListView.setAdapter(customAdminAdapter);

        btnLognout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void SetStandardGroups() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 0;
                listUserHeader.clear();
                listUserDataChild.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    User User = ds.getValue(User.class);
                    if (!User.roles.equals("admin")){
                        listUserHeader.add(User);
                        listUserDataChild.put(listUserHeader.get(counter), User);
                        counter++;
                    }
                }
                customAdminAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }


}