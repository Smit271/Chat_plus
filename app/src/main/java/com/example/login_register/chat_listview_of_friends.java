package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class chat_listview_of_friends extends AppCompatActivity {
    private String LoggedUserId = "";
    private static final String TAG = "Kinetic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_listview_of_friends);

        //This is list of my friends which should be fectched from database
        ArrayList<String> myFreindUnames = new ArrayList<String>();
        myFreindUnames.add("smit123");
        myFreindUnames.add("malav");
        myFreindUnames.add("malav16");


        //Set current user in Tool bar
        //get Current user details
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = current_user.getUid();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference ref = fireStore.collection("users").document(Uid);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.getResult().exists()) {
                                                    LoggedUserId = task.getResult().getString("user_name");
                                                    Log.d(TAG,"Usrname in Freidnslist: "+LoggedUserId);
                                                    Log.d(TAG,"Before Actionbar - Usrname: "+LoggedUserId);
                                                    ActionBar actionBar = getSupportActionBar();
                                                    actionBar.setTitle(LoggedUserId);
                                                    actionBar.setDisplayHomeAsUpEnabled(true);
                                                    actionBar.setDisplayShowHomeEnabled(true);
                                                }

                                            }
        });


        //This is list of objects to of SingleFriend to pass in array adapter
        //Profile Image should be come from database using user id
        ArrayList<SingleFriend> singleFriends = new ArrayList<SingleFriend>();
        for (int i = 0; i < myFreindUnames.size(); i++) {
            singleFriends.add(new SingleFriend(myFreindUnames.get(i),R.drawable.ic_launcher_foreground));
        }

        //create adapter
        adapter_listview_of_friends_in_chat singleFriendAdapter = new adapter_listview_of_friends_in_chat(this,singleFriends);

        //find the view where this adapter will throw the list of single friends
        ListView listView = (ListView) findViewById(R.id.listview_friends_chat);

        listView.setAdapter(singleFriendAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent chatIntent = new Intent(chat_listview_of_friends.this,ChatScreen1.class);

                //get item where user clicked
                SingleFriend sFriend = (SingleFriend) adapterView.getItemAtPosition(position);
                //send Username
                chatIntent.putExtra("uname_of_friend",sFriend.getUsername());
                startActivity(chatIntent);
            }
        });

    }
}