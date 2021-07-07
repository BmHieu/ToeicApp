package com.congnghejava.toeicapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.activity.GaMoListeningActivity;
import com.congnghejava.toeicapp.activity.GaMoReadingActivity;
import com.congnghejava.toeicapp.activity.TrungCapListeningActivity;
import com.congnghejava.toeicapp.activity.TrungCapReadingActivity;
import com.congnghejava.toeicapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {


    FirebaseAuth mAuth;
    Button btnlisgamo,btnreadgamo,btnlistrungcap,btnreadtrungcap;
    ImageView imageUserAvatar;
    DatabaseReference mUser;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnlisgamo = (Button) view.findViewById(R.id.btnlisgamo);
        btnreadgamo = (Button) view.findViewById(R.id.btnreadgamo);
        btnlistrungcap = (Button) view.findViewById(R.id.btnlistrungcap);
        btnreadtrungcap = (Button) view.findViewById(R.id.btnreadtrungcap);
        imageUserAvatar = view.findViewById(R.id.avt);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mUser = FirebaseDatabase.getInstance().getReference("Users");

        mUser.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User  user = snapshot.getValue(User.class);
                Picasso.get().load(user.avtlink).into(imageUserAvatar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnlisgamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GaMoListeningActivity.class);
                startActivity(intent);
            }
        });
        btnreadgamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GaMoReadingActivity.class);
                startActivity(intent);
            }
        });
        btnlistrungcap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrungCapListeningActivity.class);
                startActivity(intent);
            }
        });
        btnreadtrungcap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrungCapReadingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}