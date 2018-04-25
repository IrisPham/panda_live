package com.panda.live.pandalive.LiveGoup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.panda.live.pandalive.LiveSingle.LiveSingleInteractionDialogFragment;
import com.panda.live.pandalive.LiveSingle.LiveSingleViewFragment;
import com.panda.live.pandalive.R;

public class LiveGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_group);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_stream_video, LiveGroupViewFragment.newInstance()).commit();
        new LiveSingleInteractionDialogFragment().show(getSupportFragmentManager(),"InteractionDialogFragment");
    }
}
