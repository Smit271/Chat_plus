package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import com.example.login_register.HelperFunctions;

public class search_users extends AppCompatActivity {

    static String friend_id, user_id, MyEmail;
    static String currentState, isFriend;
    static Boolean a = false;


    static DatabaseReference mref;

    private RecyclerView list;
    private AutoCompleteTextView txtsearch;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mref = FirebaseDatabase.getInstance().getReference("users");

        String MyEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        user_id = HelperFunctions.getUseridFromEmail(MyEmail);
//        System.out.println("MY ID ((((((((((((((((((((((((((((((((((((((((((( :" + user_id);

        list = (RecyclerView) findViewById(R.id.list);
        txtsearch = (AutoCompleteTextView) findViewById(R.id.txtsearch);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

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
        if (snapshot.exists()) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                String name = ds.child("name").getValue(String.class);
                names.add(name);
            }
            System.out.println("ccccccccccccccccccccccccccccccs" + names);

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
            txtsearch.setAdapter(arrayAdapter);
            txtsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    System.out.println("FFFFFFFFFF--------------   " + position);
                    String selection = adapterView.getItemAtPosition(position).toString();
                    searchUser(selection);
                    Log.d("CHeck------------------------", selection);

                }
            });
        } else {
            Log.d("users", "No data Found");
        }
    }

    private void searchUser(String name) {
        Query query = mref.orderByChild("name").equalTo(name);
        System.out.println("Query :" + query);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<dataHandler> listusers = new ArrayList<>();
                ArrayList<Search_user_data> userDetails = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        dataHandler user = new dataHandler(ds.child("user_name").getValue(String.class),
                                ds.child("name").getValue(String.class),
                                ds.child("email").getValue(String.class),
                                ds.child("pass").getValue(String.class),
                                ds.child("hash_id").getValue(String.class));
                        listusers.add(user);
                        friend_id = ds.getKey();
                        Search_user_data user_details = new Search_user_data(user_id, friend_id);
                        userDetails.add(user_details);
                        a = true;
                    }
                } else {
                    a = true;
                    Log.d("users", "No data found");
                }
                if (a) {
                    CustomAdapter arrayAdapter = new CustomAdapter(listusers, userDetails);
                    list.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(getApplicationContext(), chat_listview_of_friends.class));
        finish();
    }
    
}


