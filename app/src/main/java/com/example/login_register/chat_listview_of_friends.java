package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_listview_of_friends);

        HelperFunctions helper = new HelperFunctions();


        //fetch current User Id
        String MyEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        MyUserId = helper.getUseridFromEmail(MyEmail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(MyUserId);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //set toolbar
        //get hashid of user from auth
//        String MyHashId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        //get reference of firestore database
//        DocumentReference fire_store_ref = FirebaseFirestore.getInstance().collection("users").document(MyHashId);
//
//        //fire a query to find user_name storded in firestore database
//        fire_store_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    MyUserId = task.getResult().getString("user_name");
//                    Log.d(TAG,"Before Actionbar - Usrname: "+MyUserId);

//                }
//                else{
//                    MyUserId = "Failed";
//                }
//
//            }
//        });

        //This is list of my friends which should be fectched from database
        //ArrayList<String> myFriendUnames = new ArrayList<String>();

        //fetch my friends only
        Log.d(TAG,"@@@ before on data change @@@");
        DatabaseReference usersRef =  FirebaseDatabase.getInstance().getReference("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myFriendUnames.clear();
                for (DataSnapshot snap: snapshot.getChildren()){
                    myFriendUnames.add(snap.getKey().toString());
                    Log.d(TAG,"Key +++++++ "+snap.getKey() + "++++++++++");
                }





                Log.d(TAG,"MyFriendUnames size "+ myFriendUnames.size());
                //This is list of objects to of SingleFriend to pass in array adapter
                //Profile Image should be come from database using user id
                ArrayList<SingleFriend> singleFriends = new ArrayList<SingleFriend>();
                for (int i = 0; i < myFriendUnames.size(); i++) {

                    try {
                        storageReference = FirebaseStorage.getInstance().getReference().child(myFriendUnames.get(i));
                        Log.d(TAG,"Storage Reference: "+storageReference.getName());
                        final File localfile = File.createTempFile("lance","jpeg");
                        Log.d(TAG,"Local File: "+ localfile.getName());
                        int finalI = i;
                        int finalSize = myFriendUnames.size();
                        storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(chat_listview_of_friends.this,"Picture Retrived",Toast.LENGTH_SHORT);
                                bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());

                                //((ImageView) findViewById(R.id.test_image)).setImageBitmap(bitmap);
                                Log.d(TAG,"bitmap retrived "+bitmap.toString());
                                singleFriends.add(new SingleFriend(myFriendUnames.get(finalI),R.drawable.ic_launcher_foreground,bitmap));
                                Log.d(TAG,"Fetch: ////////// "+ myFriendUnames.get(finalI) +" ////////////");

                            if (finalI == finalSize-1){
                                //create adapter
                                Log.d(TAG,"In list adapter, singlefriends size: "+singleFriends.size());
                                adapter_listview_of_friends_in_chat singleFriendAdapter = new adapter_listview_of_friends_in_chat(chat_listview_of_friends.this,singleFriends);

                                //find the view where this adapter will throw the list of single friends


                                friendListView.setAdapter(singleFriendAdapter);



                            }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(chat_listview_of_friends.this,"Picture can not retrived!",Toast.LENGTH_SHORT);
                            }
                        });

                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                }

                friendListView = (ListView) findViewById(R.id.listview_friends_chat);
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



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        myFriendUnames.add("Smit23");
//        myFriendUnames.add("karm261");
//        myFriendUnames.add("malav16");
//        myFriendUnames.add("malav");
       // myFriendUnames.add("karm123");


        Log.d(TAG,"@@@ After on data change @@@");

    }
}