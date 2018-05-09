package com.panda.live.pandalive.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.panda.live.pandalive.Login.LoginActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.model.Data;

import java.net.URL;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {
    FirebaseStorage mStorage;
    StorageReference mStorageReference;
    private static final String TAG = "Profile";
    private RelativeLayout mRlRank, mRlMyRank;
    private RoundedImageView mAvatar;
    private TextView mFullName, mLogout, mID;
    private GoogleSignInClient mGoogleSignInClient;
    private Uri filePath;
    private Intent mIntentMain;
    private Context mContext;
    private Intent mIntentProfileDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext = this.getApplicationContext();
        mIntentMain = new Intent(this, LoginActivity.class);
        mLogout = findViewById(R.id.tv_logout);
        mID = findViewById(R.id.tv_panda_id);
        String id = PreferencesManager.getID(mContext);
        mID.setText(id);
        mAvatar = findViewById(R.id.imgAvatar);
        filePath = Uri.parse(PreferencesManager.getPhotoUri(mContext));
        mFullName = findViewById(R.id.tv_full_name);
        mRlRank = findViewById(R.id.rl_rank);
        mRlMyRank = findViewById(R.id.rl_my_rank);
        String name = PreferencesManager.getName(mContext);
        mFullName.setText(name);
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);

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
        setAvatar();
//        setName();
        mRlRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });
        mRlMyRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, RankOfMeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setAvatar(){
        int value = PreferencesManager.getValueStateLogin(mContext);
        switch (value) {
            case 1: //Login with Facebook
                if(PreferencesManager.getCheckUpdateAvatarFace(mContext) + 1 == 1){
                    Glide.with(mContext).load(filePath).into(mAvatar);
                }
                else{
                    downloadImage();
                }
                break;

            case 2: //Login with Google
                if(PreferencesManager.getCheckUpdateAvatarGoogle(mContext) + 1 == 1){
                    Glide.with(mContext).load(filePath).into(mAvatar);
                }
                else{
                    downloadImage();
                }
                break;

            case 3: //Login with Phone
                if(PreferencesManager.getCheckUpdateAvatarPhone(mContext) + 1 == 1){
                    return;
                }
                else{
                    downloadImage();
                }
                break;
            default:
                return;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setAvatar();
        mFullName.setText(PreferencesManager.getName(mContext));
//        setName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_profile:
                transferData();
                mIntentProfileDetail.putExtra("id", mID.getText().toString());
                startActivity(mIntentProfileDetail);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void signOutFaceBook() {
        Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        LoginManager.getInstance().logOut();
        startActivity(mIntentMain);
        finish();
    }


    private void signOutGoogle() {
        Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                Toast.makeText(ProfileActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(mIntentMain);
        finish();

    }

    private void signOutPhone() {
        Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        startActivity(mIntentMain);
        finish();
    }


    private void showDialog(){

        final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(this);

        dialogBuilder
                .withTitle("Xác nhận")
                .withTitleColor("#FFFFFF")
                .withMessageColor("#FFFFFFFF")
                .withMessage("Bạn có muốn đăng xuất không ?")
                .withButton1Text("Đồng ý")
                .withButton2Text("Hủy")
                .withDialogColor("#4169E1")
                .withIcon(getResources().getDrawable(R.drawable.ic_like))
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int value = PreferencesManager.getValueStateLogin(mContext);

                        switch (value) {
                            case 1: //Login with Facebook
                                signOutFaceBook();
                                break;

                            case 2: //Login with Google
                                signOutGoogle();
                                break;

                            case 3: //Login with Phone
                                signOutPhone();
                                break;
                            default:
                                return;
                        }
                        finish();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .show();
    }

    public void downloadImage(){
        mStorageReference.child("images/"+PreferencesManager
                .getID(mContext)+"/avatarProfile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(mContext).load(uri).into(mAvatar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


    public void transferData(){
        mIntentProfileDetail = new Intent(ProfileActivity.this, ProfileDetailActivity.class);
        mIntentProfileDetail.putExtra("id", mID.getText());
    }

    public void setName(){
        int value = PreferencesManager.getValueStateLogin(mContext);

        switch (value) {
            case 1: //Login with Facebook
                mFullName.setText(PreferencesManager.getNameLoginFace(mContext));
                break;

            case 2: //Login with Google
                mFullName.setText(PreferencesManager.getNameLoginGoogle(mContext));
                break;

            case 3: //Login with Phone
                mFullName.setText(PreferencesManager.getNameLoginPhone(mContext));
                break;
            default:
                return;
        }
    }

}