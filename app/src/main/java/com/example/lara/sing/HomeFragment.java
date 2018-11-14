package com.example.lara.sing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeFragment extends Fragment {

    public static final String TAG = "RetrievingStatus";
    public static final String KEY_firstName = "First Name";
    public static final String KEY_lastName = "Last Name";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.document("Users/UserOne/Info/ProfileInfo");


    private TextView titleTextView;
    private TextView timeTextView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =   inflater.inflate(R.layout.list_item_home, container, false);
       // loadInfo();

//        Intent i = getActivity().getIntent();
//        Bundle extras = i.getExtras();
//
//        if (extras != null) {
//            loadInfo();
//        }



        return rootView;



//        listenBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) throws IllegalArgumentException,
//                    SecurityException, IllegalStateException {
//
//                audioIv.setEnabled(false);
//                videoIv.setEnabled(false);
//                listenBtn.setText("STOP");
//
//                mMediaPlayer = new MediaPlayer();
//                try {
//                    mMediaPlayer.setDataSource(outputFile);
//                    mMediaPlayer.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                // Request audio focus so in order to play the audio file. The app needs to play a
//                // short audio file, so we will request audio focus with a short amount of time
//                // with AUDIOFOCUS_GAIN_TRANSIENT.
//                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
//                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//
//                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//                    // We have audio focus now.
//
////                    // Create and setup the {@link MediaPlayer} for the audio resource associated
////                    // with the current word
////                    try {
////                        mMediaPlayer.setDataSource(outputFile);
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
//
//                    // Start the audio file
//                    mMediaPlayer.start();
//
//                    // Setup a listener on the media player, so that we can stop and release the
//                    // media player once the sound has finished playing.
//                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
//                }
//                Toast.makeText(getActivity(), "You are now listening to your masterpiece, long press to stop it",
//                        Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//        listenBtn.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                audioIv.setEnabled(false);
//                videoIv.setEnabled(false);
//                actionLL.setVisibility(View.VISIBLE);
//                discardBtn.setVisibility(View.VISIBLE);
//                uploadBtn.setVisibility(View.VISIBLE);
//
//                if(mMediaPlayer != null){
//                    mMediaPlayer.stop();
//                    mMediaPlayer.release();
//                    startRecording();
//                }
//                return false;
//            }
//        });


//        editTextLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(titleEt.getText().toString().length()>3) {
//
//                    current = GetUTCdatetimeAsString();
//                    Record rc = new Record(userName, AccountType, level, uid, current, 0, encoded, sp.getSelectedItemPosition()+1,titleEt.getText().toString());
//                    register(rc);
//                }
//                else
//                    Toast.makeText(getActivity(),"Please enter the title of the song ..",Toast.LENGTH_SHORT).show();
//            }
//        });
//


//        DocumentReference docRef = db.collection("Users").document("Records");
//
//        db.collection("Users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });




    }

    public void loadInfo()
    {

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
//                    String username = documentSnapshot.getString(KEY_EMAIL);
//                    String first = documentSnapshot.getString(KEY_firstName);
//                    String last = documentSnapshot.getString(KEY_lastName);
//                    String fullName = first + " " + last;
//                    User user = documentSnapshot.toObject(User.class);
                    User user = documentSnapshot.toObject(User.class);
                    titleTextView.setText(user.getFirstName() + " " + user.getLastName());


                } else {
                    Toast.makeText(getActivity(), "Document does not exit!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                 Log.d(TAG, e.toString());
             }
         });
    }
}
