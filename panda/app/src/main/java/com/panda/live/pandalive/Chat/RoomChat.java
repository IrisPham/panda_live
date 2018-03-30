package com.panda.live.pandalive.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.Data;
import com.panda.live.pandalive.data.model.DataChat;
import com.panda.live.pandalive.data.model.DataRoom;
import com.panda.live.pandalive.data.model.Room;
import com.panda.live.pandalive.data.model.User;

/**
 * Created by levan on 28/03/2018.
 */

public class RoomChat extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomchat);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                Intent intent = new Intent(RoomChat.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        Button btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomChat.this, ChatActivity.class);
                startActivity(intent);
            }
        });

    }

    public void loadData() {

        Room room = new Room("123","",true);
        DataRoom dataroom = new DataRoom(-1,"daucolink","khongcoma");
        myRef.child("rooms").push().setValue(room);
        String key = myRef.child("rooms").getKey();
        myRef.child(key).child("data").setValue(dataroom);
    }


}
