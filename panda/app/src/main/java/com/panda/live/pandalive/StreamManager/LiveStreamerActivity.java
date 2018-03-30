package com.panda.live.pandalive.StreamManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.panda.live.pandalive.LiveViewer.InteractionDialogFragment;
import com.panda.live.pandalive.R;

public class LiveStreamerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_streamer);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_stream_video, ViewFinderFragment.newInstance()).commit();
        new InteractionDialogFragment().show(getSupportFragmentManager(),"InteractionDialogFragment");
    }
}
