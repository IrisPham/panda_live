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

    private RoundedImageView mAvatar;
    private TextView mFullName, mLogout, mID;
    private GoogleSignInClient mGoogleSignInClient;
    private Uri filePath;
    private StorageReference ref;
    private CharSequence colors[] = new CharSequence[]{"Thư viện", "Camera"};
    private Intent mIntentMain;
    private Context mContext;


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

        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnCaptureImage(v);
            }
        });


        //Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_search_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });

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
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_profile:
                startActivity(new Intent(ProfileActivity.this, ProfileDetailActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        ///onBackPressed();
        //startActivity(mIntent);
        return true;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
    }

    private void takePicture() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);
    }

    private void onClickBtnCaptureImage(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Đính kèm file");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        //        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                        chooseImage();
                        break;
                    case 1:
                        ActivityCompat.requestPermissions(ProfileActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 2);
                        takePicture();
                        break;
                }
            }
        });
        builder.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    filePath = imageReturnedIntent.getData();
                    Glide.with(this).load(filePath).into(mAvatar);
                    uploadImage();
                    updateValueCheckAvatar();
                    break;
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    filePath = imageReturnedIntent.getData();
                    Glide.with(this).load(filePath).into(mAvatar);
                    uploadImage();
                    updateValueCheckAvatar();
                    break;
                }
            case 2:
                //takePicture();
                break;
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            ref = mStorageReference.child("images").child(PreferencesManager
                    .getUserIdFirebase(getApplicationContext()) + "/avatarProfile");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }

    }


    private void signOutFaceBook() {
        Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        LoginManager.getInstance().logOut();
        startActivity(mIntentMain);
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

    }

    private void signOutPhone() {
        Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        startActivity(mIntentMain);
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
                .getUserIdFirebase(mContext)+"/avatarProfile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

    public void updateValueCheckAvatar(){
        int value = PreferencesManager.getValueStateLogin(mContext);

        switch (value) {
            case 1: //Login with Facebook
                PreferencesManager.setCheckUpdateAvatarFace(mContext,
                        PreferencesManager.getCheckUpdateAvatarFace(mContext)+1);
                break;

            case 2: //Login with Google
                PreferencesManager.setCheckUpdateAvatarGoogle(mContext,
                        PreferencesManager.getCheckUpdateAvatarGoogle(mContext)+1);
                break;

            case 3: //Login with Phone
                PreferencesManager.setCheckUpdateAvatarPhone(mContext,
                        PreferencesManager.getCheckUpdateAvatarPhone(mContext)+1);
                break;
            default:
                return;
        }
    }
}
