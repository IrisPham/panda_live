package com.panda.live.pandalive.LiveSingle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.panda.live.pandalive.R;

public class LiveSingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_single);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_stream_video, LiveSingleViewFragment.newInstance()).commit();
        new LiveSingleInteractionDialogFragment().show(getSupportFragmentManager(),"InteractionDialogFragment");
    }

}
