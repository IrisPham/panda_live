package com.panda.live.pandalive.Live;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.panda.live.pandalive.R;

/*
* Đầu tiên thêm một Fragment khởi tạo live stream "chứa nội dung videos"
* InteractionDialogFragment dùng để hiển thị các tương tác của người dùng
* */
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
