package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Register_page extends AppCompatActivity {

    public static final String TAG = null;
    Button mregisterBtn;
    EditText mEmail, mPass, mName, mconfirmPass, mUsername;
    String userId;
    TextView mLogin;
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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


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
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_page.this, login.class );
                startActivity(intent);
            }
        });

        // When user click on register button
        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    registerUser(user_name, name, email, pass);
                    mName.setText("");
                    mPass.setText("");
                    mEmail.setText("");
                    mconfirmPass.setText("");
                    mUsername.setText("");
                }
            }
        });
    }

    private void registerUser(String username, String name, String email, String pass) {

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

                    // Making object of data -- to pass into FireStore Database
                    dataHandler obj = new dataHandler(name, email, pass);
                    // Add data to database of firebase
                    ref.child(username).setValue(obj);
                    // getting current user's unique UID
                    userId = mAuth.getCurrentUser().getUid();
                    // Store user-data in FireStore
                    DocumentReference documentReference = fstore.collection("users").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("user_name", username);
                    user.put("name", name);
                    user.put("email", email);
                    // If user-data stored successfully
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Successfully registered user for "+username);
                        }
                    });

                    // Making toast for successfully registration
                    Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_LONG).show();
                    // After completion redirecting to main screen
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    // Making progressbar invisible after successfully registration
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(getApplicationContext(), ""+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
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