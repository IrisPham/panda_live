package com.panda.live.pandalive.LogOut;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;



import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.panda.live.pandalive.MainActivity.MainActivity;
import com.panda.live.pandalive.R;

/**
 * Created by levan on 14/02/2018.
 */
public class LogOutActivity extends AppCompatActivity implements
        View.OnClickListener {
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        // Views

        // Button listeners
        findViewById(R.id.button_logout).setOnClickListener(this);

    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {


            //    findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
            //    findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            //} else {


            findViewById(R.id.button_logout).setVisibility(View.VISIBLE);
            //   findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        //switch (v.getId()) {
        //    case R.id.button_facebook_login:
        signOut();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        //        break;
        //    case R.id.sign_out_button:
        //        signOut();
        //        break;
        //    case R.id.disconnect_button:
        //        revokeAccess();
        //        break;
    }

}
