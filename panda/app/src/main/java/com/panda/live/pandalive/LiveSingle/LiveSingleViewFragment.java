package com.panda.live.pandalive.LiveSingle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Service.Command;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.Utils.Settings;
import com.panda.live.pandalive.data.model.DataRoom;
import com.panda.live.pandalive.data.model.IrisModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LiveSingleViewFragment extends Fragment implements Broadcaster.ViewerCountObserver {
    private SurfaceViewWithAutoAR mPreviewSurfaceView;
    private Broadcaster mBroadcaster;
    private Display mDefaultDisplay;
    private Settings mSetting;
    private Command mCommand;
    private Context mContext;

    // Fire base
    private DatabaseReference mDatabase;

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.e("TAG", "Received status change: " + broadcastStatus);
            switch (broadcastStatus){
                case FINISHING:
                    removeBroadCast();
                    break;
                 default:
                     break;
            }
        }

        @Override
        public void onStreamHealthUpdate(int i) {

        }

        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.w("TAG", "Received connection error: " + connectionError + ", " + s);
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
            setDataToFirebase();
        }
    };

    public LiveSingleViewFragment() {
    }

    public static LiveSingleViewFragment newInstance() {
        LiveSingleViewFragment fragment = new LiveSingleViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDefaultDisplay = this.getActivity().getWindowManager().getDefaultDisplay();
        mSetting = Settings.getInstance(this.getContext());
        mCommand = Command.getInstance();
        mContext = this.getContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_live_single_view, container, false);
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
        mPreviewSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
        mBroadcaster.setCameraSurface(mPreviewSurfaceView);
        mBroadcaster.setAudioQuality(Broadcaster.AudioSetting.HIGH_QUALITY);
        mBroadcaster.setMaxLiveResolution(1080, 1920);
        mBroadcaster.setResolution(mDefaultDisplay.getWidth(), mDefaultDisplay.getHeight());
        setData();
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

    private void setData() {
        mBroadcaster.setAuthor(mSetting.getAuthor());
        mBroadcaster.setTitle(mSetting.getLiveTitle());
        startBroadcaster();
    }

    private void startBroadcaster() {
        if (mBroadcaster.canStartBroadcasting()) {
            mBroadcaster.startBroadcast();
        } else {
            Toast.makeText(this.getActivity(), "Broadcaster chưa sẵn sàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataToFirebase() {
        mCommand.irisApi(new Callback<IrisModel>() {
            @Override
            public void onResponse(Call<IrisModel> call, Response<IrisModel> response) {
                if (response.isSuccessful()) {
                    IrisModel model = response.body();
                    for (int i = 0; i < model.getResults().size(); i++) {
                        if (PreferencesManager.getID(mContext).equals(model.getResults().get(i).getAuthor()) && model.getResults().get(i).getType().equals("live")) {
                            Map<String, Object> values = new HashMap<>();
                            values.put("idRoom", PreferencesManager.getID(mContext));
                            values.put("isLock", "false");
                            values.put("pwdRoom", "none");
                            values.put("title",mSetting.getLiveTitle());

                            DataRoom dataRoom = new DataRoom(
                                    -1,
                                    model.getResults().get(i).getPreview(),
                                    model.getResults().get(i).getResourceUri());
                            values.put("data", dataRoom);

                            mDatabase.child("RoomsOnline").push().setValue(values);
                        } else {
                            Log.e("TAG", "Not ok");
                        }
                    }
                } else {
                    Log.e("TAG","not OK");
                }
            }

            @Override
            public void onFailure(Call<IrisModel> call, Throwable t) {
                Log.e("TAG", "Not ok" + t);
            }
        });
    }

    private void removeBroadCast(){
        Query idRoomQuery =  mDatabase.child("RoomsOnline").orderByChild("idRoom").equalTo(PreferencesManager.getID(mContext));

        idRoomQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot idRoomSnapshot: dataSnapshot.getChildren()) {
                    idRoomSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onCurrentViewersUpdated(long l) {
        Log.e("TAG", "current viewer " + String.valueOf(l));
    }

    @Override
    public void onTotalViewersUpdated(long l) {
        Log.e("TAG", " Total Viewer " + String.valueOf(l));
    }
}
