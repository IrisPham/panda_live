package com.panda.live.pandalive.GroupViewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.adapter.PandaAdapter;
import com.panda.live.pandalive.data.model.PandaModel;

import java.util.ArrayList;

public class GroupDasboardActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<PandaModel> mPandaModels;
    private PandaAdapter mAdapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_dasboard);

        mRecyclerView = findViewById(R.id.rcv_panda);
        mPandaModels = new ArrayList<>();
        mAdapter = new PandaAdapter(mPandaModels, this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

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

        bindData();
    }

    private void bindData() {
//        mDatabase.child("RoomsOnline").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(!mPandaModels.isEmpty()){mPandaModels.clear();}
//                for (DataSnapshot idRoomSnapshot: dataSnapshot.getChildren()) {
//                    PandaModel pandaModel = idRoomSnapshot.getValue(PandaModel.class);
//                    mPandaModels.add(pandaModel);
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
