package com.congnghejava.toeicapp.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.activity.LoginActivity;
import com.congnghejava.toeicapp.activity.SettingsActivity;
import com.congnghejava.toeicapp.model.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    ImageView imgAvatar;
    TextView tvScore, tvCountExam, tvUserName;
    Button btnSetting, btnHelp, btnReview, btnLogout;
    FirebaseAuth mAuth;
    DatabaseReference mUserData;
    String UID;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mUserData = FirebaseDatabase.getInstance().getReference("Users");
        UID = currentUser.getUid().toString();

        mUserData.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getKey().equals(UID)){
                    User user = snapshot.getValue(User.class);
                    Picasso.get().load(user.avtlink).into(imgAvatar);
                    tvUserName.setText(user.name);
                    tvScore.setText(user.score);
                    tvCountExam.setText(user.countexam);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgAvatar = (ImageView) view.findViewById(R.id.avt);
        tvScore = (TextView) view.findViewById(R.id.score);
        tvCountExam = (TextView) view.findViewById(R.id.countexam);
        tvUserName = (TextView) view.findViewById(R.id.username);
        btnSetting = (Button) view.findViewById(R.id.btnsettings);
        btnHelp = (Button) view.findViewById(R.id.btnhelp);
        btnReview = (Button) view.findViewById(R.id.btnreview);
        btnLogout = (Button) view.findViewById(R.id.btnlogout);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"appgolearn@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "[Giúp đỡ] ");
                email.putExtra(Intent.EXTRA_TEXT, "Chào GoLearn App,\nVấn đề tôi gặp phải: ");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"appgolearn@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "[Đánh giá] ");
                email.putExtra(Intent.EXTRA_TEXT, "Chào GoLearn App,\nTôi đánh giá app: ");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

}