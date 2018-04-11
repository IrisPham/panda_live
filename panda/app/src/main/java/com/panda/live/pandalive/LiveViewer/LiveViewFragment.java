package com.panda.live.pandalive.LiveViewer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.PreferencesManager;

import java.io.IOException;

public class LiveViewFragment extends Fragment{

    SurfaceView mVideoSurface;
    TextView mPlayerStatusTextView;
    BroadcastPlayer mBroadcastPlayer;
    MediaController mMediaController = null;

    private Context mContext;
    private View mView;

    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            Log.e("TAG", "Status " + playerState);
//            if (mPlayerStatusTextView != null)
//                //mPlayerStatusTextView.setText("Status: " + playerState);
            if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED || playerState == PlayerState.COMPLETED) {
                if (mMediaController == null && mBroadcastPlayer != null && !mBroadcastPlayer.isTypeLive()) {
                    mMediaController = new MediaController(mContext);
                    mMediaController.setAnchorView(mVideoSurface);
                    mMediaController.setMediaPlayer(mBroadcastPlayer);
                }
                if (mMediaController != null) {
                    mMediaController.setEnabled(true);
                    // mMediaController.show();
                }
            } else if (playerState == PlayerState.ERROR || playerState == PlayerState.CLOSED) {
                if (mMediaController != null) {
                    mMediaController.setEnabled(false);
                    mMediaController.hide();
                }
                mMediaController = null;
            }

        }
        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
            Log.e("TAG", "Status " + live + " width " + width + "height " + height);
        }
    };

    public LiveViewFragment() {
        // Required empty public constructor
    }

    public static LiveViewFragment newInstance() {
        LiveViewFragment fragment = new LiveViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_view, container, false);
        mView = view;
        mVideoSurface = view.findViewById(R.id.VideoSurfaceView);
       // mPlayerStatusTextView = view.findViewById(R.id.PlayerStatusTextView);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoSurface = null;
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;
        if (mMediaController != null)
            mMediaController.hide();
        mMediaController = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "Loading latest broadcast");
        mVideoSurface = mView.findViewById(R.id.VideoSurfaceView);
//        mPlayerStatusTextView.setText("Loading latest broadcast");
        initPlayer(getActivity().getIntent().getStringExtra("URL"));
    }

    void initPlayer(String resourceUri) {
        Log.e("TAG","uri " + resourceUri);
        if (resourceUri == null) {
//            if (mPlayerStatusTextView != null)
//                mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this.getContext(), resourceUri, PreferencesManager.APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();

    }

}
