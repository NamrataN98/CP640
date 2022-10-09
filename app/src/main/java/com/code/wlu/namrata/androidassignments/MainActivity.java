package com.code.wlu.namrata.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        Button btnHelloWorld = (Button) findViewById(R.id.button);
        Button btnStartChat = (Button) findViewById(R.id.buttonChat);


        btnHelloWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
                startActivityForResult(intent,10);
            }
        });

        btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(ACTIVITY_NAME, "User clicked Start Chat");

                Intent intent = new Intent(MainActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10 && resultCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME,"Returned to MainActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            Toast.makeText(this,messagePassed,Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    public void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    public void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    public void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}