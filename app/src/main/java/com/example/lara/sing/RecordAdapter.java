package com.example.lara.sing;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lara on 5/10/2018.
 */


    public class RecordAdapter extends ArrayAdapter<Record> {
        public RecordAdapter(Context context, int resource, List<Record> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_record, parent, false);
            }

            ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
            TextView messageTextView = (TextView) convertView.findViewById(R.id.recordTextView);
            TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

            Record message = getItem(position);

//        boolean isPhoto = message.getPhotoUrl() != null;
//        if (isPhoto) {
//            messageTextView.setVisibility(View.GONE);
//            photoImageView.setVisibility(View.VISIBLE);
//            Glide.with(photoImageView.getContext())
//                    .load(message.getPhotoUrl())
//                    .into(photoImageView);
//        } else {
//            messageTextView.setVisibility(View.VISIBLE);
//            photoImageView.setVisibility(View.GONE);
//            messageTextView.setText(message.getText());
//        }
//        authorTextView.setText(message.getName());

            return convertView;
        }
    }


