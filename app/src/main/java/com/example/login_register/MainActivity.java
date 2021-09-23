package com.example.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button LogIn, Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button[] Buttons = {
                findViewById(R.id.loginBtn),
                findViewById(R.id.registerBtn),
                findViewById(R.id.chatbtn)

        };

        Class[] classActivities = {
                Register_page.class,
                login.class,
                ChatScreen1.class
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



    }
}