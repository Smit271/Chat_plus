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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class setting_profile_picture extends AppCompatActivity {

    ImageView mProfile;
    Button mRegister;
    ProgressDialog progressDialog;
    // Create a Cloud Storage reference from the app
    StorageReference storageReference;
    //get Current user details
    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = current_user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // ProgressDialog while setting up profile picture
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Setting up profile pic");
        // Initilizing Firebase storage instance
        storageReference = FirebaseStorage.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile_picture);

        mProfile = findViewById(R.id.profile_picture);
        mRegister = findViewById(R.id.registerBtn);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home.class));
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
        if(requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                mProfile.setImageURI(imageUri);
                uploadImagetoFirebaseStorage(imageUri);
            }
        }
    }

    public void uploadImagetoFirebaseStorage(Uri imageUri) {
        progressDialog.show();
        // uploading image to database
        StorageReference fileRef = storageReference.child(uid);
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
        progressDialog.dismiss();
    }

}