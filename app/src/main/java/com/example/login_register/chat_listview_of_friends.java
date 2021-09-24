package com.example.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class chat_listview_of_friends extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_listview_of_friends);

        //This is list of my friends which should be fectched from database
        ArrayList<String> myFreindUnames = new ArrayList<String>();
        myFreindUnames.add("smit123");
        myFreindUnames.add("malav");
        myFreindUnames.add("malav16");

        //This is list of objects to of SingleFriend to pass in array adapter
        //Profile Image should be come from database using user id
        ArrayList<SingleFriend> singleFriends = new ArrayList<SingleFriend>();
        for (int i = 0; i < myFreindUnames.size(); i++) {
            singleFriends.add(new SingleFriend(myFreindUnames.get(i),R.drawable.ic_google));
        }

        //create adapter
        adapter_listview_of_friends_in_chat singleFriendAdapter = new adapter_listview_of_friends_in_chat(this,singleFriends);

        //find the view where this adapter will throw the list of single friends
        ListView listView = (ListView) findViewById(R.id.listview_friends_chat);

        listView.setAdapter(singleFriendAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent chatIntent = new Intent(chat_listview_of_friends.this,ChatScreen1.class);
                startActivity(chatIntent);
            }
        });

    }
}