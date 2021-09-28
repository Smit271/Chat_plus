package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
    static String currentState;

    static DatabaseReference mref;
    FirebaseAuth mAuth;

    private RecyclerView list;
    private AutoCompleteTextView txtsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        mAuth = FirebaseAuth.getInstance();
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

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
            txtsearch.setAdapter(arrayAdapter);
            txtsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String selection = adapterView.getItemAtPosition(position).toString();
                    searchUser(selection);

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
                if (snapshot.exists()) {
                    ArrayList<dataHandler> listusers = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        dataHandler user = new dataHandler(ds.child("user_name").getValue(String.class),
                                ds.child("name").getValue(String.class),
                                ds.child("email").getValue(String.class),
                                ds.child("pass").getValue(String.class),
                                ds.child("hash_id").getValue(String.class));
                        listusers.add(user);
                        friend_id = ds.getKey();

                        DatabaseReference r = FirebaseDatabase.getInstance().getReference("users").child(friend_id).child("Request").child(user_id);
                        r.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String c = snapshot.child("request_type").getValue(String.class);
//                                System.out.println("MY ID ((((((((((((((((((((((((((((((((((((((((((( :" + c);
                                if (c != null) {
                                    currentState = (String) snapshot.child("request_type").getValue(String.class);
                                } else {
                                    currentState = "not_received";
//                                    System.out.println("MY ID ((((((((((((((((((((((((((((((((((((((((((( :" + snapshot.child("request_type").getValue(String.class));
                                }
                                System.out.println("MY ID ((((((((((((((((((((((((((((((((((((((((((( :" + currentState);
                                CustomAdapter arrayAdapter = new CustomAdapter(listusers);
                                list.setAdapter(arrayAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                } else {
                    Log.d("users", "No data found");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private ArrayList<dataHandler> localDataSet;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView email;
            Button item_btn;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                name = (TextView) view.findViewById(R.id.name);
                email = (TextView) view.findViewById(R.id.email);
                item_btn = (Button) view.findViewById(R.id.item_btn);

            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         *                by RecyclerView.
         */
        public CustomAdapter(ArrayList<dataHandler> dataSet) {
            this.localDataSet = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.user_item, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            System.out.println("gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg" + user_id + " " + friend_id + " " + (user_id.equals(friend_id)));
            if (user_id.equals(friend_id)) {
                System.out.println("Hello");
                viewHolder.item_btn.setVisibility(View.INVISIBLE);
            }
            else {
                if (currentState.equals("not_received")) {
                    viewHolder.item_btn.setText("Add Friend");
                }
                else if (currentState.equals("received")) {
                    viewHolder.item_btn.setText("Cancel Request");
                    viewHolder.item_btn.setBackgroundColor(0xFFD40000);
                }
            }

            dataHandler thisuser = localDataSet.get(position);
            viewHolder.name.setText(thisuser.getName());
            viewHolder.email.setText(thisuser.getEmail());

            viewHolder.item_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentState.equals("not_received")) {
                        mref.child(friend_id).child("Request").child(user_id).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                viewHolder.item_btn.setText("Cancel Request");
                                viewHolder.item_btn.setBackgroundColor(0xFFD40000);

                            }
                        });
                    } else if (currentState.equals("received")) {
                        mref.child(friend_id).child("Request").child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                viewHolder.item_btn.setText("Add Friend");
                                viewHolder.item_btn.setBackgroundColor(0xFF000000);
                            }
                        });
                    }
                }
            });
        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }


}


