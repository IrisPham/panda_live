package com.panda.live.pandalive.profile;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.panda.live.pandalive.data.adapter.RankAdapter;
import com.panda.live.pandalive.data.model.RankModel;
import com.panda.live.pandalive.data.model.User;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by levan on 19/04/2018.
 */

public class RankActivity extends AppCompatActivity {
    private static final String TAG = RankActivity.class.getName();
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private RoundedImageView mAvatar1, mAvatar2, mAvatar3;
    private TextView mTvCoin1, mTvCoin2, mTvCoin3, mTvName1, mTvName2, mTvName3;
    private StorageReference mStorageReference;
    private View mView;
    private ImageView mImageList;
    private int i = 0;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<RankModel> mRankSort;
    private ArrayList<RankModel> mRankData;
    private RankAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTvCoin1 = findViewById(R.id.tv_coin1);
        mTvCoin2 = findViewById(R.id.tv_coin2);
        mTvCoin3 = findViewById(R.id.tv_coin3);

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
        mView = getLayoutInflater().inflate(R.layout.recycler_view_rank, null);
        mImageList = mView.findViewById(R.id.imgAvatar);
        mStorageReference = FirebaseStorage.getInstance().getReference();

        binActivity();
        binData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Thread.interrupted();
        onBackPressed();
        return true;
    }

    private void binActivity() {
        mTvName1 = findViewById(R.id.tv_name1);
        mTvName2 = findViewById(R.id.tv_name2);
        mTvName3 = findViewById(R.id.tv_name3);
        mAvatar1 = findViewById(R.id.imgAvatar1);
        mAvatar2 = findViewById(R.id.imgAvatar2);
        mAvatar3 = findViewById(R.id.imgAvatar3);
        mRecyclerView = findViewById(R.id.recycler_view_rank_user);
        mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRankData = new ArrayList<>();
        mRankSort = new ArrayList<>();
        mAdapter = new RankAdapter(getApplicationContext(), mRankData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

    }

    public void binData() {
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mRankData.isEmpty() && !mRankSort.isEmpty()) {
                    mRankData.clear();
                    mRankSort.clear();
                }
                for (DataSnapshot coin : dataSnapshot.getChildren()) {
                    User user = coin.getValue(User.class);
                    Log.e(TAG, "Key get image in rank: " + coin.getKey() + " and id: " + user.getId());
                    mRankSort.add(new RankModel(null, user.getUsername(), user.getCoin(), user.getId()));
                }
                //After get list user then sort list with coin.
                sortDecreasing();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Lỗi", databaseError.getMessage());
            }
        });
    }

    private void sortDecreasing() {
        Collections.sort(mRankSort);
        bindDataTop();
        bindDataToList();
    }

    private void bindDataTop() {
        for (int i = 0; i < 3; i++) {
            final int pos = i;
            mStorageReference.child("images/" + mRankSort.get(i).getId() + "/avatarProfile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.e(TAG, "Uri image " + uri.toString() + " pos: " + pos);
                    switch (pos) {
                        case 0:
                            Glide.with(getApplicationContext()).load(uri).into(mAvatar1);
                            mTvName1.setText(mRankSort.get(pos).getName());
                            mTvCoin1.setText(String.valueOf(mRankSort.get(pos).getCoin()));
                            break;
                        case 1:
                            Glide.with(getApplicationContext()).load(uri).into(mAvatar2);
                            mTvName2.setText(mRankSort.get(pos).getName());
                            mTvCoin2.setText(String.valueOf(mRankSort.get(pos).getCoin()));
                            break;
                        case 2:
                            Glide.with(getApplicationContext()).load(uri).into(mAvatar3);
                            mTvName3.setText(mRankSort.get(pos).getName());
                            mTvCoin3.setText(String.valueOf(mRankSort.get(pos).getCoin()));
                            break;
                    }
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

    private void bindDataToList() {
        //add data rank from mRankSort to mRankData and Remove top for mRankData
        for (int x = 3; x < mRankSort.size(); x++) {
            mRankData.add(mRankSort.get(x));
        }
        for (int i = 0; i < mRankData.size(); i++) {
            final int pos = i;
            mStorageReference.child("images/" + mRankData.get(i).getId() + "/avatarProfile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    mRankData.get(pos).setUri(uri);
                    if (pos == mRankData.size() - 1) mAdapter.notifyDataSetChanged();
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
}
