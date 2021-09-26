package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class search_users extends AppCompatActivity {


    DatabaseReference mref;
    private ListView listdata;
    private AutoCompleteTextView txtsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        listdata=(ListView) findViewById(R.id.list);
        txtsearch=(AutoCompleteTextView) findViewById(R.id.txtsearch);
        mref = FirebaseDatabase.getInstance().getReference("users");

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mref.addListenerForSingleValueEvent(event);
    }

    private void populateSearch(DataSnapshot snapshot) {
        ArrayList<String> names = new ArrayList<>();
        if(snapshot.exists()){
            for (DataSnapshot ds:snapshot.getChildren()){
                String name=ds.child("name").getValue(String.class);
                names.add(name);
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,names);
            txtsearch .setAdapter(arrayAdapter);
            txtsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String name = txtsearch.getText().toString();
                    searchUser(name);
                }
            });

        }else{
            Log.d("users","No data Found" );
        }
    }

    private void searchUser(String name) {
        Query query = mref.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ArrayList<String> listusers = new ArrayList<>();
                    for (DataSnapshot ds:snapshot.getChildren()){
                        dataHandler user = new dataHandler(ds.child("user_name").getValue(String.class),
                                ds.child("name").getValue(String.class),
                                ds.child("email").getValue(String.class),
                                ds.child("pass").getValue(String.class),
                                ds.child("hash_id").getValue(String.class));
                        listusers.add(user.getUsername() + "\n"+ user.getName() + "\n" + user.getEmail());
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,listusers);
                    listdata.setAdapter(arrayAdapter);
                }else{
                    Log.d("users","No data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}