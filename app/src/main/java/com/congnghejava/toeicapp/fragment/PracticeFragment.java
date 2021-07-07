package com.congnghejava.toeicapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.handle.HandlePracticeActivity;
import com.congnghejava.toeicapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;


public class PracticeFragment extends Fragment {


    Button btnChaoHoi,btnTuGioiThieu, btnGiaDinh, btnTinhYeu, btnCamNghi, btnThucPham, btnThoiGian, btnMuaSam, btnDuLich, btnThoiTiet, btnSanBay, btnKhachSan;
    ImageView imageUserAvatar;

    public PracticeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pratice, container, false);

        btnChaoHoi = (Button) view.findViewById(R.id.btnchaohoi);
        btnTuGioiThieu = (Button) view.findViewById(R.id.btngioithieu);
        btnGiaDinh = (Button) view.findViewById(R.id.btngiadinh);
        btnTinhYeu = (Button) view.findViewById(R.id.btntinhyeu);
        btnCamNghi = (Button) view.findViewById(R.id.btncamnghi);
        btnThucPham = (Button) view.findViewById(R.id.btnthucpham);
        btnThoiGian = (Button) view.findViewById(R.id.btnthoigian);
        btnMuaSam = (Button) view.findViewById(R.id.btnmuasam);
        btnDuLich = (Button) view.findViewById(R.id.btndulic);
        btnThoiTiet = (Button) view.findViewById(R.id.btnthoitiet);
        btnSanBay = (Button) view.findViewById(R.id.btnsanbay);
        btnKhachSan = (Button) view.findViewById(R.id.btnkhachsan);
        imageUserAvatar = view.findViewById(R.id.avt);

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


        btnChaoHoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Chào hỏi");
                startActivity(intent);
            }
        });

        btnTuGioiThieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Tự giới thiệu");
                startActivity(intent);
            }
        });

        btnGiaDinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Gia đình - bạn bè");
                startActivity(intent);
            }
        });

        btnTinhYeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Tình yêu - hôn nhân");
                startActivity(intent);
            }
        });

        btnCamNghi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Cảm nghĩ - cảm xúc");
                startActivity(intent);
            }
        });

        btnThucPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Thực phẩm - đồ uống");
                startActivity(intent);
            }
        });

        btnThoiGian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Thời gian - số đếm");
                startActivity(intent);
            }
        });

        btnMuaSam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Mua sắm");
                startActivity(intent);
            }
        });

        btnDuLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Du lịch - đi lại");
                startActivity(intent);
            }
        });

        btnThoiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Thời tiết - thiên nhiên");
                startActivity(intent);
            }
        });

        btnKhachSan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Khách sạn - phòng ở");
                startActivity(intent);
            }
        });

        btnSanBay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), HandlePracticeActivity.class);
                intent.putExtra("keyword","Sân bay - chuyến bay");
                startActivity(intent);
            }
        });
        return view;
    }
}