package com.congnghejava.toeicapp.handle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.activity.GaMoReadingActivity;
import com.congnghejava.toeicapp.activity.ShowResultActivity;
import com.congnghejava.toeicapp.model.ListeningExam;
import com.congnghejava.toeicapp.model.ReadingExam;
import com.congnghejava.toeicapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HandleReadingActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    Button btnBack, radioBtnA,radioBtnB, radioBtnC,radioBtnD , btnNext, btnPrev;
    String sLevel, sPart, sTest;
    TextView tvQuestion, tvParagraph;
    CardView cardView;
    int countCorrectAns, i, isCompleted, mLenghtPart;
    boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_reading);
        getSupportActionBar().hide();

//        declaration and initialization all variables
        AnhXa();

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

//        Get part and test of exam
        Intent mIntent = getIntent();
        sLevel = mIntent.getStringExtra("level");
        sPart = mIntent.getStringExtra("part");
        sTest = mIntent.getStringExtra("test");

        btnNext.setEnabled(false);
//        btnPrev.setVisibility(View.GONE);
        if (sPart.equals("5")){
            cardView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.VISIBLE);
        }

        isCompleted = 0;
        countCorrectAns = 0;

//        Get lenght Part
        mLenghtPart = getLenghtPart(sPart);
//        Read database
        readDataBase(new HandleReadingActivity.FirebaseCallBack() {
            int i = 0;
            @Override
            public void onCallback(List<ReadingExam> listReadingExam) {
                ReadingExam examTemp = listReadingExam.get(i);
                tvParagraph.setText(examTemp.paragraph);
                tvQuestion.setText(examTemp.question);
                radioBtnA.setText("A."+examTemp.a);
                radioBtnB.setText("B."+examTemp.b);
                radioBtnC.setText("C."+examTemp.c);
                radioBtnD.setText("D."+examTemp.d);
//                 set event checked change for radio group
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (isChecked == false)
                            doOnRadioGroupChanged(group, checkedId, listReadingExam.get(i).result);
                    }
                });
//                create event click on button Next
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( i < (mLenghtPart - 1)){
                            doClearChecked();
                            i++;
                            ReadingExam examTemp = listReadingExam.get(i);
                            tvParagraph.setText(examTemp.paragraph);
                            tvQuestion.setText("");
                            radioBtnA.setText("A."+examTemp.a);
                            radioBtnB.setText("B."+examTemp.b);
                            radioBtnC.setText("C."+examTemp.c);
                            radioBtnD.setText("D."+examTemp.d);
                        } else {
                            Intent intent = new Intent (HandleReadingActivity.this, ShowResultActivity.class);
                            String temp1 = Integer.toString(countCorrectAns);
                            intent.putExtra("correctAns",temp1);
                            String temp2 = Integer.toString(listReadingExam.size());
                            intent.putExtra("testLenght",temp2);
                            if (countCorrectAns - listReadingExam.size() == 0){
                                isCompleted = 1;
                            }
                            String temp3 = Integer.toString(isCompleted);
                            intent.putExtra("isCompleted",temp3);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }

    private void doClearChecked() {
        radioGroup.clearCheck();
        radioBtnA.setClickable(true);
        radioBtnB.setClickable(true);
        radioBtnC.setClickable(true);
        radioBtnD.setClickable(true);
        radioBtnA.setTextColor(getResources().getColor(R.color.black));
        radioBtnB.setTextColor(getResources().getColor(R.color.black));
        radioBtnC.setTextColor(getResources().getColor(R.color.black));
        radioBtnD.setTextColor(getResources().getColor(R.color.black));
        btnNext.setEnabled(false);
        isChecked = false;
    }

    private void doOnRadioGroupChanged(RadioGroup group, int checkedId, String sResult) {
        int checkedRadioId = group.getCheckedRadioButtonId();
        Button btnTemp = (Button) findViewById(checkedId);
        switch (sResult){
            case "a": {
                if (checkedRadioId == R.id.rdbtnreada) {
                    radioBtnA.setTextColor(getResources().getColor(R.color.green));
                    countCorrectAns++;
                } else {
                    btnTemp.setTextColor(getResources().getColor(R.color.red));
                    radioBtnA.setTextColor(getResources().getColor(R.color.green));
                }
                break;
            }
            case "b":{
                if (checkedRadioId == R.id.rdbtnreadb){
                    radioBtnB.setTextColor(getResources().getColor(R.color.green));
                    countCorrectAns++;
                } else {
                    btnTemp.setTextColor(getResources().getColor(R.color.red));
                    radioBtnB.setTextColor(getResources().getColor(R.color.green));
                }
                break;
            }
            case "c":{
                if (checkedRadioId == R.id.rdbtnreadc){
                    radioBtnC.setTextColor(getResources().getColor(R.color.green));
                    countCorrectAns++;
                }else {
                    btnTemp.setTextColor(getResources().getColor(R.color.red));
                    radioBtnC.setTextColor(getResources().getColor(R.color.green));
                }
                break;
            }
            case "d":{
                if (checkedRadioId == R.id.rdbtnreadd){
                    radioBtnD.setTextColor(getResources().getColor(R.color.green));
                    countCorrectAns++;
                }else {
                    btnTemp.setTextColor(getResources().getColor(R.color.red));
                    radioBtnD.setTextColor(getResources().getColor(R.color.green));
                }
                break;
            }
        }
        radioBtnA.setClickable(false);
        radioBtnB.setClickable(false);
        radioBtnC.setClickable(false);
        radioBtnD.setClickable(false);
        btnNext.setEnabled(true);
        isChecked = true;
    }

    private int getLenghtPart(String sPart) {
        int lenghtPart = 0;
        switch (sPart){
            case "5":
                lenghtPart = 30;
                break;
            case "6":
                lenghtPart = 16;
                break;
            case "7":
                lenghtPart = 54;
                break;
        }
        return lenghtPart;
    }

    private void AnhXa() {
        radioGroup = (RadioGroup) findViewById(R.id.radioreadgroup);
        btnBack = (Button) findViewById(R.id.btnbacktoread);
        radioBtnA = (RadioButton) findViewById(R.id.rdbtnreada);
        radioBtnB = (RadioButton) findViewById(R.id.rdbtnreadb);
        radioBtnC = (RadioButton) findViewById(R.id.rdbtnreadc);
        radioBtnD = (RadioButton) findViewById(R.id.rdbtnreadd);
        btnNext = (Button) findViewById(R.id.btnhandlereadnext);
        tvQuestion = (TextView) findViewById(R.id.readquestion);
        tvParagraph = (TextView) findViewById(R.id.tvreadparagraph);
        cardView = (CardView) findViewById(R.id.readcardparagraphview);
    }

    private interface FirebaseCallBack{
        void onCallback(List<ReadingExam> listReadingExam);
    }

    private void readDataBase(HandleReadingActivity.FirebaseCallBack firebaseCallback) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        List<ReadingExam> listTemp = new ArrayList<>();
        DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mReadingRef = mRoot.child("ReadingExam");
        mReadingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){
                    ReadingExam exam = ds.getValue(ReadingExam.class);
                    if (exam.part.equals(sPart)&&exam.test.equals(sTest)){
                        listTemp.add(exam);
                    }
                }
                firebaseCallback.onCallback(listTemp);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

}