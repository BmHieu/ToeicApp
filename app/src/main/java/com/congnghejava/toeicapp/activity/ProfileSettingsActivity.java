package com.congnghejava.toeicapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.model.Comments;
import com.congnghejava.toeicapp.model.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
import java.io.InputStream;
import java.util.Calendar;

public class ProfileSettingsActivity extends AppCompatActivity {

    Button btnBack, btnSave, btnEditAvatarImage;
    ImageView imageAvatarView;
    EditText edtUserName;
    int REQUEST_IMAGE = 1;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    FirebaseAuth mAuth;
    DatabaseReference mUserData;
    FirebaseUser currentUser;
    StorageReference imageAvatarRef;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        getSupportActionBar().hide();

        btnBack = findViewById(R.id.btnbacktosettings);
        btnSave = findViewById(R.id.btnsaveprofilesettings);
        btnEditAvatarImage = findViewById(R.id.editavtimage);
        imageAvatarView = findViewById(R.id.avt);
        edtUserName = findViewById(R.id.editusername_settings);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mUserData = FirebaseDatabase.getInstance().getReference("Users");
        UID = currentUser.getUid().toString();
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReferenceFromUrl("gs://toeicapp-ee2c6.appspot.com/UserAvatar");

        mUserData.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getKey().equals(UID)) {
                    User user = snapshot.getValue(User.class);
                    Picasso.get().load(user.avtlink).into(imageAvatarView);
                    edtUserName.setText(user.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnEditAvatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_IMAGE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(ProfileSettingsActivity.this);
                progressDialog.show();

                imageAvatarRef = storageRef.child("imageavatar" + currentUser.getUid() + ".jpg");
                imageAvatarView.setDrawingCacheEnabled(true);
                imageAvatarView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageAvatarView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = imageAvatarRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplication(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        String sUserName = edtUserName.getText().toString();
                        mUserData.child(UID).child("name").setValue(sUserName);
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(sUserName)
                                .build();
                        FirebaseUser fUser = mAuth.getCurrentUser();
                        fUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    onBackPressed();
                                    Toast.makeText(getApplication(), "Cập nhật thông tin thành công!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        progressDialog.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageAvatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String sUserName = edtUserName.getText().toString();
                                Uri downloadUri = uri;
                                mUserData.child(UID).child("avtlink").setValue(downloadUri.toString());
                                mUserData.child(UID).child("name").setValue(sUserName);
                                FirebaseUser fUser = mAuth.getCurrentUser();
                                updateUserProfile(fUser, edtUserName.getText().toString(), downloadUri);
                                onBackPressed();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });

            }
        });
    }

    private void updateUserProfile(FirebaseUser fUser, String sUserName, Uri ImageURL) {
        Uri URL = ImageURL;
        String UserName = sUserName;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(UserName)
                .setPhotoUri(URL)
                .build();

        DatabaseReference mComments = FirebaseDatabase.getInstance().getReference("Comments");
        mComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Comments comments = ds.getValue(Comments.class);
                        String keyTemp1 = dataSnapshot.getKey();
                        String keyTemp2 = ds.getKey();
                        if (comments.userid.equals(UID)){
                            mComments.child(keyTemp1).child(keyTemp2).child("username").setValue(sUserName);
                            mComments.child(keyTemp1).child(keyTemp2).child("useravatar").setValue(ImageURL.toString());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplication(),"Cập nhật thông tin thành công!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {
            if (requestCode == REQUEST_IMAGE){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageAvatarView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}