package com.panda.live.pandalive.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.User;

import java.util.ArrayList;


/**
 * Created by levan on 19/04/2018.
 */

public class RankActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private RoundedImageView mAvatar1, mAvatar2, mAvatar3;
    private TextView mCoin1, mCoin2, mCoin3, mName1, mName2, mName3;
    private int[]mCoinArr;
    private String[]mNameArr;
    private String[]mIDArr;
    private StorageReference mStorageReference;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        mDatabase = FirebaseDatabase.getInstance().getReference();
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

        mStorageReference = FirebaseStorage.getInstance().getReference();
        binActivity();
        binData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void binData(){
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCoinArr = new int[10];
                mNameArr = new String[10];
                mIDArr = new String[10];
                for(DataSnapshot coin : dataSnapshot.getChildren()){
                    User user = coin.getValue(User.class);
                    if(user.coin != 0){
                        mCoinArr[i] = user.coin;
                        mNameArr[i] = user.username;
                        mIDArr[i] = user.id;
                        i++;
                    }
                }
                sortDecreasing();
                setDataRank();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Lỗi", databaseError.getMessage());
            }
        });
    }

    public void sortDecreasing(){
        int temp;
        String name, id;
        for(int j=0; j<i; j++){
            for(int k=j+1; k<i; k++){
                if(mCoinArr[j] < mCoinArr[k]){
                    temp = mCoinArr[j];
                    mCoinArr[j] = mCoinArr[k];
                    mCoinArr[k] = temp;

                    name = mNameArr[j];
                    mNameArr[j] = mNameArr[k];
                    mNameArr[k] = name;

                    id = mIDArr[j];
                    mIDArr[j] = mIDArr[k];
                    mIDArr[k] = id;
                }
            }
        }
    }

    public void setDataRank(){
        mCoin1.setText(mCoinArr[0]+"");
        mCoin2.setText(mCoinArr[1]+"");
        mCoin3.setText(mCoinArr[2]+"");
        mName1.setText(mNameArr[0]);
        mName2.setText(mNameArr[1]);
        mName3.setText(mNameArr[2]);
        downloadImage(mIDArr[0], mAvatar1);
        downloadImage(mIDArr[1], mAvatar2);
        downloadImage(mIDArr[2], mAvatar3);
        i=0;
    }

    public void binActivity() {
        mRecyclerView = findViewById(R.id.recycler_view_rank);
        mCoin1 = findViewById(R.id.tv_coin1);
        mCoin2 = findViewById(R.id.tv_coin2);
        mCoin3 = findViewById(R.id.tv_coin3);
        mName1 = findViewById(R.id.tv_name1);
        mName2 = findViewById(R.id.tv_name2);
        mName3 = findViewById(R.id.tv_name3);
        mAvatar1 = findViewById(R.id.imgAvatar1);
        mAvatar2 = findViewById(R.id.imgAvatar2);
        mAvatar3 = findViewById(R.id.imgAvatar3);

    }
    public void downloadImage(String id, final RoundedImageView avatar) {
        mStorageReference.child("images/" + id + "/avatarProfile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(getApplicationContext()).load(uri).into(avatar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("Lỗi", exception.getMessage());
            }
        });
    }

}
