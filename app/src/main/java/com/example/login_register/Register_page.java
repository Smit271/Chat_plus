package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register_page extends AppCompatActivity {

    public static final String TAG = null;
    Button mregisterBtn;
    EditText mEmail, mPass, mName, mconfirmPass, mUsername;
    TextView mLogin;
    String userId;
    ProgressBar progressBar;
    // Declaring instance of FireStore Database
    FirebaseFirestore fstore;
    // Declaring Instance of Firebase
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Making object of HelperFunctions
        HelperFunctions helper = new HelperFunctions();

        // Init Register button
        mregisterBtn = findViewById(R.id.registerBtn);
        mName =  findViewById(R.id.name);
        mEmail =  findViewById(R.id.emailId);
        mPass =  findViewById(R.id.passwd);
        mUsername = findViewById(R.id.username);
        mconfirmPass = findViewById(R.id.confirm_passwd);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        mLogin = findViewById(R.id.textview_login);


        // If user click on already have an account login text -- Redirect to login page..
        mLogin.setOnClickListener(view -> {
            Intent intent = new Intent(Register_page.this, login.class);
            startActivity(intent);
        });

        // When user click on register button
        mregisterBtn.setOnClickListener(view -> {
            String email = mEmail.getText().toString().trim();
            String name = StringUtils.capitalize(mName.getText().toString().trim()); // Capitalize first letter of name
            String pass = mPass.getText().toString().trim();
            String user_name = mUsername.getText().toString().trim();
            String cpass = mconfirmPass.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                mEmail.setError("Invalid Error");
                mEmail.setFocusable(true);
            }
            else if (user_name.isEmpty()){
                mName.setError("Enter UserName");
                mName.setFocusable(true);
            }
            else if (name.isEmpty()){
                mName.setError("Enter Name");
                mName.setFocusable(true);
            }
            else if (!pass.equals(cpass)) {
                mconfirmPass.setError("Enter same as above");
            }
            else if (pass.length() < 6) {
                mPass.setError("Password length at least 6 characters");
                mPass.setFocusable(true);
            }
            else {
                String user_id = helper.getUseridFromEmail(email);
                registerUser(user_id, user_name, name, email, pass);
                mName.setText("");
                mPass.setText("");
                mEmail.setText("");
                mconfirmPass.setText("");
                mUsername.setText("");
            }
        });
    }
    private void registerUser(String user_id, String username, String name, String email, String pass) {

        progressBar.setVisibility(View.VISIBLE);

        // Database initialization
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("users"); //table name

        // Authentication initialization
        mAuth = FirebaseAuth.getInstance();

        // Creating instance of FirebaseFireStore
        fstore = FirebaseFirestore.getInstance();

        // Creating user with email and password -- with the help of Firebase authentication
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    // Updating user's display name
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    current_user.updateProfile(profileUpdates);

                    // getting current user's unique UID
                    userId = current_user.getUid();
                    // Making object of data -- to pass into FireStore Database
                    dataHandler obj = new dataHandler(username, name, email, pass, userId);
                    // Add data to database of firebase
                    ref.child(user_id).setValue(obj);
                    // Store user-data in FireStore
                    DocumentReference documentReference = fstore.collection("users").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("user_name", username);
                    user.put("name", name);
                    user.put("email", email);
                    user.put("userid", user_id);
                    // If user-data stored successfully
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Successfully registered user for "+username);
                        }
                    });

                    // Making toast for successfully registration
                    Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_LONG).show();

                    // Making progressbar invisible after successfully registration
                    progressBar.setVisibility(View.GONE);
                    // After completion redirecting to profile screen
                    Intent intent = new Intent(Register_page.this, setting_profile_picture.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), ""+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    // After completion redirecting to main screen
                    Intent intent = new Intent(Register_page.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}