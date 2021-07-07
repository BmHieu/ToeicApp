package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.congnghejava.toeicapp.fragment.ForumFragment;
import com.congnghejava.toeicapp.fragment.HomeFragment;
import com.congnghejava.toeicapp.fragment.PracticeFragment;
import com.congnghejava.toeicapp.fragment.ProfileFragment;
import com.congnghejava.toeicapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();

        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.container_nav,fragment1,"");
        ft1.commit();

        bottomNavigationView = findViewById(R.id.navbottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_nav:
                        HomeFragment fragment1 = new HomeFragment();
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.container_nav,fragment1,"");
                        ft1.commit();
                        return true;
                    case R.id.pratice_nav:
                        PracticeFragment fragment2 = new PracticeFragment();
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.container_nav,fragment2,"");
                        ft2.commit();
                        return true;
                    case R.id.forum_nav:
                        ForumFragment fragment3 = new ForumFragment();
                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.container_nav,fragment3,"");
                        ft3.commit();
                        return true;
                    case R.id.profile_nav:
                        ProfileFragment fragment4 = new ProfileFragment();
                        FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                        ft4.replace(R.id.container_nav,fragment4,"");
                        ft4.commit();
                        return true;
                }
                return false;
            }
        });
    }
}