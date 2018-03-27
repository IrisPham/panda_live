package com.panda.live.pandalive.LiveViewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.panda.live.pandalive.R;

public class LiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        LiveViewFragment liveViewFragment = new LiveViewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_stream_video, liveViewFragment).commit();
        new InteractionDialogFragment().show(getSupportFragmentManager(),"InteractionDialogFragment");
    }

}
