package com.example.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button LogIn, Register, Chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button[] Buttons = {
                findViewById(R.id.loginBtn),
                findViewById(R.id.registerBtn),
                //findViewById(R.id.chatbtn),
                findViewById(R.id.chatbtn1)
        };

        Class[] classActivities = {
                Register_page.class,
                login.class,
                //ChatScreen1.class,
                chat_listview_of_friends.class
        };

        //create dynamic loop to handle onclick event
        for (int btn_no = 0;btn_no<Buttons.length;btn_no++) {
            Button btn = (Button) Buttons[btn_no];

            int finalBtn_no = btn_no;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, classActivities[finalBtn_no]);

                    // start the activity connect to the specified class
                    startActivity(intent);
                }
            });

        }


//        LogIn = findViewById(R.id.loginBtn);
//        Register = findViewById(R.id.registerBtn);
//        Chat = findViewById(R.id.chatbtn);
//
////        String name = FirebaseAuth.getInstance().getCurrentUser().getUid();
////        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
//
//        // Click on Register button
//        Register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, Register_page.class);
//
//                // start the activity connect to the specified class
//                startActivity(intent);
//            }
//        });
//
//        // Click on Chat button
//        Chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ChatScreen1.class);
//
//                // start the activity connect to the specified class
//                startActivity(intent);
//            }
//        });
//
//        // Click on Login button
//        LogIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, login.class);
//
//                // start the activity connect to the specified class
//                startActivity(intent);
//            }
//        });

    }
}