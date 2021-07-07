package com.congnghejava.toeicapp.handle;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.fragment.ForumFragment;
import com.congnghejava.toeicapp.model.Comments;
import com.congnghejava.toeicapp.model.Vocabulary;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CustomForumAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Comments> contentComment;
    private HashMap<Comments, List<Comments>> childComment;

    public CustomForumAdapter(Context mContext, List<Comments> contentComment, HashMap<Comments, List<Comments>> childComment) {
        this.mContext = mContext;
        this.contentComment = contentComment;
        this.childComment = childComment;
    }

    @Override
    public int getGroupCount() {
        return this.contentComment.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childComment.get(this.contentComment.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.contentComment.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childComment.get(this.contentComment.get(groupPosition)).get(childPosition);
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
        Comments commentsTitle = (Comments) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_forum, null);
        }
        TextView tv = convertView.findViewById(R.id.listcommenttitle);
        tv.setText(commentsTitle.content);
        ImageView imageViewAvt = convertView.findViewById(R.id.ivuseravatarcomment);
        Picasso.get().load(commentsTitle.useravatar).into(imageViewAvt);
        TextView textView = convertView.findViewById(R.id.commentusername);
        textView.setText(commentsTitle.username);
        Button btnReplyComments = convertView.findViewById(R.id.replycomment);
        btnReplyComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded){
                    ((ExpandableListView) parent).collapseGroup(groupPosition);
                } else {
                    ((ExpandableListView) parent).expandGroup(groupPosition, true);
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Comments comments = (Comments) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_forum, null);
        }
        TextView expandedListReplyCommentView = (TextView) convertView.findViewById(R.id.listreplycommenttitle);
        expandedListReplyCommentView.setText(comments.content);
        ImageView imageViewAvt = convertView.findViewById(R.id.ivuseravatarreplycomment);
        Picasso.get().load(comments.useravatar).into(imageViewAvt);
        TextView textView = convertView.findViewById(R.id.replycommentusername);
        textView.setText(comments.username);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
