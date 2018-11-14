package com.example.lara.sing;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

import static com.example.lara.sing.R.id.textView_displayDate;

public class SignUpProfileActivity extends BaseActivity implements View.OnClickListener {


    public static final String TAG = "Status";

    public static final String KEY_FIRSTNAME = "FirstName";
    public static final String KEY_LASTNAME = "LastName";
    public static final String KEY_DOB = "DOB";
    public static final String KEY_GENDER = "Gender";
    public static final String KEY_COUNTRY = "Country";
    public static final String KEY_PROFILE = "Profile Picture";

    //Firebase Stuff
    FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.document("Users/UserOne");



    //Profile Pic Stuff
    private static final int CHOOSE_IMAGE = 101;
    String profileImageUrl;
    Uri uriProfileImage;

    //Stuff for DatePicker
    private TextView displayDateTextView;
    private TextView uploadTV;
    ImageButton calenderImageView;
    private int mYear, mMonth, mDay;

    //Layout basic features
    private ImageView doneImageView;
    private ImageView profilePicImageView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private ProgressDialog mProgress;
    private TextView datePickerTextView;
    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_profile);

        mAuth = FirebaseAuth.getInstance();

        firstNameEditText = (EditText) findViewById(R.id.firstName_EditText);
        lastNameEditText = (EditText) findViewById(R.id.lastName_EditText);
        profilePicImageView = (ImageView) findViewById(R.id.profilepic_imageView);
        doneImageView = (ImageView) findViewById(R.id.done_ImageView);
        displayDateTextView = (TextView) findViewById(textView_displayDate);
        calenderImageView = (ImageButton) findViewById(R.id.imageButton_calendar);
        uploadTV = (TextView) findViewById(R.id.uploadTextView);
        radioGroup = (RadioGroup) findViewById(R.id.radio);

        mProgress = new ProgressDialog(SignUpProfileActivity.this);

        doneImageView.setEnabled(true);
        Spinner spinner = (Spinner) findViewById(R.id.countries_spinner);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        loadUserInformation();
    }


    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profilePicImageView);
//                doneImageView.setEnabled(true);
            } else {
//                doneImageView.setEnabled(false);
                Toast.makeText(this, "Upload a picture please", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // To choose the user's dob
    public void showDialogOnButtonClick() {


        final Calendar cal = Calendar.getInstance();

        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MaterialDatePickerTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        DecimalFormat mFormat = new DecimalFormat("00");
                        String Dates = mFormat.format(Double.valueOf(monthOfYear + 1)) + "/ " +
                                mFormat.format(Double.valueOf(dayOfMonth)) + "/ " +
                                mFormat.format(Double.valueOf(year));
                        displayDateTextView.setText(Dates);

                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }


    private boolean validation() {

        boolean validation = true;

        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String date = displayDateTextView.getText().toString().trim();


        if (firstName.isEmpty()) {
            firstNameEditText.setError("First Name required");
            firstNameEditText.requestFocus();
            validation = false;
        }

        if (lastName.isEmpty()) {
            lastNameEditText.setError("Last Name required");
            lastNameEditText.requestFocus();
            validation = false;
        }

        if (date.isEmpty()) {
            displayDateTextView.setError("DOB is required");
            validation = false;
        } else {
            displayDateTextView.setError(null);
        }

//        TextView errorText = (TextView) spinner.getSelectedView();
//        errorText.setError("");
//        errorText.setTextColor(Color.RED);//just to highlight that this is an error
//        errorText.setText("Empty Field");//changes the selected item text to this
//        validation = false;



//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                ((TextView) view).setTextColor(getResources().getColor(R.color.colorPrimary)); //Change selected text color
////                if(position == 0)
////                {
////                    Toast.makeText(SignUpProfileActivity.this, "Please select your country", Toast.LENGTH_SHORT).show();
////                }
//                TextView errorText = (TextView)spinner.getSelectedView();
//                errorText.setError("");
//                errorText.setTextColor(Color.RED);//just to highlight that this is an error
//                errorText.setText("My actual error text");//changes the selected item text to this
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(SignUpProfileActivity.this, "Please select your country", Toast.LENGTH_SHORT).show();
//            }
//        });


        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(firstName)
                    .setDisplayName(lastName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                hideProgressDialog();
                                doneImageView.setEnabled(true);
                                Toast.makeText(SignUpProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
//                    .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    doneImageView.setEnabled(false);
//                    Toast.makeText(SignUpProfileActivity.this, "Upload a picture for you..", Toast.LENGTH_SHORT).show();
//                }
//            });

//            validation = false;
//            Toast.makeText(this, "Upload a picture for you..", Toast.LENGTH_SHORT).show();

        }


//
////        String text = spinner.getSelectedItem().toString();
//        String s = valueOf(spinner.getSelectedItemPosition());
//        if(firstName.isEmpty() || lastName.isEmpty() || s.equals("Choose Your Country")
//                || date.isEmpty() || profileImageUrl ==null)
//        {
//            validation = false;
//            Toast.makeText(SignUpProfileActivity.this, "Check Empty Fields", Toast.LENGTH_SHORT).show();
//        }
//
//        doneImageView.setEnabled(true);
        return validation;
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                profilePicImageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilePics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            showProgressDialog();
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            hideProgressDialog();
                            uploadTV.setVisibility(View.GONE);
                            profileImageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            Picasso.with(getApplicationContext()).load(uriProfileImage).fit().centerCrop()
                                    .into(profilePicImageView);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressDialog();
                            Toast.makeText(SignUpProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profilepic_imageView:
                showImageChooser();
                break;

            case R.id.done_ImageView:
                if(validation() == false) {
                    Toast.makeText(SignUpProfileActivity.this, "Check Empty Fields", Toast.LENGTH_SHORT).show();
                } else if ( profileImageUrl == null){
                    Toast.makeText(SignUpProfileActivity.this, "Upload a photo for you", Toast.LENGTH_SHORT).show();

            } else{
                    doneImageView.setEnabled(true);
                    finish();
                    startActivity(new Intent(SignUpProfileActivity.this, NavigatorActivity.class));
                      saveSignUp();

                }

                break;

            case R.id.imageButton_calendar:
                showDialogOnButtonClick();
                break;

        }
    }

//
//    public void saveSignUp()
//    {
//
//        String firstName = firstNameEditText.getText().toString().trim();
//        String lastName = lastNameEditText.getText().toString().trim();
//        String date = displayDateTextView.getText().toString();
//        String country = getCountry();
//
//
//        // get selected radio button from radioGroup
//        int selectedId = radioGroup.getCheckedRadioButtonId();
//
//        // find the radioButton by returned id
//        radioButton = (RadioButton) findViewById(selectedId);
//
//        String gender = radioButton.getText().toString();
//
//
//        Map<String, String> signUp = new HashMap<>();
//        signUp.put(KEY_FIRSTNAME, firstName);
//        signUp.put(KEY_LASTNAME, lastName);
//        signUp.put(KEY_DOB, date);
//        signUp.put(KEY_COUNTRY, country);
//        signUp.put(KEY_GENDER, gender);
//
//        docRef.collection("Info").document("Profile Info").set(signUp)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
////                        Toast.makeText(SignUpActivity.this, "SignIn saved", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "Info Saved");
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(SignUpProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, e.toString());
//
//            }
//        });
//
//
//
//    }

  /* public String getCountry() {

        String selectedItemText = "";
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text
//            Toast.makeText
//                    (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                    .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return selectedItemText;
    }
    */


    public void saveSignUp() {

        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
//        String date = displayDateTextView.getText().toString();
//        String country = getCountry().toString();

//
////         get selected radio button from radioGroup
//        int selectedId = radioGroup.getCheckedRadioButtonId();
//
////         find the radioButton by returned id
//        radioButton = (RadioButton) findViewById(selectedId);
//
//        String gender = radioButton.getText().toString();

//        // [START set_document]
//        Map<String, Object> user = new HashMap<>();
//        user.put(KEY_FIRSTNAME, firstName);
//        user.put(KEY_LASTNAME, lastName);
//        user.put(KEY_DOB, date);
//        user.put(KEY_COUNTRY, country);
//        user.put(KEY_GENDER, gender);


        User user = new User(firstName, lastName);
        db.collection("Users").getParent().collection("Info").add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        // Sleep for 200 milliseconds.
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mProgress.dismiss();
                        Toast.makeText(SignUpProfileActivity.this, "Uploading finished...", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });


     /*   docRef.collection("Info").document("ProfileInfo")
                .set(user, SetOptions.merge())
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
                        Toast.makeText(SignUpProfileActivity.this, "Uploading finished...", Toast.LENGTH_LONG).show();

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

       */





//        Map<String, Object> data = new HashMap<>();
//
//        // [START set_with_id]
//        db.collection("users").document("new-user-id").set(data);
//        // [END set_with_id]
    }

//

}
