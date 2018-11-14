package com.example.lara.sing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class SingFragment extends Fragment {


    private String outputFile = null;
    private MediaRecorder mediaRecorder;
    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    /** Handles playback of all the sound files */
    MediaPlayer mMediaPlayer;

    public static final String TAG = "RecordStatus";

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;


//    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//            .setTimestampsInSnapshotsEnabled(true)
//            .build();
//    firestore.setFirestoreSettings(settings);

    private static final int User_ID = 1;
    private static final int Rec_I = 0;
    private static final String TITLE_KEY = "Title";
    private static final String RECORD_KEY = "Record";
    private static final String DATE_KEY= "Date";
    private static final String DATEFORMAT = "yyyy-MM-dd";

//    Timestamp timestamp = snapshot.getTimestamp("created_at");
//    java.util.Date date = timestamp.toDate();
    String currentDate = GetUTCdatetimeAsString();

    public static Date GetUTCdatetimeAsDate()
    {
        //note: doesn't check for null
        return StringDateToDate(GetUTCdatetimeAsString());
    }
    public static String GetUTCdatetimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }
    public static Date StringDateToDate(String StrDate)
    {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

        try
        {
            dateToReturn = (Date)dateFormat.parse(StrDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dateToReturn;
    }


    public static final int RequestPermissionCode = 1;
    Random random;
    String randomAudioFileName= "ABCDEFGHIJKLMNOP";

    boolean started = false;

    View rootView;
    private ImageView audioIv;
    private ImageView videoIv;
    private EditText titleEt;
    private TextInputLayout editTextLayout;
    private LinearLayout actionLL;
    private ImageView saveIv;

//    /* Firebase Stuff */
//    private StorageReference mStorage;

    private ProgressDialog mProgress;

    // Access a Cloud Firestore instance from your Activity

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRef = db.document("Users/Records");




    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sing, container, false);

//        mStorage = FirebaseStorage.getInstance().getReference();

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        random= new Random();


        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        ///Declaration
        audioIv = (ImageView) rootView.findViewById(R.id.audioImageView);
        videoIv = (ImageView) rootView.findViewById(R.id.videoImageView);

        mProgress = new ProgressDialog(getContext());

        titleEt = (EditText)rootView.findViewById(R.id.titleEditText);




        saveIv= (ImageView)rootView.findViewById(R.id.saveImageView);
        actionLL= (LinearLayout)rootView.findViewById(R.id.actionLayout) ;
        editTextLayout= (TextInputLayout)rootView.findViewById(R.id.title_editText_layout);
//        titleEt.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_save), null);
        titleEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    titleEt.setHint("");
                else
                    titleEt.setHint("Title Your Record");
            }
        });


        audioIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()) {

                    outputFile =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    startRecording();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    videoIv.setEnabled(false);


                    Toast.makeText(getActivity(), "Recording started, Long press to stop recording.",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
            }

         });

        audioIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mediaRecorder.stop();
                videoIv.setEnabled(false);
                audioIv.setEnabled(false);
                titleEt.setVisibility(View.VISIBLE);
                editTextLayout.setVisibility(View.VISIBLE);
                actionLL.setVisibility(View.VISIBLE);
                saveIv.setVisibility(View.VISIBLE);

//                reviewLinearLayout.setVisibility(View.VISIBLE);


                Toast.makeText(getActivity(), "Recording Completed",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });


        saveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord();

            }
        });

        return rootView;
    }

    public void saveRecord()
    {

        String recTitle = titleEt.getText().toString();
        if(recTitle.isEmpty()) {
            Toast.makeText(getContext(), "Please provide a title for your record", Toast.LENGTH_LONG).show();
        }

        mProgress.setMessage("Uploading Your Masterpiece ...");
        mProgress.show();
        setDocument();

//        // Create firebase database
//        Map<String, Object> dataToSave = new HashMap<>();
//        dataToSave.put(TITLE_KEY , recTitle);
//        dataToSave.put(DATE_KEY, currentDate);
//        dataToSave.put(RECORD_KEY, outputFile);
//        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "Record has been saved");
//                mProgress.dismiss();
//                Toast.makeText(getActivity(), "Uploading finished...", Toast.LENGTH_LONG).show();
//                saveIv.setImageResource(R.drawable.ic_action_verified);
//                titleEt.setText("");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Record was not saved", e);
//            }
//        });


///// Add a new document with a generated ID
//       db.collection("Users")
//               .add(dataToSave)
//               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                   @Override
//                   public void onSuccess(DocumentReference documentReference) {
//                       Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                       // Sleep for 200 milliseconds.
//                       try {
//                           Thread.sleep(200);
//                       } catch (InterruptedException e) {
//                           e.printStackTrace();
//                       }
//                       mProgress.dismiss();
//                       Toast.makeText(getActivity(), "Uploading finished...", Toast.LENGTH_LONG).show();
//                       saveIv.setImageResource(R.drawable.ic_action_verified);
//                       titleEt.setText("");
//                   }
//               })
//               .addOnFailureListener(new OnFailureListener() {
//                   @Override
//                   public void onFailure(@NonNull Exception e) {
//                       Log.w(TAG, "Error adding document", e);
//                   }
//               });

    }


    public void setDocument() {
        // [START set_document]
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Mahmoud");
        user.put("age", "33");
        user.put("gender", "male");
        user.put("country", "Lebanon");
        user.put("Email", "mddaher@hotmail.com");
        user.put("Password", "mahmouddaher");
        user.put(DATE_KEY, currentDate);
        subcollectionReference();


        db.collection("users").document("Mahmoud_Daher")
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        // Sleep for 200 milliseconds.
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mProgress.dismiss();
                        Toast.makeText(getActivity(), "Uploading finished...", Toast.LENGTH_LONG).show();
                        saveIv.setImageResource(R.drawable.ic_action_verified);
                        titleEt.setText("");
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        // [END set_document]

        Map<String, Object> data = new HashMap<>();

        // [START set_with_id]
        db.collection("users").document("new-user-id").set(data);
        // [END set_with_id]
    }

    public void subcollectionReference() {
        // [START subcollection_reference]
        DocumentReference recordRef = db
                .collection("users").document("Mahmoud_Daher")
                .collection("Record").document("Record1");
        // [END subcollection_reference]
    }




//    private void uploadRecord() {
//
//        mProgress.setMessage("Uploading Your Masterpiece ...");
//        mProgress.show();
//
//        StorageReference filepath = mStorage.child("Child").child("new_audio.3pg");
//
//        Uri uri = Uri.fromFile(new File(outputFile));
//
//        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                mProgress.dismiss();
//
//                Toast.makeText(getActivity(), "Uploading finished...", Toast.LENGTH_LONG).show();
//                saveIv.setImageResource(R.drawable.ic_action_verified);
//
//
//            }
//        });
//    }


    public void setFieldWithMerge() {
        // [START set_field_with_merge]
        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
//        data.put("Records", true);

        db.collection("Users").document("Records")
                .set(data, SetOptions.merge());
        // [END set_field_with_merge]
    }

    public void startRecording(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(randomAudioFileName.
                       charAt(random.nextInt(randomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getActivity(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(),"Permission Denied",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onStop() {
        super.onStop();

        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}



