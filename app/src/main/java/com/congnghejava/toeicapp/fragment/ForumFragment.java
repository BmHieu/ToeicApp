package com.congnghejava.toeicapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.handle.CustomForumAdapter;
import com.congnghejava.toeicapp.handle.CustomPracticeAdapter;
import com.congnghejava.toeicapp.handle.HandlePracticeActivity;
import com.congnghejava.toeicapp.model.Comments;
import com.congnghejava.toeicapp.model.User;
import com.congnghejava.toeicapp.model.Vocabulary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForumFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mUser, mComments;
    ImageView imageUserAvatar;
    ExpandableListView expandableListView;
    CustomForumAdapter customForumAdapter;
    List<Comments> listDataHeader;
    HashMap<Comments, List<Comments>> listDataChild;
    Button btnPost;
    EditText edtPostComment;
    int lastExpandedPosition = -1;

    public ForumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        imageUserAvatar = view.findViewById(R.id.avt);
        btnPost = view.findViewById(R.id.btnpostcomment);
        edtPostComment = view.findViewById(R.id.edtpostcomment);
        expandableListView = view.findViewById(R.id.expandableForumListView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mUser = FirebaseDatabase.getInstance().getReference("Users");
        mComments = FirebaseDatabase.getInstance().getReference("Comments");
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<Comments, List<Comments>>();

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

        SetStandardGroups();
        customForumAdapter = new CustomForumAdapter(getActivity(), listDataHeader, listDataChild);
        expandableListView.setAdapter(customForumAdapter);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtPostComment.getText().toString();
                if (content.matches("")){
                    Toast.makeText(getActivity(),"Không có nội dung",Toast.LENGTH_SHORT).show();
                } else {
                    Comments comments = new Comments(content,currentUser.getUid(),currentUser.getPhotoUrl().toString(),currentUser.getDisplayName(),"main");
                    mComments.push().push().setValue(comments);
                }
                edtPostComment.setText("");
                edtPostComment.setHint("Bạn muốn viết gì");
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                edtPostComment.setHint("Bạn muốn viết gì...");
            }
        });

        readDataBase(new FirebaseCallBack() {
            @Override
            public void onCallback(List<String> listKey) {
                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition){
                            expandableListView.collapseGroup(lastExpandedPosition);
                        }
                        lastExpandedPosition = groupPosition;
                        edtPostComment.setHint("Bạn muốn trả lời gì...");
                        btnPost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String content = edtPostComment.getText().toString();
                                if (content.matches("")){
                                    Toast.makeText(getActivity(),"Không có nội dung",Toast.LENGTH_SHORT).show();
                                } else {
                                    Comments comments = new Comments(content,currentUser.getUid(),currentUser.getPhotoUrl().toString(),currentUser.getDisplayName(),"reply");
                                    String path = listKey.get(groupPosition);
                                    mComments.child(path).push().setValue(comments);
                                }
                                edtPostComment.setText("");
                                edtPostComment.setHint("Bạn muốn viết gì");
                            }
                        });
                    }
                });
            }
        });

        return view;
    }

    private void SetStandardGroups() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        mComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 0;
                listDataHeader.clear();
                listDataChild.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    List<Comments> listChild = new ArrayList<Comments>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Comments comment = ds.getValue(Comments.class);
                        if (comment.category.toString().equals("main")){
                            listDataHeader.add(comment);
                        } else {
                            listChild.add(comment);
                        }
                    }
                    listDataChild.put(listDataHeader.get(counter),listChild);
                    counter++;
                }
                customForumAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseCallBack{
        void onCallback(List<String> listKey);
    }

    private void readDataBase(ForumFragment.FirebaseCallBack firebaseCallback) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        List<String> listKey = new ArrayList<>();
        DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mCommentsRef = mRoot.child("Comments");
        mCommentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    listKey.add(ds.getKey());
                }
                firebaseCallback.onCallback(listKey);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }
}