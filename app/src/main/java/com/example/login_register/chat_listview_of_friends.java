package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class chat_listview_of_friends extends AppCompatActivity {

    private String MyUserId = "";
    private static final String TAG = "Kinetic";
    ListView friendListView;
    StorageReference storageReference;
    Bitmap bitmap;
    ArrayList<String> myFriendUnames = new ArrayList<String>();
    ArrayList<SingleFriend> singleFriends = new ArrayList<SingleFriend>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_listview_of_friends);

        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();

        HelperFunctions helper = new HelperFunctions();

        //set adapter
        friendListView = (ListView) findViewById(R.id.listview_friends_chat);
        adapter_listview_of_friends_in_chat singleFriendAdapter = new adapter_listview_of_friends_in_chat(chat_listview_of_friends.this, singleFriends);

        //find the view where this adapter will throw the list of single friends
        friendListView.setAdapter(singleFriendAdapter);


        //fetch current User Id
        String MyEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        MyUserId = helper.getUseridFromEmail(MyEmail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(MyUserId);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        //fetch my friends only
        Log.d(TAG, "@@@ before on data change @@@");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(MyUserId).child("friends");
        usersRef.keepSynced(true);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myFriendUnames.clear();
                singleFriends.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    myFriendUnames.add(snap.getKey().toString());
                    Log.d(TAG, "Key +++++++ " + snap.getKey() + "++++++++++");

                    //here I am adding items to list adapter
                }


                Log.d(TAG, "MyFriendUnames size " + myFriendUnames.size());

                //This is list of objects to of SingleFriend to pass in array adapter
                //Profile Image should be come from database using user id

                for (int i = 0; i < myFriendUnames.size(); i++) {

                    storageReference = FirebaseStorage.getInstance().getReference().child(myFriendUnames.get(i));
                    int finalI = i;
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, myFriendUnames.get(finalI)+": " + uri.toString());
//                                ImageView fprofile_image = (ImageView) findViewById(R.id.test_image);
                            //Picasso.get().load(uri.toString()).into(fprofile_image);

                            singleFriends.add(new SingleFriend(myFriendUnames.get(finalI), 0, null, uri.toString()));

                            singleFriendAdapter.notifyDataSetChanged();

                            Log.d(TAG,finalI+" Just Before if......");
//                                if (finalI == (myFriendUnames.size()-1)){
//                                    addListAdapter();
//                                }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failed to get Image of "+myFriendUnames.get(finalI));
                            singleFriends.add(new SingleFriend(myFriendUnames.get(finalI), R.drawable.ic_launcher_foreground, null, null));
                            singleFriendAdapter.notifyDataSetChanged();

//                                if (finalI == (myFriendUnames.size()-1)){
//                                    addListAdapter();
//                                }
                        }
                    });
                }

                friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent chatIntent = new Intent(chat_listview_of_friends.this, ChatScreen1.class);

                        //get item where user clicked
                        SingleFriend sFriend = (SingleFriend) adapterView.getItemAtPosition(position);
                        //send Username
                        chatIntent.putExtra("uname_of_friend", sFriend.getUsername());
                        chatIntent.putExtra("uname_of_mine", MyUserId);
                        chatIntent.putExtra("profile_image",sFriend.getUri());
                        Log.d(TAG, "In Putextra my Usrname: " + MyUserId);
                        startActivity(chatIntent);

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d(TAG, "@@@ After on data change @@@");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(chat_listview_of_friends.this, home.class));
        finish();
    }

    public void addListAdapter() {

        Log.d(TAG, "In list adapter, singlefriends size: " + singleFriends.size());
        adapter_listview_of_friends_in_chat singleFriendAdapter = new adapter_listview_of_friends_in_chat(chat_listview_of_friends.this, singleFriends);

        //find the view where this adapter will throw the list of single friends
        friendListView.setAdapter(singleFriendAdapter);

        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent chatIntent = new Intent(chat_listview_of_friends.this, ChatScreen1.class);

                //get item where user clicked
                SingleFriend sFriend = (SingleFriend) adapterView.getItemAtPosition(position);
                //send Username
                chatIntent.putExtra("uname_of_friend", sFriend.getUsername());
                chatIntent.putExtra("uname_of_mine", MyUserId);
                Log.d(TAG, "In Putextra my Usrname: " + MyUserId);
                startActivity(chatIntent);

            }
        });

    }
}