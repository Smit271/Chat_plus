package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import com.example.login_register.HelperFunctions;

public class list_of_requests extends AppCompatActivity {


    static DatabaseReference mref;
    private RecyclerView request_list;
    static String user_id, MyEmail;
    TextView no_data_found;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_requests);

        no_data_found = (TextView) findViewById(R.id.no_data_found);
        mAuth = FirebaseAuth.getInstance();

        MyEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        user_id = HelperFunctions.getUseridFromEmail(MyEmail);
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Request");
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        System.out.println("MY ID ((((((((((((((((((((((((((((((((((((((((((( :" + user_id);

        request_list = (RecyclerView) findViewById(R.id.request_list);
        mref = FirebaseDatabase.getInstance().getReference("users");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        request_list.setLayoutManager(layoutManager);
    }

    private void readData(DataSnapshot snapshot) {
        if (snapshot.exists()) {
            ArrayList<String> listusers = new ArrayList<>();

            for (DataSnapshot ds : snapshot.getChildren()) {
                String name_of_request = ds.getKey();

                listusers.add(name_of_request);
            }

            CustomAdapter arrayAdapter = new CustomAdapter(listusers);
            request_list.setAdapter(arrayAdapter);
        } else {
            no_data_found.setVisibility(View.VISIBLE);
//            Log.d("users77777777777777777777777777777777777777777777777777777777777777777777", "No data available");
        }
    }


    public static class CustomAdapter extends RecyclerView.Adapter<list_of_requests.CustomAdapter.ViewHolder> {

        private ArrayList<String> localDataSet;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            Button item_btn_accept, item_btn_decline;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View
                name = (TextView) view.findViewById(R.id.name);
                item_btn_accept = (Button) view.findViewById(R.id.item_btn_accept);
                item_btn_decline = (Button) view.findViewById(R.id.item_btn_decline);
            }

        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         *                by RecyclerView.
         */
        public CustomAdapter(ArrayList<String> dataSet) {
            this.localDataSet = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public list_of_requests.CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.friend_request, viewGroup, false);

            return new list_of_requests.CustomAdapter.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(list_of_requests.CustomAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            String thisuser = localDataSet.get(position);
            if (!localDataSet.isEmpty()) {
                viewHolder.name.setText(thisuser);
//                Log.d("#####################################################", thisuser);
            }

            viewHolder.item_btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mref.child(user_id).child("friends").child(thisuser).child("request_type").setValue("accepted").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(view.getContext(), "Request Accepted ", Toast.LENGTH_LONG).show();
                            viewHolder.name.setVisibility(view.GONE);
                            viewHolder.item_btn_accept.setVisibility(View.GONE);
                            viewHolder.item_btn_decline.setVisibility(View.GONE);
                        }
                    });
                    mref.child(thisuser).child("friends").child(user_id).child("request_type").setValue("accepted").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                    mref.child(user_id).child("Request").child(thisuser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                }
            });
            viewHolder.item_btn_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mref.child(user_id).child("Request").child(thisuser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(view.getContext(), "Request Declined ", Toast.LENGTH_LONG).show();
                            viewHolder.name.setVisibility(view.GONE);
                            viewHolder.item_btn_accept.setVisibility(View.GONE);
                            viewHolder.item_btn_decline.setVisibility(View.GONE);
                        }
                    });
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
