package com.example.login_register;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

//        //get hashid of user from auth
//        String MyHashId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Log.d(TAG, "OnAuth: MyHashId: " + MyHashId);
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
//                    Log.d(TAG, "OnComplete chat screen: MyUserId: " + MyUserId);
//                }
//                else{
//                    MyUserId = "Failed";
//                }
//
//            }
//        });

        //get username of friend  which sent along with intent using putExtra()
        MyFriendUserId = getIntent().getExtras().getSerializable("uname_of_friend").toString();


        //set username of friend at Top in curent layout
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(MyFriendUserId);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }


//        Log.d(TAG, "On create chat screen: MyUserId: " + MyUserId);
//        Log.d(TAG, "On create chat screen: MyFrinedId: " + MyFriendUserId);
        TextView textView = (TextView)findViewById(R.id.friend_username_main_chat_screen);
        textView.setText(MyFriendUserId);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FloatingActionButton fltBtn = (FloatingActionButton) findViewById(R.id.btn_message_send);
        EditText user_message = (EditText) findViewById(R.id.message_edit_text);
        fltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChatMessage userChat = new ChatMessage(user_message.getText().toString(),
                        MyUserId,MyFriendUserId);

                String keyForTwoUsers = helper.generateKeyFromTwoKeys(MyUserId,MyFriendUserId);
                Task updateTask = mDatabase.child("chats").child(keyForTwoUsers).push().setValue(userChat);
//                if (updateTask.isSuccessful()){
//                    Log.d(TAG, "Data inserted in DATABASE!");
//                }
                Log.d(TAG, "On Click float button: MyUserId: " + MyUserId);
                Log.d(TAG, "On Click float button: CombinedId: " + keyForTwoUsers);

                //FirebaseDatabase.getInstance().getReference().push().setValue(userChat);
                //Log.d(TAG, "User id: " + ChatScreen1.this.MyUserId);
                //Log.d(TAG, "User name: " + MyName);
                //Toast.makeText(getApplicationContext(), "Logged In as " + MyName, Toast.LENGTH_SHORT).show();

                user_message.setText("");

            }
        });

        displayChatMessages();
        }


      public void displayChatMessages(){
          Log.d(TAG, "************ Display message called ************\n"+MyFriendUserId);

          ListView listOfMessages = (ListView) findViewById(R.id.listview_messages);
          Log.d(TAG, "List view created 2");

          String keyForTwoUsers = helper.generateKeyFromTwoKeys(MyUserId,MyFriendUserId);
          Log.d(TAG, "Combined Key:"+keyForTwoUsers);

        AdapterMessage adapterMessage = new AdapterMessage(ChatScreen1.this,ChatMessage.class,R.layout.message,
                FirebaseDatabase.getInstance().getReference("chats").child(keyForTwoUsers));
          Log.d(TAG, "adapter created 3");

        listOfMessages.setAdapter(adapterMessage);
          Log.d(TAG, "adapter set 4");
    }

    public String getLoggedinUserId(){ return MyUserId; }

    public  String getMyFriendUserId(){
        return MyFriendUserId;
    }


//    public void displayChatMessages(){
//        ListView ListOfMessages = (ListView) findViewById(R.id.listview_messaged);
//
//        FirebaseListAdapter fire_adapter = new FirebaseListAdapter<ChatMessage>(ChatScreen1.this,ChatMessage.class,R.layout.message, FirebaseDatabase.getInstance().getReference()) {
//            @Override
//            protected void populateView(View v, ChatMessage model, int position) {
//
//                TextView messageText = (TextView) v.findViewById(R.id.messageText);
//
//                messageText.setText(model.getMessageText());
//
//            }
//        };
//
//        ListOfMessages.setAdapter(fire_adapter);
//    }
}