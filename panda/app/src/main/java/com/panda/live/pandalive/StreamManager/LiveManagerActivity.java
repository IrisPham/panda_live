package com.panda.live.pandalive.StreamManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.panda.live.pandalive.R;

public class LiveManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_manager);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_stream_video, LiveManagerFragment.newInstance()).commit();
        new LiveManagerDialogFragment().show(getSupportFragmentManager(), "InteractionDialogFragment");
    }
}
