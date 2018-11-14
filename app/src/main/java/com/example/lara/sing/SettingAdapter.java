package com.example.lara.sing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lara on 5/11/2018.
 */

public class SettingAdapter extends ArrayAdapter<Setting> {

    /** Resource ID for the background color for this list of words */
    private int mColorResourceId;


    public SettingAdapter(Context context, ArrayList<Setting> words) {
        super(context, 0, words);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.setting_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Setting currentItem = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID belarabi_text_view.
        TextView settingTextView = (TextView) listItemView.findViewById(R.id.setting_text_view);
        // Get the belarabi translation from the currentWord object and set this text on
        // the belarabi TextView.
        settingTextView.setText(currentItem.getDefaultTranslationId());

//        // Find the TextView in the list_item.xml layout with the ID default_text_view.
//        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
//        // Get the default translation from the currentWord object and set this text on
//        // the default TextView.
//        defaultTextView.setText(currentWord.getDefaultTranslationId());

        // Find the ImageView in the list_item.xml layout with the ID image.
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        // Check if an image is provided for this word or not
        if (currentItem.hasImage()) {
            // If an image is available, display the provided image based on the resource ID
            imageView.setImageResource(currentItem.getImageResourceId());
            // Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Otherwise hide the ImageView (set visibility to GONE)
            imageView.setVisibility(View.GONE);
        }

//        // Set the theme color for the list item
//        View textContainer = listItemView.findViewById(R.id.text_container);
//        // Find the color that the resource ID maps to
//        int color = ContextCompat.getColor(getContext(), mColorResourceId);
//        // Set the background color of the text container View
//        textContainer.setBackgroundColor(color);
//
//        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in
//        // the ListView.
        return listItemView;
    }
}

