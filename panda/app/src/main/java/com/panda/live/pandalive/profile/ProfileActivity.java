package com.panda.live.pandalive.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.User.User;
import com.panda.live.pandalive.data.model.Data;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;
    private static final String TAG = "Profil";

    private Data mData;
    private RoundedImageView mAvatar;
    private TextView mFullName;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private User mUser;
    private String userID;
    private Uri filePath;
    private StorageReference ref;
    private CharSequence colors[] = new CharSequence[]{"Thư viện", "Camera"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mData = new Data();
        mAvatar = findViewById(R.id.imgAvatar);
        mFullName = findViewById(R.id.tv_full_name);
        mFullName.setText(mData.name);
        Glide.with(this).load(mData.URI).into(mAvatar);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //getImage();

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
//                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(takePicture, 0);//zero can be replaced with any action code
//                        //takePicture();
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
                    break;
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    filePath = imageReturnedIntent.getData();
                    Glide.with(this).load(filePath).into(mAvatar);
                    uploadImage();
                    break;
                }
            case 2:
                //takePicture();
                break;
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            ref = storageReference.child("images").child(userID + "/" + UUID.randomUUID().toString());
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

    public void getImage() {


        if(storageReference == null){
            Glide.with(ProfileActivity.this).load(mData.URI).into(mAvatar);
        }else{
            ref = storageReference.child("images").child(userID + "/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            if (downloadUrl != null) {
                                mAvatar.setImageURI(downloadUrl);
                            } else {

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(ProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());

                        }
                    });

        }

    }
}
