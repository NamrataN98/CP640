package com.code.wlu.namrata.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        final EditText email = (EditText) findViewById(R.id.etEmaildress);
        final EditText password = (EditText) findViewById(R.id.etTextPassword);
        Button btnLogin = (Button) findViewById(R.id.etButton);

        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        String DefaultValue = sharedPreferences.getString("DefaultEmail", "email@domain.com");
        String DefaultData = sharedPreferences.getString("data", "");
        Log.i(ACTIVITY_NAME + DefaultData, "Last Data");

        if(DefaultData != "") {
            email.setText(DefaultData);
        } else {
            email.setText(DefaultValue);
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEmail = email.getText().toString();
                String newPassword = password.getText().toString();

                if (isEmailValid(newEmail)) {

                    SharedPreferences sharedPreferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("data", newEmail);
                    editor.putString(newEmail + newPassword + "data", newEmail + "/n" + newPassword);
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.invalid, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isEmailValid(CharSequence email) {
         return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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