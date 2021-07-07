package com.congnghejava.toeicapp.handle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.fragment.PracticeFragment;
import com.congnghejava.toeicapp.model.ReadingExam;
import com.congnghejava.toeicapp.model.Vocabulary;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HandlePracticeActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    CustomPracticeAdapter customPracticeAdapter;
    List<String> listDataHeader;
    HashMap<String, Vocabulary> listDataChild;
    DatabaseReference mData;
    String keyword;
    Button btnSpeaker, btnBack;
    TextView txtTitle;
    int lastExpandedPosition = -1;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_practice);
        getSupportActionBar().hide();
        textToSpeech = new TextToSpeech(getApplication(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
        btnSpeaker = (Button) findViewById(R.id.practicelistitemspeaker) ;
        btnBack = (Button) findViewById(R.id.btnbacktopractice);
        txtTitle = (TextView) findViewById(R.id.practicetitle);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, Vocabulary>();

        mData = FirebaseDatabase.getInstance().getReference("vocabulary");
        expandableListView = findViewById(R.id.expandableListView);

        Intent mIntent = getIntent();
        keyword = (String) mIntent.getStringExtra("keyword");
        txtTitle.setText(keyword);


        SetStandardGroups();
        customPracticeAdapter = new CustomPracticeAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(customPracticeAdapter);

        readDataBase(new FirebaseCallBack() {
            @Override
            public void onCallback(List<Vocabulary> listVocabulary) {
                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition){
                            expandableListView.collapseGroup(lastExpandedPosition);
                        }
                        lastExpandedPosition = groupPosition;
                        textToSpeech.speak(listVocabulary.get(groupPosition).english,TextToSpeech.QUEUE_FLUSH,null);
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

    private void SetStandardGroups() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Vocabulary vocabulary = ds.getValue(Vocabulary.class);
                    if (vocabulary.keyword.equals(keyword)){
                        listDataHeader.add(vocabulary.vietnamese);
                        listDataChild.put(listDataHeader.get(counter), vocabulary);
                        counter++;
                    }
                }
                customPracticeAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    private interface FirebaseCallBack{
        void onCallback(List<Vocabulary> listVocabulary);
    }

    private void readDataBase(HandlePracticeActivity.FirebaseCallBack firebaseCallback) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        List<Vocabulary> listTemp = new ArrayList<>();
        DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mVocabularyRef = mRoot.child("vocabulary");
        mVocabularyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Vocabulary vocabulary = ds.getValue(Vocabulary.class);
                   if(vocabulary.keyword.equals(keyword)){
                        listTemp.add(vocabulary);
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