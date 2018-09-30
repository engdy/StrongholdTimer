package com.strongholdgames.timer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TutorialAdapter extends ArrayAdapter<Tutorial> {
    private List<Tutorial> tutorials = new ArrayList<>();
    private ImageView imgIcon;
    private TextView txtLabel;

    public TutorialAdapter(Context context, int textViewResourceId, List<Tutorial> objects) {
        super(context, textViewResourceId, objects);
        tutorials = objects;
    }

    public int getCount() {
        return tutorials.size();
    }

    public Tutorial getItem(int index) {
        return tutorials.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.tutrow, parent, false);
        }
        Tutorial tutorial = getItem(position);
        imgIcon = row.findViewById(R.id.icon);
        txtLabel = row.findViewById(R.id.label);
        txtLabel.setText(tutorial.stringid);
        imgIcon.setImageResource(tutorial.resid);
        return row;
    }
}
