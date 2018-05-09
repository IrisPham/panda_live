package com.panda.live.pandalive.profile;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.model.User;


/**
 * Created by levan on 09/05/2018.
 */

public class RankOfMeActivity extends AppCompatActivity {

    private TextView mMyExp;
    private TextView mExpNeed;
    private TextView mRankName;
    private ImageView mImvMyRank;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_of_me);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_rank_of_me);

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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mImvMyRank = findViewById(R.id.imv_rank_of_me);
        mMyExp = findViewById(R.id.tv_exp_now);
        mExpNeed = findViewById(R.id.tv_exp_need);
        mRankName = findViewById(R.id.tv_rank_name);

        setExp();

    }

    public void setExp(){
        mDatabase.child("users").child(PreferencesManager
                .getUserIdFirebase(getApplicationContext()))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mMyExp.setText(user.getExp()+ "");
                mRankName.setText(user.getRank());
                setExpNeed(user.getExp());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setExpNeed(int exp){
        if(exp < 100){
            mExpNeed.setText("100");
            mImvMyRank.setBackgroundResource(R.drawable.ic_dong);
        }
        else if(exp < 250){
            mExpNeed.setText("250");
            mImvMyRank.setBackgroundResource(R.drawable.ic_bac);
        }
        else if(exp < 500){
            mExpNeed.setText("500");
            mImvMyRank.setBackgroundResource(R.drawable.ic_vang);
        }
        else if(exp < 1000){
            mExpNeed.setText("1000");
        }
        else {
            mExpNeed.setText("99999999");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}