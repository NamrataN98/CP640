package com.code.wlu.namrata.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    String typedMessage;
    ArrayList<String> messages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        Button sendButton = (Button) findViewById(R.id.buttonChat);
        EditText editTextBox = (EditText) findViewById(R.id.EditTextBox);
        ListView viewBox = (ListView) findViewById(R.id.chatView);

        ChatAdapter messageAdapter = new ChatAdapter( this );
        viewBox.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typedMessage = editTextBox.getText().toString();
                messages.add(typedMessage);
                messageAdapter.notifyDataSetChanged();
            }
        });
    }

    public class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return messages.size();
        }

        public String getItem( int position) {
            return messages.get(position);
        }

        public View getView(int position,View ConvertView , ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null ;
            TextView message;
            if(position%2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
                message = (TextView)result.findViewById(R.id.messageText);
            }

            else{
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
                message = (TextView)result.findViewById(R.id.messageText2);
            }

            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }

    }
}

