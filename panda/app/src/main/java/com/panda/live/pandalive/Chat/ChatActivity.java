package com.panda.live.pandalive.Chat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.DataChat;

/**
 * Created by levan on 28/03/2018.
 */

public class ChatActivity extends AppCompatActivity {
    private FloatingActionButton mSend;
    private ListView mList;
    private EditText mMessage;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String mGetMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mSend = findViewById(R.id.fab);
        mList = findViewById(R.id.list_of_messages);
        mMessage = findViewById(R.id.input);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetMessage = mMessage.getText().toString();
            }
        });
    }

    public void sendMessage(){
        myRef.child("chat").child("123").child("999").setValue("");
    }

    public void retrieveMessage(){

    }

}
