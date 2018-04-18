package com.panda.live.pandalive.profile;


import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.storage.StorageReference;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.model.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private TextView mTitle, mName, mBirthday;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private EditText mAddress, mID, mNickname, mEducation, mJob;
    private Spinner mSpinner;
    private ImageView mAvatar;
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private String mId, mMyParentNode;
    private ArrayList<String> mList = new ArrayList<>();
    private HintAdapter<String> mHintAdapter;
    private Profile mProfile;
    private String mGender = "";

    private DatePickerDialog.OnDateSetListener mDate;
    private Calendar mCalendar;
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
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bindActivity();
        mBirthday.setOnClickListener(this);
        mId = getIntent().getStringExtra("id");
        mAppBarLayout.addOnOffsetChangedListener(this);
        mToolbar.inflateMenu(R.menu.menu_profile_details);
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
        mBirthday = findViewById(R.id.tv_birthday);
        mSpinner = findViewById(R.id.sn_gender);
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
        mProfile = new Profile(mNickname.getText().toString(), mAddress.getText().toString(),
                mEducation.getText().toString(), mJob.getText().toString(),mGender
                ,mBirthday.getText().toString());
        mDatabase.child("users").child(PreferencesManager
                .getUserIdFirebase(getApplicationContext())).child("profile").setValue(mProfile);
        Toast.makeText(getApplicationContext(), "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
        removeArray();

    }

    private void readData() {
        Toast.makeText(getApplicationContext(), mId, Toast.LENGTH_SHORT).show();
        if (PreferencesManager.getID(getApplicationContext()).equals(mId)) {
            setProfileDetailIdol(PreferencesManager.getUserIdFirebase(getApplicationContext()));
            downloadImage(PreferencesManager.getID(getApplicationContext()));
        } else {
            getUserIdById();
            setProfileDetailIdol(mMyParentNode);
            downloadImage(mId);
        }
    }

    public void setProfileDetailIdol(String userID) {
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
                setGender();
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void getUserIdById() {
        final Query userQuery = mDatabase.orderByChild("id").equalTo(mId);
        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mMyParentNode = dataSnapshot.getKey();
                Toast.makeText(getApplicationContext(), mMyParentNode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    public void setGender(){
        switch (mProfile.gender){
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

}
