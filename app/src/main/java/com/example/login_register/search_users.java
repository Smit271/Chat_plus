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
import java.util.Map;

import com.example.login_register.HelperFunctions;

public class search_users extends AppCompatActivity {

    static String friend_id, user_id, MyEmail;
    static DatabaseReference mref;
    private final String TAG = "Kinetic";
    private RecyclerView list;
    private AutoCompleteTextView txtsearch;
    static ArrayList<dataHandler> listusers;
    static ArrayList<SearchUserInfo> userDetails;
    String isFriend,currentState,extra1,extra2;
    CustomAdapter customarrayAdapter;
    int size;
    DatabaseReference fref,rref;
    int index;
    Map<String,Integer> frequencyMap;


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

        listusers = new ArrayList<dataHandler>();
        userDetails = new ArrayList<SearchUserInfo>();
        customarrayAdapter = new CustomAdapter(this,listusers,userDetails);
        list.setAdapter(customarrayAdapter);

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int temp = userDetails.size();
                userDetails.clear();
                listusers.clear();
                customarrayAdapter.notifyItemRangeRemoved(0,temp);

                Log.d(TAG,"Before Populate Search " + userDetails);
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

            frequencyMap = HelperFunctions.count_freq(names);
            System.out.println("ccccccccccccccccccccccccccccccs" + names);

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
            txtsearch.setAdapter(arrayAdapter);
            txtsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    int temp = userDetails.size();
                    userDetails.clear();
                    listusers.clear();
                    customarrayAdapter.notifyItemRangeRemoved(0,temp);
                    customarrayAdapter.notifyDataSetChanged();

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
        Log.d(TAG,"1 Search User is called!");
        Query query = mref.orderByChild("name").equalTo(name);
        System.out.println("Query :" + query);

        size = HelperFunctions.getIndexFromMap(frequencyMap,name);
        Log.d(TAG,"SIZEEEEEEEEE: "+size);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Log.d(TAG,"2 Query on Data change called");
                    Log.d(TAG,"3 userDetails & listusers is cleraed");
                    int temp = userDetails.size();
                    userDetails.clear();
                    listusers.clear();
                    customarrayAdapter.notifyItemRangeRemoved(0,temp);
                    index = 0;

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.d(TAG,"3 Index: "+index);
                        dataHandler user = new dataHandler(ds.child("user_name").getValue(String.class),
                                ds.child("name").getValue(String.class),
                                ds.child("email").getValue(String.class),
                                ds.child("pass").getValue(String.class),
                                ds.child("hash_id").getValue(String.class));

                        friend_id = ds.getKey();
                        //SearchUserInfo user_details = new SearchUserInfo(user_id, friend_id);

                        // Add start
                        fref = FirebaseDatabase.getInstance().getReference("users").child(ds.getKey()).child("friends").child(user_id);

                        System.out.println("Kinetic: Before on data change: " + ds.getKey() + " " + user_id + " fref:" + fref.toString());


                            fref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    rref = FirebaseDatabase.getInstance().getReference("users").child(ds.getKey()).child("Request").child(user_id);

                                    rref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                            Log.d(TAG,"OnDatachange of "+ ds.getKey() +" for requests called!");

                                            Log.d(TAG,"OnDatachange of "+ ds.getKey() +" for friends called!");

                                            extra1 = snapshot.child("request_type").getValue(String.class);
                                            if (extra1 != null) {

                                                isFriend = (String) snapshot.child("request_type").getValue(String.class);
                                            } else {
                                                isFriend = "not_accepted";
                                            }
                                            System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww" + isFriend);


                                            extra2 = snapshot2.child("request_type").getValue(String.class);

                                            if (extra2 != null) {
                                                currentState = (String) snapshot2.child("request_type").getValue(String.class);
                                            } else {
                                                currentState = "not_received";
                                            }
                                            //System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww" + currentState);

                                            System.out.println("Kinetic: In deep: FriendId= "+ ds.getKey() +" currentstate= "+currentState + "Isfriend"+isFriend);

                                            //
                                            if (index < size){
                                                user.setIndex(index);
                                                listusers.add(user);
                                                userDetails.add(new SearchUserInfo(user_id,ds.getKey(),isFriend,currentState,index));
                                                index+=1;
                                                }
                                            else{

                                                listusers.set(user.getIndex(),user);
                                                userDetails.set(user.getIndex(), new SearchUserInfo(user_id,ds.getKey(),isFriend,currentState,index));
                                            }
                                            customarrayAdapter.notifyItemInserted(index);
                                            Log.d(TAG,index + " USer added: "+ds.getKey());

                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot2) {
//
//
//                        fref.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                rref = FirebaseDatabase.getInstance().getReference("users").child(ds.getKey()).child("Request").child(user_id);
//                                rref.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        extra2 = snapshot.child("request_type").getValue(String.class);
//                                        if (extra2 != null) {
//                                            currentState = (String) snapshot.child("request_type").getValue(String.class);
//                                        } else {
//                                            currentState = "not_received";
//                                        }
//                                        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww" + currentState);
//
//
//                                    }
//
//
//                                }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                        System.out.println("Kinetic: Isfriend "+isFriend);
//
//
//                            //Add end
//
//
//                        userDetails.add(user_details);
//                        //customarrayAdapter.notifyDataSetChanged();
//                        customarrayAdapter.notifyItemInserted(index);
//                        index+=1;
//                        Log.d(TAG,index + " USer added: "+friend_id);
//
//
//
//                    }
//                } else {
//                    Log.d("users", "No data found");
//                }
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
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


