package com.code.wlu.namrata.androidassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    String typedMessage;
    ArrayList<String> messages = new ArrayList<>();
    ChatDatabaseHelper myDb = new ChatDatabaseHelper(this);
    private SQLiteDatabase database;
    private SQLiteDatabase myRead;

    private String[] columns = {myDb.KEY_ID, myDb.KEY_MESSAGE};
    private static String ACTIVITY_NAME = "ChatWindow";
    private boolean ifFrameLayoutExsist = false;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        Button sendButton = (Button) findViewById(R.id.buttonChat);
        EditText editTextBox = (EditText) findViewById(R.id.EditTextBox);
        ListView viewBox = (ListView) findViewById(R.id.chatView);
        FrameLayout framesLay = (FrameLayout) findViewById(R.id.frames);

        if (framesLay == null) {
            ifFrameLayoutExsist = false;
        } else ifFrameLayoutExsist = true;

        ChatAdapter messageAdapter = new ChatAdapter(this);
        viewBox.setAdapter(messageAdapter);
        database = myDb.getWritableDatabase();
        myRead = myDb.getReadableDatabase();
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

        cursor = myRead.rawQuery("SELECT * from " + ChatDatabaseHelper.TABLE_NAME, new String[] {});

        viewBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle mInfo = new Bundle();
                mInfo.putInt("id", (int)i);
                mInfo.putString("message", cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE)));
                if (ifFrameLayoutExsist) {
                    Log.i(ACTIVITY_NAME,"Started Side Bar");
                    MessageFragment msgFragment = new MessageFragment(ChatWindow.this);
                    msgFragment.setArguments(mInfo);
                    FragmentTransaction ft =
                            getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frames, msgFragment);
                    ft.commit();
                }else{
                    Intent newIntent = new Intent(ChatWindow.this, MessageDetails.class);
                    newIntent.putExtras(mInfo);
                    startActivityForResult(newIntent, 11);
                }

            }
        });
    }

    protected void fillChatList(){
        cursor = myRead.rawQuery("SELECT * from " + ChatDatabaseHelper.TABLE_NAME, new String[] {});
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        messages.clear();

        while(!cursor.isAfterLast()){
            messages.add(cursor.getString(index));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(index));
            cursor.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor's column count =" + cursor.getColumnCount());
        for(int i=0; i < cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
        }
    }

    protected void deleteMessage(int id){

        cursor = myRead.rawQuery("SELECT * from " + ChatDatabaseHelper.TABLE_NAME, new String[] {});
        ChatDatabaseHelper.deleteMessage(database, (long) id);
        fillChatList();
        ChatAdapter messageAdapter = new ChatAdapter(this);
        messageAdapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data) {

        super.onActivityResult(requestCode, responseCode, data);

        if(requestCode == 11 && responseCode == Activity.RESULT_OK){
            deleteMessage(data.getIntExtra("id", 0));
        }
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

        public long getItemId(int position) {

            cursor = myRead.rawQuery("SELECT * from " + ChatDatabaseHelper.TABLE_NAME, new String[] {});
            cursor.moveToPosition(position);
            return cursor.getInt(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_ID));
        };

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
        cursor = database.query(ChatDatabaseHelper.TABLE_NAME, columns,
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

