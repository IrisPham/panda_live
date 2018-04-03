package com.panda.live.pandalive.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.panda.live.pandalive.Home.HomeActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.User;

/**
 * Created by levan on 15/03/2018.
 */

public class PhoneLogin extends AppCompatActivity {
    private static final String TAG = "PhoneLogin";

    private EditText mPhoneNum, mPass;
    private Button mLogin;
    private String phonenum, pass, userID, verifyphonenum, verifypass;
    private Intent mIntent;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private User mUser;
    private Context mContext;

    private boolean bool;
    private Intent intentMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_phone);
        mPhoneNum = findViewById(R.id.field_phone_number);
        mPass = findViewById(R.id.field_pwd);
        mLogin = findViewById(R.id.button_login);
        intentMain = new Intent(this, HomeActivity.class);


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenum = mPhoneNum.getText().toString();
                pass = mPass.getText().toString();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference();

                myRef.child("users").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            mUser = postSnapshot.getValue(User.class);
                            if(mUser.id.equals(phonenum)&& mUser.pwd.equals(pass)){
                                bool = true;
                                startActivity(intentMain);
                                toastMessage("Đăng nhập thành công !");
                                finish();
                                break;
                            }
                            else{
                                toastMessage("Sai tài khoản hoặc mật khẩu !");
                                break;
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Failed to read value.", databaseError.toException());
                    }
                });



            }
        });
        //Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sign_in);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}