package com.example.login_register;

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
    private String currentUname;
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

    public String getUnameFromUid(FirebaseUser user){

        //get hashcode of user from auth
        String currentUserId = user.getUid();


        //get reference of firestore database
        DocumentReference fire_store_ref = FirebaseFirestore.getInstance().collection("users").document(currentUserId);

        //fire a query to find user_name storded in firestore database
        fire_store_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    currentUname = task.getResult().getString("user_name");
                }
                else{
                    currentUname = "Failed";
                }

            }
        });
        return currentUname;
    }
}

