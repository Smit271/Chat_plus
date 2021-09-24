package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatScreen1 extends AppCompatActivity {

    private static final String TAG = "Kinetic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen1);

        //get Current user details
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference ref = fireStore.collection("users").document(uid);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    String user_name = task.getResult().getString("user_name");
                    Log.d(TAG, "User name: " + user_name);

                    FloatingActionButton fltBtn = (FloatingActionButton) findViewById(R.id.btn_message_send);
                    EditText user_message = (EditText) findViewById(R.id.message_edit_text);
                    fltBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            ChatMessage userChat = new ChatMessage(user_message.getText().toString(),
                                    user_name);
                            FirebaseDatabase.getInstance().getReference().push().setValue(userChat);
                            Log.d(TAG, "User name: " + user_name);
                            Toast.makeText(getApplicationContext(), "Logged In as " + user_name, Toast.LENGTH_LONG).show();

                            user_message.setText("");
                        }
                    });

                    displayChatMessages();
                }
            }
        });
        }


    public void displayChatMessages(){
        ListView ListOfMessages = (ListView) findViewById(R.id.listview_messaged);

        FirebaseListAdapter fire_adapter = new FirebaseListAdapter<ChatMessage>(ChatScreen1.this,ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                TextView messageText = (TextView) v.findViewById(R.id.messageText);

                messageText.setText(model.getMessageText());

            }
        };

        ListOfMessages.setAdapter(fire_adapter);
    }
}