package com.code.wlu.namrata.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

public class MessageFragment extends Fragment {

    TextView messageView, idView;
    Button delete;
    String message;
    int id;
    ChatWindow window = null;
    View inflated;

    public MessageFragment(){};

    public MessageFragment(ChatWindow window){
        this.window = window;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup parent,
            @Nullable Bundle savedInstanceState
    ) {
        Bundle mInfo = this.getArguments();
        message = mInfo.getString("message");
        id = mInfo.getInt("id");
        inflated = inflater.inflate(R.layout.fragment1, parent, false);

        messageView = inflated.findViewById(R.id.messageHere);
        idView = inflated.findViewById(R.id.idHere);
        delete = inflated.findViewById(R.id.delete);

        messageView.setText("Message Contents: " + message);
        idView.setText("ID=" + String.valueOf(id));

        if (window != null) {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.deleteMessage(id);
                    messageView.setText("Message Content:");
                    idView.setText("ID=");
                }
            });
        }

            return inflated;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}