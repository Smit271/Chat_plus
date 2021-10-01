package com.chatplus.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    ArrayList<String> myFriendnames = new ArrayList<String>();
    ArrayList<SingleFriend> singleFriends = new ArrayList<SingleFriend>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_listview_of_friends);


        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_in_chat_list);
        TextView textView = (TextView) findViewById(R.id.no_friend_indicator);
        progressBar.setVisibility(View.VISIBLE);


        FloatingActionButton add_usr_btn = (FloatingActionButton) findViewById(R.id.add_user_in_chat_screen);
        add_usr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),search_users.class);
                startActivity(intent);
            }
        });

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
//        actionBar.setLogo(R.mipmap.ic_launcher_round);
//        actionBar.setDisplayUseLogoEnabled(true);
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
                myFriendnames.clear();
                singleFriends.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {

                    String uname = snap.getKey().toString();
                    myFriendUnames.add(uname);

                     FirebaseDatabase.getInstance().getReference("users").child(uname).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()){
                                String name = task.getResult().getValue().toString();
                                myFriendnames.add(name);
                                Log.d(TAG, "Key +++++++ " + name + " " + snap.getKey() + "++++++++++");

                            }
                        }
                    });




                }


                Log.d(TAG, "MyFriendUnames size " + myFriendUnames.size());

                if (myFriendUnames.size() == 0){
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }



                //This is list of objects to of SingleFriend to pass in array adapter
                //Profile Image should be come from database using user id

                for (int i = 0; i < myFriendUnames.size(); i++) {

                    storageReference = FirebaseStorage.getInstance().getReference().child(myFriendUnames.get(i));
                    int finalI = i;
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, myFriendUnames.get(finalI)+": " + uri.toString());
                            singleFriends.add(new SingleFriend(myFriendUnames.get(finalI), myFriendnames.get(finalI),0, null, uri.toString()));
                            singleFriendAdapter.notifyDataSetChanged();
                            Log.d(TAG,myFriendUnames.get(finalI) + " Notified!");

                            if (progressBar.getVisibility() == View.VISIBLE){ progressBar.setVisibility(View.GONE); }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failed to get Image of "+myFriendUnames.get(finalI));
                            singleFriends.add(new SingleFriend(myFriendUnames.get(finalI),myFriendnames.get(finalI), R.drawable.ic_launcher_foreground, null, null));
                            singleFriendAdapter.notifyDataSetChanged();
                            Log.d(TAG,myFriendUnames.get(finalI) + " Notified!");
                            if (progressBar.getVisibility() == View.VISIBLE){ progressBar.setVisibility(View.GONE); }
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
                        Log.d(TAG, "In Put extra my Username: " + MyUserId);
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