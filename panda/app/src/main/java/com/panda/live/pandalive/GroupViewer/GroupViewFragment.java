package com.panda.live.pandalive.GroupViewer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceView;
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
import com.panda.live.pandalive.Utils.Constant;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.Utils.Settings;
import com.panda.live.pandalive.data.model.DataRoom;
import com.panda.live.pandalive.data.model.GroupBroadcast;
import com.panda.live.pandalive.data.model.IrisModel;

import java.security.acl.Group;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupViewFragment extends Fragment implements Broadcaster.ViewerCountObserver {
    private static Broadcaster mBroadcaster;
    private SurfaceViewWithAutoAR mPreviewSurfaceView;
    private Display mDefaultDisplay;
    private static Settings mSetting;
    private Command mCommand;
    private static Context mContext;

    private HashMap<String, GroupBroadcast> listMembers = new HashMap<>();

    // Fire base
    private DatabaseReference mDatabase;

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.e("TAG", "Received status change: " + broadcastStatus);
            switch (broadcastStatus) {
                case FINISHING:

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

    public GroupViewFragment() {
        // Required empty public constructor
    }

    public static GroupViewFragment newInstance() {
        GroupViewFragment fragment = new GroupViewFragment();
        return fragment;
    }

    public static void switchCamera() {
        if (mBroadcaster.canSwitchCameraWithoutResolutionChange()) {
            mBroadcaster.switchCamera();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        View itemView = inflater.inflate(R.layout.fragment_group_view, container, false);
        mPreviewSurfaceView = itemView.findViewById(R.id.PreviewSurfaceView);
        mBroadcaster = new Broadcaster(this.getActivity(), PreferencesManager.APPLICATION_ID, this.mBroadcasterObserver);
//        //Handle list Member
//        listMembers.put("1",new GroupBroadcast((SurfaceView) itemView.findViewById(R.id.VideoSurfaceView_user_1),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",this.getActivity()));
//        listMembers.put("2",new GroupBroadcast((SurfaceView) itemView.findViewById(R.id.VideoSurfaceView_user_2),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",this.getActivity()));
//        listMembers.put("3",new GroupBroadcast((SurfaceView) itemView.findViewById(R.id.VideoSurfaceView_user_3),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",this.getActivity()));
//        listMembers.put("4",new GroupBroadcast((SurfaceView) itemView.findViewById(R.id.VideoSurfaceView_user_4),"https://cdn.bambuser.net/broadcasts/81c8d72b-99ce-4d36-8f35-51fc6b156657?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524208852&da_static=1&da_ttl=0&da_signature=01d83ae8df50bd5761b6db1cca8ba51b4d86ebb251893b1ae5dbfd70f7aec363",this.getActivity()));
//        listMembers.put("5",null);
//        listMembers.put("6",null);
//        listMembers.put("7",new GroupBroadcast((SurfaceView) itemView.findViewById(R.id.VideoSurfaceView_user_7),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",this.getActivity()));
//        listMembers.put("8",new GroupBroadcast((SurfaceView) itemView.findViewById(R.id.VideoSurfaceView_user_8),"https://cdn.bambuser.net/broadcasts/27674b93-1fdb-467b-b138-48b0779db9dd?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524208864&da_static=1&da_ttl=0&da_signature=2b358820e394444cf9823396b61aa130e66fccdeff2668862138acdd4796c035",this.getActivity()));
        return itemView;
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
        mBroadcaster.setResolution(300, 300);
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

    public static void setData() {
        mBroadcaster.setAuthor(mSetting.getAuthor());
        mBroadcaster.setTitle(mSetting.getLiveTitle());
        startBroadcaster();
    }

    private static void startBroadcaster() {
        if (mBroadcaster.canStartBroadcasting()) {
            mBroadcaster.startBroadcast();
        } else {
            Toast.makeText(mContext, "Broadcaster chưa sẵn sàng", Toast.LENGTH_SHORT).show();
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
                            values.put("pwdRoom", Constant.CHANNEL_PASSWORD);
                            values.put("title", mSetting.getLiveTitle());

                            DataRoom dataRoom = new DataRoom(
                                    Constant.CHANNEL,
                                    model.getResults().get(i).getPreview(),
                                    model.getResults().get(i).getResourceUri());
                            values.put("data", dataRoom);

                            //mDatabase.child("RoomsOnline").push().setValue(values);
                            mDatabase.child("VideosOffline").child(PreferencesManager.getID(mContext)).push().setValue(values);
                            GroupInteractionFragment.sendPositionLive(model.getResults().get(i).getResourceUri());
                        } else {
                            Log.e("TAG", "Not ok");
                        }
                    }
                } else {
                    Log.e("TAG", "not OK");
                }
            }

            @Override
            public void onFailure(Call<IrisModel> call, Throwable t) {
                Log.e("TAG", "Not ok" + t);
            }
        });
    }

//    private void removeBroadCast() {
//        Query idRoomQuery = mDatabase.child("RoomsOnline").orderByChild("idRoom").equalTo(PreferencesManager.getID(mContext));
//
//        idRoomQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot idRoomSnapshot : dataSnapshot.getChildren()) {
//                    idRoomSnapshot.getRef().removeValue();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("TAG", "onCancelled", databaseError.toException());
//            }
//        });
//    }

    @Override
    public void onCurrentViewersUpdated(long l) {
        Log.e("TAG", "current viewer " + String.valueOf(l));
    }

    @Override
    public void onTotalViewersUpdated(long l) {
        Log.e("TAG", " Total Viewer " + String.valueOf(l));
    }


}
