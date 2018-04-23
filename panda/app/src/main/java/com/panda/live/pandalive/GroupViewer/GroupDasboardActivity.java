package com.panda.live.pandalive.GroupViewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.adapter.GroupAdapter;
import com.panda.live.pandalive.data.adapter.PandaAdapter;
import com.panda.live.pandalive.data.model.PandaModel;

import java.util.ArrayList;

public class GroupDasboardActivity extends AppCompatActivity {
    private String TAG = GroupDasboardActivity.class.getName();
    private RecyclerView mRecyclerView;
    private ArrayList<PandaModel> mPandaModels;
    private GroupAdapter mAdapter;
    private DatabaseReference mDatabase;

    private int mChannelId;
    private String mChannelName;
    private TextView mTvGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_dasboard);

        mRecyclerView = findViewById(R.id.rcv_panda);
        mTvGroupName = findViewById(R.id.tv_group_name);
        mPandaModels = new ArrayList<>();
        mAdapter = new GroupAdapter(mPandaModels, this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        //setup firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        //Get list channel from channel id
        mChannelId = getIntent().getIntExtra("CHANNELID", -2);
        mChannelName = getIntent().getStringExtra("CHANNEL_NAME");
        if (mChannelId != -1 && mChannelId != -2 && mChannelName != null) {
            mTvGroupName.setText(mChannelName);
            bindData();
        }
    }

    private void bindData() {
        mDatabase.child("RoomsOnline").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mPandaModels.isEmpty()) {
                    mPandaModels.clear();
                }
                for (DataSnapshot idRoomSnapshot : dataSnapshot.getChildren()) {
                    PandaModel pandaModel = idRoomSnapshot.getValue(PandaModel.class);
                    if (pandaModel.getData().channelId == mChannelId) {
                        Log.e(TAG, String.valueOf(mChannelId));
                        mPandaModels.add(pandaModel);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
