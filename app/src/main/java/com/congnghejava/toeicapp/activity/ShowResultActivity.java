package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.model.Completed;
import com.congnghejava.toeicapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShowResultActivity extends AppCompatActivity {

    Button btnNextHome;
    TextView tvScore, mCountExam;
    DatabaseReference data;
    FirebaseAuth mAuth;
    DatabaseReference mRoot, mUser;
    FirebaseUser currentUser;
    int iScore;
    String countCorrectAns, testLenght, isCompleted, sPart, sTest, UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        getSupportActionBar().hide();

        tvScore = (TextView) findViewById(R.id.txtscore);
        mCountExam = (TextView) findViewById(R.id.txtcountcorrectanswer);

        Intent mIntent = getIntent();
        countCorrectAns = mIntent.getStringExtra("correctAns");
        testLenght = mIntent.getStringExtra("testLenght");
        isCompleted = mIntent.getStringExtra("isCompleted");
        sPart = mIntent.getStringExtra("part");
        sTest = mIntent.getStringExtra("test");



        mCountExam.setText(countCorrectAns+"/"+testLenght);
        iScore = Integer.parseInt(countCorrectAns) * 5;
        String sScore = Integer.toString(iScore);
        tvScore.setText(Integer.toString((iScore/7)));

        mRoot = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mUser = mRoot.child("Users");
        UID = currentUser.getUid();

        if (isCompleted.equals("1")){
            HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("uid",UID);
            hashMap.put("passScore",sScore);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("isCompleted/Test"+sTest+"/Part"+sPart+"/"+UID);
            reference.setValue(hashMap);
            DatabaseReference mIsCompleted = FirebaseDatabase.getInstance().getReference().child("isCompleted");
            mIsCompleted.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int countexam = 0;
                    int allScore = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            for (DataSnapshot data : ds.getChildren()){
                                if (data.getKey().equals(UID)){
                                    Completed completed = data.getValue(Completed.class);
                                    countexam++;
                                    allScore = allScore + Integer.parseInt(completed.passScore);
                                }
                            }
                        }
                    }
                    mUser.child(UID).child("score").setValue(Integer.toString(allScore));
                    mUser.child(UID).child("countexam").setValue(Integer.toString(countexam));

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        btnNextHome = (Button) findViewById(R.id.btnnexttohome);
        btnNextHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ShowResultActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}