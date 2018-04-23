package com.panda.live.pandalive.GroupViewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.panda.live.pandalive.LiveGoup.LiveGroupViewFragment;
import com.panda.live.pandalive.LiveSingle.LiveSingleInteractionDialogFragment;
import com.panda.live.pandalive.R;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_stream_video, GroupViewFragment.newInstance()).commit();
        new GroupInteractionDialogFragment().show(getSupportFragmentManager(),"InteractionDialogFragment");
    }
}
