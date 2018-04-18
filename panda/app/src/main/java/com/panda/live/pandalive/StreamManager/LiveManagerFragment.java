package com.panda.live.pandalive.StreamManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.panda.live.pandalive.LiveViewer.InteractionFragment;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.Utils.Settings;


public class LiveManagerFragment extends Fragment {
    private SurfaceViewWithAutoAR mPreviewSurfaceView;
    private Broadcaster mBroadcaster;
    private Display mDefaultDisplay;
    private Settings mSettings;


    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {

        }

        @Override
        public void onStreamHealthUpdate(int i) {

        }

        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {

        }

        @Override
        public void onCameraError(CameraError cameraError) {

        }

        @Override
        public void onChatMessage(String s) {

        }

        @Override
        public void onResolutionsScanned() {

        }

        @Override
        public void onCameraPreviewStateChanged() {

        }

        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {

        }

        @Override
        public void onBroadcastIdAvailable(String s) {

        }
    };

    public LiveManagerFragment() {
    }

    public static LiveManagerFragment newInstance() {
        LiveManagerFragment fragment = new LiveManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDefaultDisplay = this.getActivity().getWindowManager().getDefaultDisplay();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_live_manager, container, false);
        mPreviewSurfaceView = itemView.findViewById(R.id.PreviewSurfaceView);
        mBroadcaster = new Broadcaster(this.getActivity(), PreferencesManager.APPLICATION_ID, this.mBroadcasterObserver);
        return itemView;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mBroadcaster.onActivityPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, 1);

        mBroadcaster.onActivityResume();
        mBroadcaster.setRotation(this.mDefaultDisplay.getRotation());
        mPreviewSurfaceView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
        mBroadcaster.setCameraSurface(mPreviewSurfaceView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mBroadcaster != null) {
            this.mBroadcaster.onActivityDestroy();
        }
        this.mBroadcaster = null;
    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this.getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }


}
