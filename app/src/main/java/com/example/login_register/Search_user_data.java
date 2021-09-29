package com.example.login_register;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Search_user_data {
    private String user_id,friend_id,isFriend,currentState,extra1,extra2;
    private DatabaseReference fref,rref;
    public Search_user_data(String user_id, String friend_id) {
        this.user_id = user_id;
        this.friend_id = friend_id;
        get_details(user_id,friend_id);
    }

    private void get_details(String user_id, String friend_id) {
        fref = FirebaseDatabase.getInstance().getReference("users").child(friend_id).child("friends").child(user_id);
        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extra1 = snapshot.child("request_type").getValue(String.class);
                if (extra1 != null) {
                    isFriend = (String) snapshot.child("request_type").getValue(String.class);
                } else {
                    isFriend = "not_accepted";
                }
                System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww" + isFriend);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rref = FirebaseDatabase.getInstance().getReference("users").child(friend_id).child("Request").child(user_id);
        rref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                extra2 = snapshot.child("request_type").getValue(String.class);
                if (extra2 != null) {
                    currentState = (String) snapshot.child("request_type").getValue(String.class);
                } else {
                    currentState = "not_received";
                }
                System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww" + currentState);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public String getUser_id() {
        return user_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public String getCurrentState() {
        return currentState;
    }
}
