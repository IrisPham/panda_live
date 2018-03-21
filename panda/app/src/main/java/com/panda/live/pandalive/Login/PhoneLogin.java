package com.panda.live.pandalive.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.panda.live.pandalive.MainActivity.MainActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.User.User;

/**
 * Created by levan on 15/03/2018.
 */

public class PhoneLogin extends AppCompatActivity {
    private static final String TAG = "PhoneLogin";

    private EditText mPhoneNum, mPass;
    private Button mLogin;
    private String phonenum, pass, userID,verifyphonenum,verifypass;
    private Intent mIntent;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_phone);
        mPhoneNum = findViewById(R.id.field_phone_number);
        mPass = findViewById(R.id.field_pwd);
        mLogin = findViewById(R.id.button_login);
        mIntent = new Intent(this, MainActivity.class);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("users");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        myRef.child(userID).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                verifyphonenum = mUser.id;
                verifypass = mUser.pwd;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenum = mPhoneNum.getText().toString();
                pass = mPass.getText().toString();
                if(phonenum.equals(verifyphonenum) && pass.equals(verifypass)){
                    startActivity(mIntent);
                }else{
                    toastMessage("Số điện thoại hoặc mật khẩu không đúng !");
                }

            }
        });


    }



    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
