package com.congnghejava.toeicapp.handle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.activity.GaMoListeningActivity;
import com.congnghejava.toeicapp.activity.GaMoListeningPart1Activity;
import com.congnghejava.toeicapp.activity.GaMoListeningPart2Activity;
import com.congnghejava.toeicapp.activity.GaMoListeningPart3Activity;
import com.congnghejava.toeicapp.activity.GaMoListeningPart4Activity;
import com.congnghejava.toeicapp.activity.ShowResultActivity;
import com.congnghejava.toeicapp.activity.TrungCapListeningPart1Activity;
import com.congnghejava.toeicapp.activity.TrungCapListeningPart2Activity;
import com.congnghejava.toeicapp.activity.TrungCapListeningPart3Activity;
import com.congnghejava.toeicapp.activity.TrungCapListeningPart4Activity;
import com.congnghejava.toeicapp.model.ListeningExam;
import com.congnghejava.toeicapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HandleListeningActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private MediaPlayer mediaPlayer;
    public TextView remainingTime, txtQuestion;
    private Handler updateHandler = new Handler();
    private SeekBar seekBar;
    private RadioGroup radioGroup;
    private Button btnPlay, btnBack, radioBtnA,radioBtnB, radioBtnC,radioBtnD , btnNext, btnPrev;
    private ImageView imageView;
    private CardView cardImgView;
    private int countCorrectAns, i, isCompleted, mLenghtPart;
    private boolean isPlaying = false, isChecked = false;
    private String sLevel, sPart, sTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_listening);
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
        btnPrev.setVisibility(View.GONE);

        if (sPart.equals("2")){
            radioBtnD.setVisibility(View.GONE);
        } else {
            radioBtnD.setVisibility(View.VISIBLE);
        }

        isCompleted = 0;
        countCorrectAns = 0;
//        Get lenght Part
        mLenghtPart = getLenghtPart(sPart);

//        Read database
        readDataBase(new FirebaseCallBack() {
            int i = 0;
            @Override
            public void onCallback(List<ListeningExam> listListenExam) {
                ListeningExam examTemp = listListenExam.get(i);
                txtQuestion.setText(examTemp.question);
                radioBtnA.setText("A."+examTemp.a);
                radioBtnB.setText("B."+examTemp.b);
                radioBtnC.setText("C."+examTemp.c);
                radioBtnD.setText("D."+examTemp.d);
                if (examTemp.imagelink.equals("")){
                    cardImgView.setVisibility(View.GONE);
                } else {
                    cardImgView.setVisibility(View.VISIBLE);
                    Picasso.get().load(examTemp.imagelink).into(imageView);
                }

//                 set event checked change for radio group
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (isChecked == false)
                            doOnRadioGroupChanged(group, checkedId, listListenExam.get(i).result);
                    }
                });
//                create event click on button Next
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( i < (mLenghtPart - 1)){
                            doClearChecked();
                            i++;
                            ListeningExam examTemp = listListenExam.get(i);
                            txtQuestion.setText(examTemp.question);
                            radioBtnA.setText("A."+examTemp.a);
                            radioBtnB.setText("B."+examTemp.b);
                            radioBtnC.setText("C."+examTemp.c);
                            radioBtnD.setText("D."+examTemp.d);
                            if (examTemp.imagelink.equals("")){
                                cardImgView.setVisibility(View.GONE);
                            } else {
                                cardImgView.setVisibility(View.VISIBLE);
                                Picasso.get().load(examTemp.imagelink).into(imageView);
                            }
                        } else {
                            Intent intent = new Intent (HandleListeningActivity.this, ShowResultActivity.class);
                            String temp1 = Integer.toString(countCorrectAns);
                            intent.putExtra("correctAns",temp1);
                            String temp2 = Integer.toString(listListenExam.size());
                            intent.putExtra("testLenght",temp2);
                            if (countCorrectAns - listListenExam.size() == 0){
                                isCompleted = 1;
                            }
                            String temp3 = Integer.toString(isCompleted);
                            intent.putExtra("isCompleted",temp3);
                            intent.putExtra("part",sPart);
                            intent.putExtra("test",sTest);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                }
            });
//        create media player for play music
        mediaPlayer = MediaPlayer.create(this, doCreateMediaPlayer(sLevel,sPart,sTest));
        remainingTime = (TextView)findViewById(R.id.remainingtime);
        seekBar.setMax((int) mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(this);
//        create event click on button play
        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isPlaying) {
                    pause(view);
                    btnPlay.setBackground(getResources().getDrawable(R.drawable.ic_play_50));
                }else{
                    play(view);
                    btnPlay.setBackground(getResources().getDrawable(R.drawable.ic_pause_50));
                }
                isPlaying = !isPlaying;
            }
        });
//        create handler function for action on seekbar
        updateHandler = new Handler();
        updateHandler.postDelayed(update, 100);
//        create event click on button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }

    private void readDataBase(FirebaseCallBack firebaseCallback) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        List<ListeningExam> listTemp = new ArrayList<>();
        DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mListeningRef = mRoot.child("ListeningExam");
        mListeningRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ListeningExam exam = ds.getValue(ListeningExam.class);
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


    private void AnhXa(){
        btnPrev = (Button) findViewById(R.id.btnlisbai1prev);
        btnPrev.setVisibility(View.GONE);
        imageView = (ImageView) findViewById(R.id.imglistest);
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        btnPlay = (Button) findViewById(R.id.btngamop1play);
        btnBack = (Button) findViewById(R.id.btnbacktolis);
        radioGroup = (RadioGroup) findViewById(R.id.radiogrouplisbai1);
        radioBtnA = (Button) findViewById(R.id.rdbtnlisa);
        radioBtnB = (Button) findViewById(R.id.rdbtnlisb);
        radioBtnC = (Button) findViewById(R.id.rdbtnlisc);
        radioBtnD = (Button) findViewById(R.id.rdbtnlisd);
        btnNext = (Button) findViewById(R.id.btnlisbai1next);
        txtQuestion = (TextView) findViewById(R.id.lisquestion);
        cardImgView = (CardView) findViewById(R.id.liscardimageview);
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

    private int doCreateMediaPlayer(String sLevel, String sPart, String sTest) {
        switch (sPart){
            case "1":{
                switch (sTest){
                    case "1":{
                        return R.raw.lct1p1;
                    }
                    case "2":{
                        return R.raw.lct2p1;
                    }
                    case "3":{
                        return R.raw.lct3p1;
                    }
                    case "4":{
                        return R.raw.lct4p1;
                            }
                    case "5":{
                        return R.raw.lct5p1;
                    }
                    case "6":{
                        return R.raw.lct6p1;
                    }
                    case "7":{
                        return R.raw.lct7p1;
                    }
                }
            }
            case "2":{
                switch (sTest){
                    case "1":{
                        return R.raw.lct1p2;
                    }
                    case "2":{
                        return R.raw.lct2p2;
                    }
                    case "3":{
                        return R.raw.lct3p2;
                    }
                    case "4":{
                        return R.raw.lct4p2;
                    }
                    case "5":{
                        return R.raw.lct5p2;
                    }
                    case "6":{
                        return R.raw.lct6p2;
                    }
                    case "7":{
                        return R.raw.lct7p2;
                    }
                }
            }
            case "3":{
                switch (sTest){
                    case "1":{
                        return R.raw.lct1p3;
                    }
                    case "2":{
                        return R.raw.lct2p3;
                    }
                    case "3":{
                        return R.raw.lct3p3;
                    }
                    case "4":{
                        return R.raw.lct4p3;
                    }
                    case "5":{
                        return R.raw.lct5p3;
                    }
                    case "6":{
                        return R.raw.lct6p3;
                    }
                    case "7":{
                        return R.raw.lct7p3;
                    }
                }
            }
            case "4":{
                switch (sTest){
                    case "1":{
                        return R.raw.lct1p4;
                    }
                    case "2":{
                        return R.raw.lct2p4;
                    }
                    case "3":{
                        return R.raw.lct3p4;
                    }
                    case "4":{
                        return R.raw.lct4p4;
                    }
                    case "5":{
                        return R.raw.lct5p4;
                    }
                    case "6":{
                        return R.raw.lct6p4;
                    }
                    case "7":{
                        return R.raw.lct7p4;
                    }
                }
            }
        }
        return 0;
    }

    private int getLenghtPart(String sPart){
        int startNumber = 0;
        switch (sPart){
            case "1":
                startNumber = 6;
                break;
            case "2":
                startNumber = 25;
                break;
            case "3":
                startNumber = 39;
                break;
            case "4":
                startNumber = 30;
                break;
        }
        return startNumber;
    }

    private void doOnRadioGroupChanged(RadioGroup group, int checkedId, String sResult) {
        int checkedRadioId = group.getCheckedRadioButtonId();
        Button btnTemp = (Button) findViewById(checkedId);
        switch (sResult){
            case "a": {
                if (checkedRadioId == R.id.rdbtnlisa) {
                    radioBtnA.setTextColor(getResources().getColor(R.color.green));
                    countCorrectAns++;
                } else {
                    btnTemp.setTextColor(getResources().getColor(R.color.red));
                    radioBtnA.setTextColor(getResources().getColor(R.color.green));
                }
                break;
            }
            case "b":{
                if (checkedRadioId == R.id.rdbtnlisb){
                    radioBtnB.setTextColor(getResources().getColor(R.color.green));
                    countCorrectAns++;
                } else {
                    btnTemp.setTextColor(getResources().getColor(R.color.red));
                    radioBtnB.setTextColor(getResources().getColor(R.color.green));
                }
                break;
            }
            case "c":{
                if (checkedRadioId == R.id.rdbtnlisc){
                    radioBtnC.setTextColor(getResources().getColor(R.color.green));
                    countCorrectAns++;
                }else {
                    btnTemp.setTextColor(getResources().getColor(R.color.red));
                    radioBtnC.setTextColor(getResources().getColor(R.color.green));
                }
                break;
            }
            case "d":{
                if (checkedRadioId == R.id.rdbtnlisd){
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

    private interface FirebaseCallBack{
        void onCallback(List<ListeningExam> listListenExam);
    }


    public void play(View view)
    {
        mediaPlayer.start();
    }

    public void pause(View view)
    {
        mediaPlayer.pause();
    }

    private Runnable update = new Runnable()
    {
        public void run()
        {
            long currentTime = mediaPlayer.getCurrentPosition();
            seekBar.setProgress((int)currentTime);
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(currentTime);
            int seconds = (int)TimeUnit.MILLISECONDS.toSeconds(currentTime) - minutes * 60;
            remainingTime.setText(String.format("%02d:%02d",minutes, seconds));
            updateHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser)
            mediaPlayer.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}