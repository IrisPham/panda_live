package com.panda.live.pandalive.Chat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.adapter.ChatAdapter;
import com.panda.live.pandalive.data.model.DataChat;

import java.util.ArrayList;

/**
 * Created by levan on 28/03/2018.
 */

public class ChatActivity extends AppCompatActivity {
    private FloatingActionButton mSend;
    private EditText mMessage;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private static RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DataChat> mData;
    private ChatAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //mSend = findViewById(R.id.textMessage);
        mMessage = findViewById(R.id.etInput);
        mRecyclerView = findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new ChatAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mMessage.getText().toString());
                mMessage.setText("");

            }
        });
        retrieveMessage();
    }

    public void sendMessage(String s) {
        DataChat datachat = new DataChat(PreferencesManager.getName(this.getApplicationContext()), s);
        mRef.child("chat").child("123").setValue(datachat);
    }

    public void retrieveMessage(){
        mRef.child("chat").child("123").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataChat datachat = dataSnapshot.getValue(DataChat.class);
                String name = datachat.name;
                String message = datachat.message;
                mData.add(new DataChat(name + ": ", message));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });


    }


}

