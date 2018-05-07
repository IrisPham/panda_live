package com.panda.live.pandalive.profile;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.adapter.PandaAdapter;
import com.panda.live.pandalive.data.adapter.VideoOfflineAdapter;
import com.panda.live.pandalive.data.model.PandaModel;
import com.panda.live.pandalive.data.model.Post;
import com.panda.live.pandalive.data.model.Profile;
import com.panda.live.pandalive.data.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.Locale;
import java.util.Map;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;


/**
 * @author Pham Hoai An
 * */
 public class ProfileDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private boolean isEdited = false;
    private boolean mSelected = false;

    private LinearLayout mTitleContainer;
    private TextView mTitle, mBirthday;
    private EditText mName;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private EditText mAddress, mID, mNickname, mEducation, mJob;
    private Spinner mSpinner;
    private ImageView mAvatar, mBackground;
    private DatabaseReference mDatabase;
    private String mId;
    private ArrayList<String> mList = new ArrayList<>();
    private HintAdapter<String> mHintAdapter;
    private Profile mProfile;
    private String mGender = "";
    private Context mContext;
    private DatePickerDialog.OnDateSetListener mDate;
    private Calendar mCalendar;
    private Uri mFilePath;
    private RecyclerView mRecyclerView;
    private ArrayList<PandaModel> mPandaModels;
    private VideoOfflineAdapter mAdapter;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private CharSequence colors[] = new CharSequence[]{"Thư viện", "Camera"};
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_detail);
        mContext = this.getApplicationContext();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bindActivity();
        mBirthday.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mId = getIntent().getStringExtra("id");
        mAppBarLayout.addOnOffsetChangedListener(this);
        mToolbar.inflateMenu(R.menu.menu_profile_details);
        mFilePath = Uri.parse(PreferencesManager.getPhotoUri(getApplicationContext()));
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        setStateProfile();
        //Set event when the user onclick item edit on menu.
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (PreferencesManager.getID(getApplicationContext()).equals(mId)) {
                    switch (item.getItemId()) {
                        case R.id.action_edit_profile:
                            if (isEdited) {
                                isEdited = false;
                                mToolbar.getMenu().getItem(0).setIcon(R.drawable.ic_profile_details);
                                setStateProfile();
                                saveData();
                            } else {
                                isEdited = true;
                                mToolbar.getMenu().getItem(0).setIcon(R.drawable.ic_checked);
                                setStateProfile();
                            }
                            return false;
                    }
                    return false;
                }
                return false;
            }
        });
        //Đọc thông tin người dùng
        readData();
        bindData();
    }

    private void bindActivity() {
        mToolbar = findViewById(R.id.main_toolbar);
        mTitle = findViewById(R.id.main_textview_title);
        mTitleContainer = findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = findViewById(R.id.main_appbar);
        mAddress = findViewById(R.id.edt_hometown);
        mID = findViewById(R.id.edt_panda_id);
        mNickname = findViewById(R.id.edt_nick_name);
        mEducation = findViewById(R.id.edt_education);
        mJob = findViewById(R.id.edt_job);
        mName = findViewById(R.id.tv_profile_name);
        mAvatar = findViewById(R.id.img_avatar_profile);
        mBackground = findViewById(R.id.img_background_profile);
        mBirthday = findViewById(R.id.tv_birthday);
        mSpinner = findViewById(R.id.sn_gender);
        mRecyclerView = findViewById(R.id.rcv_list_videos_offline);
        mPandaModels = new ArrayList<>();
        mAdapter = new VideoOfflineAdapter(mPandaModels, getApplicationContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void setAvatar(String s){
        int value = PreferencesManager.getValueStateLogin(mContext);
        switch (value) {
            case 1: //Login with Facebook
                if(PreferencesManager.getCheckUpdateAvatarFace(mContext) + 1 == 1){
                    Glide.with(mContext).load(mFilePath).into(mAvatar);
                    Glide.with(mContext).load(mFilePath).into(mBackground);
                }
                else{
                    downloadImage(s);
                }
                break;

            case 2: //Login with Google
                if(PreferencesManager.getCheckUpdateAvatarGoogle(mContext) + 1 == 1){
                    Glide.with(mContext).load(mFilePath).into(mAvatar);
                    Glide.with(mContext).load(mFilePath).into(mBackground);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_details, menu);
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_edit_profile:
//                if (isEdited) {
//                    isEdited = false;
//                    setStateProfile();
//                    saveData();
//                } else {
//                    isEdited = true;
//                    setStateProfile();
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void setStateProfile() {
        mAddress.setEnabled(isEdited);
        mNickname.setEnabled(isEdited);
        mEducation.setEnabled(isEdited);
        mJob.setEnabled(isEdited);
        mBirthday.setEnabled(isEdited);
        mSpinner.setEnabled(isEdited);
        mName.setEnabled(isEdited);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    private void saveData() {
        if(mSelected){
            mSelected = false;
        }
        else{
            mGender = mProfile.gender;
        }
        mTitle.setText(mName.getText().toString());
        mDatabase.child("users").child(PreferencesManager.getUserIdFirebase(mContext))
                .child("username").setValue(mName.getText().toString());
        mProfile = new Profile(mNickname.getText().toString(), mAddress.getText().toString(),
                mEducation.getText().toString(), mJob.getText().toString(),mGender
                ,mBirthday.getText().toString());
        mDatabase.child("users").child(PreferencesManager
                .getUserIdFirebase(getApplicationContext())).child("profile").setValue(mProfile);
        setName();
        PreferencesManager.setName(mContext, mName.getText().toString());
        Toast.makeText(getApplicationContext(), "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
        removeArray();

    }

    private void readData() {
        if (PreferencesManager.getID(getApplicationContext()).equals(mId)) {
            setProfileDetailIdolByPref(PreferencesManager.getUserIdFirebase(getApplicationContext()));
            setAvatar(PreferencesManager.getID(getApplicationContext()));
        } else {
            setProfileDetailIdolByID();
            downloadImage(mId);
            setAvatar(mId);
        }
    }

    public void setProfileDetailIdolByPref(String userID) {
        mDatabase.child("users").child(userID)
                .child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProfile = dataSnapshot.getValue(Profile.class);
                mID.setText(PreferencesManager.getID(getApplicationContext()));
                mAddress.setText(mProfile.address);
                mNickname.setText(mProfile.nickName);
                mEducation.setText(mProfile.education);
                mJob.setText(mProfile.job);
                mBirthday.setText(mProfile.birthday);
                mName.setText(PreferencesManager.getName(getApplicationContext()));
                mTitle.setText(PreferencesManager.getName(getApplicationContext()));
                setGender(mProfile.gender);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", databaseError.getMessage());
            }
        });
    }

    public void downloadImage(String id) {
        mStorageReference.child("images/" + id + "/avatarProfile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(getApplicationContext()).load(uri).into(mAvatar);
                Glide.with(getApplicationContext()).load(uri).into(mBackground);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void setProfileDetailIdolByID() {
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot id : dataSnapshot.getChildren()){
//                    User user = id.getValue(User.class);
                    Post post = id.getValue(Post.class);
                    if(post.id.equals(mId)){
//                        mProfile = id.getValue(Profile.class);
                        mID.setText(mId);
                        mAddress.setText(post.profile.address);
                        mNickname.setText(post.profile.nickName);
                        mEducation.setText(post.profile.education);
                        mJob.setText(post.profile.job);
                        mBirthday.setText(post.profile.birthday);
                        mName.setText(post.username);
                        mTitle.setText(post.username);
                        setGender(post.profile.gender);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LOI", databaseError.getMessage());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_birthday:
                createDatePicker();
                new DatePickerDialog(ProfileDetailActivity.this, mDate, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.img_avatar_profile:
                onClickBtnCaptureImage(v);
        }
    }

    public void createDatePicker(){
        mCalendar = Calendar.getInstance();
        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                mBirthday.setText(sdf.format(mCalendar.getTime()));


            }

        };
    }

    public void setHintValueSpinner(String s){
        mHintAdapter = new HintAdapter(this, s, mList);
        HintSpinner<String> spinner = new HintSpinner<>(
                mSpinner,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                mHintAdapter,
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        mSelected = true;
                        setGenderSelectedItem();
                    }

                });
        spinner.init();
    }

    public void setGender(String s){
        switch (s){
            case "none":
                mList.add("Nam");
                mList.add("Nữ");
                setHintValueSpinner("Chọn giới tính");
                break;
            case "Nam":
                mList.add("Nữ");
                setHintValueSpinner("Nam");
                break;
            case "Nữ":
                mList.add("Nam");
                setHintValueSpinner("Nữ");
                break;
        }

    }

    public void setGenderSelectedItem(){
        mGender = mSpinner.getSelectedItem().toString();
        switch (mGender)
        {
            case "Nam":
                removeArray();
                mList.add("Nữ");
                setHintValueSpinner("Nam");
                break;
            case "Nữ":
                removeArray();
                mList.add("Nam");
                setHintValueSpinner("Nữ");
                break;
        }
    }

    public void removeArray(){
        for(int i=0; i<mList.size(); i++){
            mList.remove(i);
        }
    }

    public void bindData(){
        mDatabase.child("VideosOffline").child(PreferencesManager.getID(getApplicationContext())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!mPandaModels.isEmpty()){mPandaModels.clear();}
                for (DataSnapshot idRoomSnapshot: dataSnapshot.getChildren()) {
                    PandaModel pandaModel = idRoomSnapshot.getValue(PandaModel.class);
                    if(pandaModel.getIdRoom().equals(mId)){
                        mPandaModels.add(pandaModel);
                        mAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
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
                        ActivityCompat.requestPermissions(ProfileDetailActivity.this,
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
                    mFilePath = imageReturnedIntent.getData();
                    Glide.with(this).load(mFilePath).into(mAvatar);
                    Glide.with(this).load(mFilePath).into(mBackground);
                    uploadImage();
                    updateValueCheckAvatar();
                    break;
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    mFilePath = imageReturnedIntent.getData();
                    Glide.with(this).load(mFilePath).into(mAvatar);
                    Glide.with(this).load(mFilePath).into(mBackground);
                    uploadImage();
                    updateValueCheckAvatar();
                    break;
                }
        }
    }

    public void updateValueCheckAvatar(){
        int value = PreferencesManager.getValueStateLogin(getApplicationContext());

        switch (value) {
            case 1: //Login with Facebook
                PreferencesManager.setCheckUpdateAvatarFace(getApplicationContext(),
                        PreferencesManager.getCheckUpdateAvatarFace(getApplicationContext())+1);
                break;

            case 2: //Login with Google
                PreferencesManager.setCheckUpdateAvatarGoogle(getApplicationContext(),
                        PreferencesManager.getCheckUpdateAvatarGoogle(getApplicationContext())+1);
                break;

            case 3: //Login with Phone
                PreferencesManager.setCheckUpdateAvatarPhone(getApplicationContext(),
                        PreferencesManager.getCheckUpdateAvatarPhone(getApplicationContext())+1);
                break;
            default:
                return;
        }
    }

    private void uploadImage() {

        if (mFilePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Cập nhật");
            progressDialog.show();

            mStorageReference = mStorageReference.child("images").child(PreferencesManager
                    .getID(getApplicationContext()) + "/avatarProfile");
            mStorageReference.putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileDetailActivity.this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileDetailActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void setName(){
        int value = PreferencesManager.getValueStateLogin(mContext);

        switch (value) {
            case 1: //Login with Facebook
                PreferencesManager.setNameLoginFace(mContext, mName.getText().toString().trim());
                break;

            case 2: //Login with Google
                PreferencesManager.setNameLoginGoogle(mContext, mName.getText().toString().trim());
                break;

            case 3: //Login with Phone

                PreferencesManager.setNameLoginPhone(mContext, mName.getText().toString().trim());
                break;
            default:
                return;
        }
    }
}
