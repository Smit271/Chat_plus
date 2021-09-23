package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class login extends AppCompatActivity {

    Button mLoginbtn;
    EditText mEmail, mPass;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login to Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mLoginbtn = findViewById(R.id.loginBtn);
        mEmail = findViewById(R.id.email);
        mPass = findViewById(R.id.passwd);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String passwd = mPass.getText().toString().trim();
                login_user(email, passwd);
            }

            private void login_user(String email, String passwd) {
                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // Getting current user details
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            DocumentReference ref;
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            ref = firestore.collection("users").document(userId);
                            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                    if(task.getResult().exists()){
                                        String user_name = task.getResult().getString("user_name");
                                        String name = task.getResult().getString("name");
                                        String email = task.getResult().getString("email");

                                        Toast.makeText(getApplicationContext(), "Logged In as " + user_name, Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Intent intent = new Intent(login.this, Register_page.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            Intent intent = new Intent(login.this, Register_page.class);
                            Toast.makeText(getApplicationContext(), ""+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            startActivity(intent);
                        }
                    }
                });
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}