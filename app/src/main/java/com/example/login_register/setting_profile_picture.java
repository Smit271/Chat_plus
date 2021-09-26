package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class setting_profile_picture extends AppCompatActivity {

    ImageView mProfile;
    Button mRegister;
    // Create a Cloud Storage reference from the app
    StorageReference storageReference;
    //get Current user details
    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = current_user.getEmail();
    // Declaring instance of FireStore Database
    FirebaseFirestore fstore;
    HelperFunctions helperFunctions = new HelperFunctions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initilizing Firebase storage instance
        storageReference = FirebaseStorage.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile_picture);

        mProfile = findViewById(R.id.profile_picture);
        mRegister = findViewById(R.id.registerBtn);

        // Code to get user's detail from Firebase FireStore...
//        // Creating instance of FirebaseFireStore
//        fstore = FirebaseFirestore.getInstance();
//
//        DocumentReference docRef = fstore.collection("users").document(uid);
//        Task<DocumentSnapshot> documentSnapshotTask = docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            private static final String TAG = "k";
//
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "User data: " + document.getData());
//                        String email = (String) document.get("email");
//                        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
//
//                    } else {
//                        Log.d(TAG, "No such user");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_profile_picture.this, home.class);
                startActivity(intent);
            }
        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ProgressDialog while setting up profile picture
        ProgressDialog progressDialog = new ProgressDialog(setting_profile_picture.this);
        progressDialog.setMessage("Setting up profile pic");
        if(requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                mProfile.setImageURI(imageUri);
                progressDialog.show();
                uploadImagetoFirebaseStorage(imageUri);
                progressDialog.dismiss();
            }
        }
    }

    public void uploadImagetoFirebaseStorage(Uri imageUri) {
        // uploading image to database
        String email = current_user.getEmail();
        String user_id = helperFunctions.getUseridFromEmail(email);

        StorageReference fileRef = storageReference.child(user_id);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(setting_profile_picture.this, "Profile picture set", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(setting_profile_picture.this, "Failed to set", Toast.LENGTH_LONG).show();
            }
        });
    }

}