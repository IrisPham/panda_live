package com.panda.live.pandalive.Login;

/**
 * Created by levan on 15/03/2018.
 */

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.Profile;
import com.panda.live.pandalive.data.model.User;
import com.panda.live.pandalive.data.model.Data;

public class SetPass extends AppCompatActivity {

    private EditText mPass;
    private Data mData;
    private String pass;
    private Button mButtonComplete;
    private Intent mIntent;
    private String userID;

    //add Firebase Database stuff
    private static final String TAG = "AddToDatabase";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        mIntent = new Intent(this,PhoneLogin.class);
        mData = new Data();
        mPass = findViewById(R.id.edit_pass);
        mButtonComplete = findViewById(R.id.button_complete);
        Toast.makeText(getApplicationContext(),pass,Toast.LENGTH_SHORT).show();


        //Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sign_up);

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

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();



        // Read from the database


        mButtonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Submit pressed.");
                pass = mPass.getText().toString();


                //handle the exception if the EditText fields are null

                    User user = new User(mData.phoneNum, pass, mData.name,
                            "NO", "NO", 1000, 0, 0 );
                    Profile profile = new Profile("NO","NO", "NO", "NO");
                    myRef.child("users").child(userID).setValue(user);
                    myRef.child("users").child(userID).child("profile").setValue(profile);
                    toastMessage("Đăng kí thành công !");
                    startActivity(mIntent);
            }
        });
    }


    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(mIntent);
        return true;
    }
}
