package com.panda.live.pandalive.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.panda.live.pandalive.Login.LoginActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.Data;

import bolts.Task;

public class MainActivity extends AppCompatActivity {
    private ProfilePictureView img;
    private Data data;
    private Button logout;
    private Intent intent;
    private GoogleSignInClient mGoogleSignInClient;
    private int val;
    private TextView name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.imagev);
        data = new Data();
        img.setProfileId(data.ID);
        logout = (Button)findViewById(R.id.button_logout_id);
        name = (TextView)findViewById(R.id.name);
        intent = new Intent(this,LoginActivity.class);
        val = data.value;
        name.setText(data.name);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xét xem signin = gì thì signout bằng đó
                if(val == 1) {
                    signOutFaceBook();
                }else{
                    signOutGoogle();
                }


                startActivity(intent);
            }
        });

    }
    public void signOutFaceBook() {
        LoginManager.getInstance().logOut();
    }



    private void signOutGoogle() {
        Toast.makeText(this,"Đăng xuất",Toast.LENGTH_SHORT).show();
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                Toast.makeText(MainActivity.this,"Đăng xuất",Toast.LENGTH_SHORT).show();
            }
        });


    }

}