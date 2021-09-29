package com.example.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class home extends AppCompatActivity {

    private static final String TAG = "Kinetic";
    Button friend_request;
    Button Addfriend, mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Addfriend = findViewById(R.id.add_friend);
        friend_request = findViewById(R.id.friend_request);
        mLogout = findViewById(R.id.logout);

        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        ActionBar actionBar = getSupportActionBar();
//        if (name == null) {
//            actionBar.setTitle("Welcome");
//        } else {
//            actionBar.setTitle("Welcome " + name);
//        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, search_users.class);
                // start the activity connect to the specified class
                startActivity(intent);
            }
        });

        friend_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, list_of_requests.class);
                startActivity(intent);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(home.this, login.class);
                // start the activity connect to the specified class
                startActivity(intent);
            }
        });

        /// By Karm Start
        Button mChat = (Button) findViewById(R.id.chatbtn1);
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this,chat_listview_of_friends.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(home.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate your main_menu into the menu
        getMenuInflater().inflate(R.menu.menu, menu);

        // Find the menuItem to add your SubMenu
        MenuItem myMenuItem = menu.findItem(R.id.empty);

        // Inflating the sub_menu menu this way, will add its menu items
        // to the empty SubMenu you created in the xml
        getMenuInflater().inflate(R.menu.sub_menu, myMenuItem.getSubMenu());

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        HelperFunctions helper = new HelperFunctions();

        //change intent based on item pressed
        helper.doActiononClickActionBtn(getApplicationContext(),id);

        return super.onOptionsItemSelected(item);
    }
}


