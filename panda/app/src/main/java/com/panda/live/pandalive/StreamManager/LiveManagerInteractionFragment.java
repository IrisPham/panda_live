package com.panda.live.pandalive.StreamManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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
import com.panda.live.pandalive.LiveSingle.LiveSingleActivity;
import com.panda.live.pandalive.LiveViewer.InteractionFragment;
import com.panda.live.pandalive.MainActivity.MainActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.Utils.Settings;
import com.panda.live.pandalive.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Android Studio on 2/6/2018.
 */

public class LiveManagerInteractionFragment extends Fragment implements View.OnClickListener{
    private View mView;
    private EditText mEdtTitle;
    private boolean isGroup = false;
    private Settings mSetting;
    private CircleImageView mAvatar;
    private CharSequence colors[] = new CharSequence[]{"Thư viện", "Camera"};
    private static final String TAG = "LiveManager";
    private Uri mFilePath;
    private String userID;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;
    private StorageReference mRefStorage;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private Context mContext;
    private int mChangeAvatar = 0;
    private Intent mIntentLiveSingle;
    public LiveManagerInteractionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        mSetting = Settings.getInstance(this.getContext());
        mSetting.setAuthor(PreferencesManager.getID(this.getContext()));

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        mIntentLiveSingle = new Intent(this.getActivity(), LiveSingleActivity.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live_manager_interaction, container, false);
            mEdtTitle = mView.findViewById(R.id.edt_title_live);
            mAvatar = mView.findViewById(R.id.imgAvatar);
            mView.findViewById(R.id.btn_start_live).setOnClickListener(this);
            mView.findViewById(R.id.layout_live_group).setOnClickListener(this);
            mView.findViewById(R.id.layout_live_single).setOnClickListener(this);
            mView.findViewById(R.id.imgAvatar).setOnClickListener(this);
            setAvatar(PreferencesManager.getID(mContext));
            mEdtTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s != null ) mSetting.setLiveTitle(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_live:
                handleStartBroadCast();
                break;
            case R.id.layout_live_single:
                isGroup = true;
                break;
            case R.id.layout_live_group:
                showAlertDialogWithListview();
                break;
            case R.id.imgAvatar:
                onClickBtnCaptureImage(v);
            default:
                break;
        }
    }

    public void setAvatar(String s){
        int value = PreferencesManager.getValueStateLogin(mContext);
        switch (value) {
            case 1: //Login with Facebook
                if(PreferencesManager.getCheckUpdateAvatarFace(mContext) + 1 == 1){
                    Glide.with(mContext).load(Uri.parse(PreferencesManager.getPhotoUri(mContext))).into(mAvatar);
                }
                else{
                    downloadImage(s);
                }
                break;

            case 2: //Login with Google
                if(PreferencesManager.getCheckUpdateAvatarGoogle(mContext) + 1 == 1){
                    Glide.with(mContext).load(mFilePath).into(mAvatar);
                }
                else{
                    downloadImage(s);
                }
                break;

            case 3: //Login with Phone
                if(PreferencesManager.getCheckUpdateAvatarPhone(mContext) + 1 == 1){
                    return;
                }
                else{
                    downloadImage(s);
                }

                break;
            default:
                return;
        }
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
//                        ActivityCompat.requestPermissions(getActivity(),
//                                new String[]{Manifest.permission.CAMERA}, 2);
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
                    mFilePath = imageReturnedIntent.getData();
                    Glide.with(this).load(mFilePath).into(mAvatar);
                    uploadImage();
                    mChangeAvatar = 1;
                    break;
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    mFilePath = imageReturnedIntent.getData();
                    Glide.with(this).load(mFilePath).into(mAvatar);
                    uploadImage();
                    mChangeAvatar = 1;
                    break;
                }
            case 2:
                //takePicture();
                break;
        }

    }

    private void uploadImage() {

        if (mFilePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle("Cập nhật");
            progressDialog.show();
            mRefStorage = mStorageReference.child("images").child(PreferencesManager.getID(getContext()) + "/avatarLive");
            mRefStorage.putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Đang cập nhật... " + (int) progress + "%");
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO) && !hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        else if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this.getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void handleStartBroadCast() {
        if (isGroup) {
            startActivity(mIntentLiveSingle);
        } else {
            //startActivity(new Intent(this.getActivity(), L.class));
        }
    }

    public void showAlertDialogWithListview()
    {
        List<String> topic = new ArrayList<>();
        topic.add("Học tập");
        topic.add("Âm nhạc");
        topic.add("Phim");
        topic.add("Tán gẫu");

        final CharSequence[] Topics = topic.toArray(new String[topic.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Chọn chủ đề");
        dialogBuilder.setItems(Topics, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Topics[item].toString();  //Selected item in listview
                isGroup = true;
                Toast.makeText(getContext(),selectedText,Toast.LENGTH_SHORT).show();
                isGroup = false;

            }
        });
        AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }

    public void downloadImage(String id) {
        mStorageReference.child("images/" + id + "/avatarProfile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
}
