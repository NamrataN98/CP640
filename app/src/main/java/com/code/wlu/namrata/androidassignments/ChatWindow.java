package com.code.wlu.namrata.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    ChatDatabaseHelper myDb = new ChatDatabaseHelper(this);
    private SQLiteDatabase database;

    private String[] columns = {myDb.KEY_ID, myDb.KEY_MESSAGE};
    private static String ACTIVITY_NAME = "ChatWindow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        Button sendButton = (Button) findViewById(R.id.buttonChat);
        EditText editTextBox = (EditText) findViewById(R.id.EditTextBox);
        ListView viewBox = (ListView) findViewById(R.id.chatView);

        ChatAdapter messageAdapter = new ChatAdapter( this );
        viewBox.setAdapter(messageAdapter);
        database = myDb.getWritableDatabase();
        messages = getAllItem();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typedMessage = editTextBox.getText().toString();

                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, typedMessage);
                updateMessages(typedMessage);

                messages.add(typedMessage);
                messageAdapter.notifyDataSetChanged();
                editTextBox.setText("");
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
        database.close();
    }

    public void updateMessages(String messages) {
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, messages);
        database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

    }

    public ArrayList<String> getAllItem() {
        ArrayList<String> items = new ArrayList<>();
        Cursor cursor = database.query(ChatDatabaseHelper.TABLE_NAME, columns,
                null, null, null, null, null);
        int size = cursor.getCount();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String messages = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE " + cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "Cursor's column count= " + (cursor.getColumnCount()));
            items.add(messages);
            cursor.moveToNext();
        }
        for(int idx=0;idx<cursor.getColumnCount();idx++)
        {
            Log.i(ACTIVITY_NAME, "Cursor's column Names= " + (cursor.getColumnName(idx)));
        }
        cursor.close();
        return items;

    }


}

