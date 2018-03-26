package com.panda.live.pandalive.Login;

/**
 * Created by levan on 15/03/2018.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.Data;

public class SetPass extends AppCompatActivity {

    private EditText mPass;
    private Data mData;
    private User mUser;
    private String pass;
    private Button mButtonComplete;
    private DatabaseReference myRef;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        mData = new Data();
        mPass = findViewById(R.id.edit_pass);
        mButtonComplete = findViewById(R.id.button_complete);
        Toast.makeText(getApplicationContext(),pass,Toast.LENGTH_SHORT).show();

        mButtonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = mPass.getText().toString();

            }
        });


    }
    @IgnoreExtraProperties
    public class User {

        public String id;
        public String pwd;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
            mDatabase = FirebaseDatabase.getInstance();
        }

        public User(String id, String pwd) {
            this.id = id;
            this.pwd = pwd;
        }

        private void writeNewUser(String id, String pwd) {
            User user = new User(id, pwd);
            myRef = mDatabase.getReference();
            myRef.child("Users").child(mData.phoneNum).setValue(pass);
        }

    }
}
