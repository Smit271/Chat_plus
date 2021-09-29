package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
        friendListView.setAdapter(singleFriendAdapter);


        //fetch current User Id
        String MyEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        MyUserId = helper.getUseridFromEmail(MyEmail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(MyUserId);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);


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
                            singleFriends.add(new SingleFriend(myFriendUnames.get(finalI), 0, null, uri.toString()));
                            singleFriendAdapter.notifyDataSetChanged();
                            Log.d(TAG,myFriendUnames.get(finalI) + " Notified!");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failed to get Image of "+myFriendUnames.get(finalI));
                            singleFriends.add(new SingleFriend(myFriendUnames.get(finalI), R.drawable.ic_launcher_foreground, null, null));
                            singleFriendAdapter.notifyDataSetChanged();
                            Log.d(TAG,myFriendUnames.get(finalI) + " Notified!");
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

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return super.onSupportNavigateUp();
//    }
//    @Override
//    public void onBackPressed()
//    {
//        startActivity(new Intent(chat_listview_of_friends.this, home.class));
//        finish();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate your main_menu into the menu
        getMenuInflater().inflate(R.menu.menu, menu);

        // Find the menuItem to add your SubMenu
        MenuItem myMenuItem = menu.findItem(R.id.empty);

        // Inflating the sub_menu menu this way, will add its menu items
        // to the empty SubMenu you created in the xml
        getMenuInflater().inflate(R.menu.sub_menu, myMenuItem.getSubMenu());

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        HelperFunctions helper = new HelperFunctions();

        //change intent based on item pressed
        helper.doActiononClickActionBtn(getApplicationContext(),id);

        return super.onOptionsItemSelected(item);
    }

}