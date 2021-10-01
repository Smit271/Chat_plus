package com.chatplus.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button LogIn, Register, Chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

        Intent intent = new Intent(MainActivity.this,login.class);
        startActivity(intent);
            }
        }, 3000);
    }

}
