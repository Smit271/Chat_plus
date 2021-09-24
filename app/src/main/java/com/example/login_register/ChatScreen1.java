package com.example.login_register;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatScreen1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen1);


        FloatingActionButton fltBtn = (FloatingActionButton) findViewById(R.id.btn_message_send);
        EditText user_message = (EditText) findViewById(R.id.message_edit_text);
        fltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage userChat = new ChatMessage(user_message.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                FirebaseDatabase.getInstance().getReference().push().setValue(userChat);
                user_message.setText("");
            }
        });

        displayChatMessages();
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