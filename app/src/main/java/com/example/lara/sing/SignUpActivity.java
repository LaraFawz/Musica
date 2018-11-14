package com.example.lara.sing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_PASSWORD = "Password";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.document("Users/UserOne");


    private ImageView nextImageView;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();


        usernameEditText = (EditText) findViewById(R.id.username_EditText);
//        String email = usernameEditText.getText().toString().trim();
        String email = docRef.get().toString();


        passwordEditText = (EditText) findViewById(R.id.password_EditText);
        confirmEditText = (EditText) findViewById(R.id.confirmpassword_EditText);


        nextImageView = (ImageView) findViewById(R.id.next_ImageView);

        Intent intent = new Intent();
        intent.putExtra(KEY_EMAIL, email);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_ImageView:
                registerUser();
                break;
        }
    }


    public void registerUser() {
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPass = confirmEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            usernameEditText.setError("Email is required");
            usernameEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameEditText.setError("Please enter a valid email");
            usernameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password is too short. 6 characters minimum..");
            passwordEditText.requestFocus();
            return;

        }


        if (TextUtils.isEmpty(confirmPass)) {
            confirmEditText.setError("Please Confirm Your Password");
            passwordEditText.requestFocus();
            return;
        }

        if (!confirmPass.matches(password)){
            confirmEditText.setError("Does Not Match");
            confirmEditText.requestFocus();
            return;

        }

        showProgressDialog();

        //create a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            finish();
                            startActivity(new Intent(SignUpActivity.this, SignUpProfileActivity.class));
                            saveSignIn();
                        }
                    }
                });

    }

    public void saveSignIn()
    {

        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Map<String, String> signIn = new HashMap<>();
        signIn.put(KEY_EMAIL, email);
        signIn.put(KEY_PASSWORD, password);



        db.collection("Users").document().collection("Info").add(signIn)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });

//        db.collection("Users").add(signIn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Error adding document", e);
//            }
//        });


//        db.collection("Users").document("UserOne").collection("Info").document("Login Info").set(signIn)
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
//                Toast.makeText(SignUpActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, e.toString());
//
//            }
//        });

    }

}
