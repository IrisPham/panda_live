package com.panda.live.pandalive.data.model;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.panda.live.pandalive.Utils.PreferencesManager;

public class GroupBroadcast {
    private static String TAG = GroupBroadcast.class.getName();
    private SurfaceView mVideoSurface;
    private BroadcastPlayer mBroadcastPlayer;
    private MediaController mMediaController = null;
    private String mUrl;
    private Context mContext;
    private BroadcastPlayer.Observer mBroadcastPlayerObserver;

    public GroupBroadcast(SurfaceView mVideoSurface, String mUrl, Context mContext) {
        this.mVideoSurface = mVideoSurface;
        this.mUrl = mUrl;
        this.mContext = mContext;
        initBroadcastPlayer();
    }

    public void initBroadcastPlayer() {
        mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
            @Override
            public void onStateChange(PlayerState playerState) {
                Log.e("TAG", "Status " + playerState);
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
                if(playerState == playerState.COMPLETED){
                    mVideoSurface.setVisibility(View.GONE);
                    removeBroadcastPlayer();
                }

            }

            @Override
            public void onBroadcastLoaded(boolean live, int width, int height) {
                Log.e("TAG", "Status " + live + " width " + width + "height " + height);
            }
        };
        initPlayer();
    }

    public void initPlayer() {
        Log.e(TAG, "uri " + mUrl);
        if (mUrl == null) {
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(mContext, mUrl, PreferencesManager.APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();
    }

    public void removeBroadcastPlayer() {
        mVideoSurface = null;
        mBroadcastPlayer = null;
        mMediaController = null;
        mUrl = null;
        mContext = null;
        mBroadcastPlayerObserver = null;
    }
}
