package com.example.lara.sing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MoreFragment extends Fragment {

    private FirebaseAuth mAuth;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_list, container, false);

        mAuth = FirebaseAuth.getInstance();


        // Create a list of settings
        final ArrayList<Setting> settings = new ArrayList<Setting>();
        settings.add(new Setting(R.string.edit_profile, R.drawable.edit_profile_pic));
        settings.add(new Setting(R.string.my_files, R.drawable.my_files_pic));
        settings.add(new Setting(R.string.my_messages, R.drawable.my_messages_pic));
        settings.add(new Setting(R.string.my_favorite, R.drawable.my_favorites_pic));
        settings.add(new Setting(R.string.my_community, R.drawable.my_community_pic));
        settings.add(new Setting(R.string.log_out, R.drawable.log_out_pic));



        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        SettingAdapter adapter = new SettingAdapter(getActivity(), settings);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

//         Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                switch( position )
                {
                    case 0:
                        startActivity(new Intent(getActivity(), SignUpProfileActivity.class));
                        break;
                    case 1:
//                        Intent intent = new Intent(this, youtube.class);
//                        startActivity(newActivity);
                        break;
                    case 2:
//                        Intent newActivity = new Intent(this, olympiakos.class);
//                        startActivity(newActivity);
//                        break;
                    case 3:
//                        Intent newActivity = new Intent(this, karaiskaki.class);
//                        startActivity(newActivity);
//                        break;
                    case 4:
//                        Intent newIntent = new Intent(this, reservetickets.class);
//                        startActivity(newActivity);
//                        break;
                    case 5:
                        mAuth.signOut();
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        break;
                }


//                Setting setting = settings.get(position);

////                // Release the media player if it currently exists because we are about to
////                // play a different sound file
////                releaseMediaPlayer();
//
//                // Get the {@link Word} object at the given position the user clicked on
//                Setting setting = settings.get(position);

//                // Request audio focus so in order to play the audio file. The app needs to play a
//                // short audio file, so we will request audio focus with a short amount of time
//                // with AUDIOFOCUS_GAIN_TRANSIENT.
//                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
//                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//
//                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//                    // We have audio focus now.
//
//                    // Create and setup the {@link MediaPlayer} for the audio resource associated
//                    // with the current word
//                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
//
//                    // Start the audio file
//                    mMediaPlayer.start();
//
//                    // Setup a listener on the media player, so that we can stop and release the
//                    // media player once the sound has finished playing.
//                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
//                }
            }
        });

        return rootView;
    }

}
