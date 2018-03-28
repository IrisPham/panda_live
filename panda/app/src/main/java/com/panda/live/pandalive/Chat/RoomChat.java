package com.panda.live.pandalive.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.panda.live.pandalive.R;

/**
 * Created by levan on 28/03/2018.
 */

public class RoomChat extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomchat);


        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    private void loadData(){
}




}
