package com.chatplus.login_register;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatScreen1 extends AppCompatActivity {


    HelperFunctions helper = new HelperFunctions();

    private String MyUserId = "";
    private String MyFriendUserId = "";
    private String MyName = "";

    private static final String TAG = "Kinetic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen1);

        MyUserId = getIntent().getExtras().getString("uname_of_mine");

        //get username of friend  which sent along with intent using putExtra()
        MyFriendUserId = getIntent().getExtras().getSerializable("uname_of_friend").toString();


        //set username of friend at Top in curent layout
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(MyFriendUserId);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        //Display Messages
        String keyForTwoUsers = helper.generateKeyFromTwoKeys(MyUserId, MyFriendUserId);
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("users").child(MyUserId);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "@@@@@@@@ OnDataChange called @@@@@@");
                displayChatMessages(MyUserId, MyFriendUserId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "@@@@@@@@ OnCancelled called @@@@@@");
            }
        });


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        FloatingActionButton fltBtn = (FloatingActionButton) findViewById(R.id.btn_message_send);
        EditText user_message = (EditText) findViewById(R.id.message_edit_text);
        fltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(user_message.getText().toString().trim().equals(""))) {
                    ChatMessage userChat = new ChatMessage(user_message.getText().toString(),
                            MyUserId, MyFriendUserId);

                    //String keyForTwoUsers = helper.generateKeyFromTwoKeys(MyUserId, MyFriendUserId);
                    Task updateTask = mDatabase.child(keyForTwoUsers).push().setValue(userChat);

                    Log.d(TAG, "On Click float button: MyUserId: " + MyUserId);
                    Log.d(TAG, "On Click float button: CombinedId: " + keyForTwoUsers);
                    user_message.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a Message", Toast.LENGTH_SHORT);
                }

            }
        });


    }


//      public void displayChatMessages(){
//          Log.d(TAG, "************ Display message called ************\n"+MyFriendUserId);
//
//          ListView listOfMessages = (ListView) findViewById(R.id.listview_messages);
//          Log.d(TAG, "List view created 2");
//
//          String keyForTwoUsers = helper.generateKeyFromTwoKeys(MyUserId,MyFriendUserId);
//          Log.d(TAG, "Combined Key:"+keyForTwoUsers);
//
//        AdapterMessage adapterMessage = new AdapterMessage(ChatScreen1.this,ChatMessage.class,R.layout.message_received,
//                FirebaseDatabase.getInstance().getReference("chats").child(keyForTwoUsers));
//          Log.d(TAG, "adapter created 3");
//
//        listOfMessages.setAdapter(adapterMessage);
//          Log.d(TAG, "adapter set 4");
//    }

    public String getLoggedinUserId() {
        return MyUserId;
    }

    public String getMyFriendUserId() {
        return MyFriendUserId;
    }


    public void displayChatMessages(String myUserId, String myFriendId) {

        ArrayList<ChatMessage> chats = new ArrayList<ChatMessage>();
        String combinedKey = helper.generateKeyFromTwoKeys(myUserId, myFriendId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chats").child(combinedKey);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chat = snapshot.getValue(ChatMessage.class);
                    Log.d(TAG, "?????????? " + chat.getMessageText() + " ?????????");
                    chats.add(chat);
                }


                for (ChatMessage c : chats) {
                    Log.d(TAG, "--------- " + c.getMessageText() + " -------------");
                }

                ChatListAdapter messageAdapter = new ChatListAdapter(ChatScreen1.this, chats);
                ListView listView = (ListView) findViewById(R.id.listview_messages);
                listView.setAdapter(messageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(ChatScreen1.this, chat_listview_of_friends.class));
        finish();
    }
}

