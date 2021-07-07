package com.congnghejava.toeicapp.handle;


import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.congnghejava.toeicapp.R;
import com.congnghejava.toeicapp.model.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static com.congnghejava.toeicapp.R.color.white;

public class CustomPracticeAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> headerItem;
    private HashMap<String, Vocabulary> childItem;
    private TextToSpeech textToSpeech;

    public CustomPracticeAdapter(Context mContext, List<String> headerItem, HashMap<String, Vocabulary> childItem) {
        this.mContext = mContext;
        this.headerItem = headerItem;
        this.childItem = childItem;
    }

    @Override
    public int getGroupCount() {
        return this.headerItem.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerItem.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childItem.get(headerItem.get(groupPosition));
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
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_practice_group, null);
        }
        TextView tv = convertView.findViewById(R.id.listpracticetitle);
        tv.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        Vocabulary vocabulary = (Vocabulary) getChild(groupPosition,childPosition);
        String childText = vocabulary.english;
//        String childText =(String) getChild(groupPosition,childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_practice, null);
        }
        TextView tv = convertView.findViewById(R.id.practicelistitem);
        Button btnSpeaker = (Button) convertView.findViewById(R.id.practicelistitemspeaker) ;
        tv.setText(childText);
        btnSpeaker.setClickable(true);

        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(vocabulary.english, TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
