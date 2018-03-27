package com.panda.live.pandalive.LiveStreamer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.panda.live.pandalive.LiveViewer.InteractionDialogFragment;
import com.panda.live.pandalive.LiveViewer.LiveViewFragment;
import com.panda.live.pandalive.R;

public class LiveStreamerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_streamer);

        LiveViewFragment liveViewFragment = new LiveViewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_stream_video, liveViewFragment).commit();
        new InteractionDialogFragment().show(getSupportFragmentManager(),"InteractionDialogFragment");
    }
}
