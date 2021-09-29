package com.example.login_register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HelperFunctions {
    private String currentUname, user_id;
    public void HelperFunctions(){
        currentUname = "";
    }

    public String generateKeyFromTwoKeys(String key1, String key2){
        if (key1.compareTo(key2) < 0){
            return key1+key2;
        }
        else{
            return key2+key1;
        }
    }

    public static String getUseridFromEmail(String email){
        int index = email.indexOf('@');
        return email.substring(0,index);
    }

    public void doActiononClickActionBtn(Context thisActivity, int id){
        Intent intent = null;

        switch (id){

            case R.id.action_chat:
                intent = new Intent(thisActivity,chat_listview_of_friends.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                thisActivity.startActivity(intent);
                break;

            case R.id.action_add_friend:
                intent = new Intent(thisActivity,search_users.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                thisActivity.startActivity(intent);
                break;

            case R.id.action_pending_requests:
                intent = new Intent(thisActivity,list_of_requests.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                thisActivity.startActivity(intent);
                break;

            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(thisActivity, "Logged out", Toast.LENGTH_SHORT).show();
                intent = new Intent(thisActivity, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                thisActivity.startActivity(intent);
                break;
            // start the activity connect to the specified class
        }


    }

}

