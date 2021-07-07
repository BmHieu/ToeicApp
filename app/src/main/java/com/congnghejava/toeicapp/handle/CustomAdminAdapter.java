package com.congnghejava.toeicapp.handle;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.model.Comments;
import com.congnghejava.toeicapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CustomAdminAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<User> userHeader;
    private HashMap<User, User> childUser;

    public CustomAdminAdapter(Context mContext, List<User> userHeader, HashMap<User, User> childUser) {
        this.mContext = mContext;
        this.userHeader = userHeader;
        this.childUser = childUser;
    }

    @Override
    public int getGroupCount() {
        return this.userHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.userHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childUser.get(this.userHeader.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        User headerUser = (User) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_user, null);
        }
        TextView tv = convertView.findViewById(R.id.tvadminusername);
        ImageView imageView = convertView.findViewById(R.id.ivadminuseravatar);
        ImageView imageViewEditMore = convertView.findViewById(R.id.iveditmore);
        tv.setText(headerUser.name);
        Picasso.get().load(headerUser.avtlink).into(imageView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded){
                    ((ExpandableListView) parent).collapseGroup(groupPosition);
                } else {
                    ((ExpandableListView) parent).expandGroup(groupPosition, true);
                }
            }
        });

        imageViewEditMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(mContext,imageViewEditMore, Gravity.END);
                popupMenu.getMenu().add(Menu.NONE,0,0,"Xóa tài khoản");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == 0 ){
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                            userRef.child(headerUser.uid).removeValue();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        User childUser = (User) getChild(groupPosition,childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child_user, null);
        }
        TextView tvEmail = convertView.findViewById(R.id.tvadminuseremail);
        TextView tvScore = convertView.findViewById(R.id.tvadminuserscore);
        TextView tvCountExam = convertView.findViewById(R.id.tvadminusercountexam);

        tvEmail.setText("Email:"+childUser.email);
        tvScore.setText("Điểm:"+childUser.score);
        tvCountExam.setText("Hoàn thành:"+childUser.countexam);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
