package com.panda.live.pandalive.LiveGoup;

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
import com.google.firebase.database.ChildEventListener;
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
import com.panda.live.pandalive.data.model.PandaModel;
import com.panda.live.pandalive.data.model.PositonGroupModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveGroupViewFragment extends Fragment implements Broadcaster.ViewerCountObserver {
    private static String TAG = LiveGroupViewFragment.class.getName();
    private View mView;
    private static Broadcaster mBroadcaster;
    private SurfaceViewWithAutoAR mPreviewSurfaceView;
    private SurfaceViewWithAutoAR mVideoSurfaceView_1;
    private SurfaceViewWithAutoAR mVideoSurfaceView_2;
    private SurfaceViewWithAutoAR mVideoSurfaceView_3;
    private SurfaceViewWithAutoAR mVideoSurfaceView_4;
    private SurfaceViewWithAutoAR mVideoSurfaceView_5;
    private SurfaceViewWithAutoAR mVideoSurfaceView_6;
    private SurfaceViewWithAutoAR mVideoSurfaceView_7;
    private SurfaceViewWithAutoAR mVideoSurfaceView_8;

    private Display mDefaultDisplay;
    private Settings mSetting;
    private Command mCommand;
    private Context mContext;
    private int mPosition = 0, mCount = 0;
    private int[] mPositionArr = new int [7];

    private HashMap<String, GroupBroadcast> listMembers = new HashMap<>();

    // Fire base
    private DatabaseReference mDatabase;

    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.e("TAG", "Received status change: " + broadcastStatus);
            switch (broadcastStatus) {
                case FINISHING:
                    removeBroadCast();
                    removePositionGroup();
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
            retrievePositionGroup();
        }
    };

    public LiveGroupViewFragment() {
        // Required empty public constructor
    }

    public static LiveGroupViewFragment newInstance() {
        LiveGroupViewFragment fragment = new LiveGroupViewFragment();
        return fragment;
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

        View itemView = inflater.inflate(R.layout.fragment_live_group_view, container, false);
        mView = itemView;
        mPreviewSurfaceView = itemView.findViewById(R.id.PreviewSurfaceView);
        mBroadcaster = new Broadcaster(this.getActivity(), PreferencesManager.APPLICATION_ID, this.mBroadcasterObserver);
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
        mPreviewSurfaceView.setVisibility(View.VISIBLE);
        mPreviewSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
        mBroadcaster.setCameraSurface(mPreviewSurfaceView);
        mBroadcaster.setAudioQuality(Broadcaster.AudioSetting.HIGH_QUALITY);
        mBroadcaster.setMaxLiveResolution(1080, 1920);
        mBroadcaster.setResolution(300,300);
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
                            values.put("pwdRoom", Constant.CHANNEL_PASSWORD);
                            values.put("title",mSetting.getLiveTitle());

                            DataRoom dataRoom = new DataRoom(
                                    Constant.CHANNEL,
                                    model.getResults().get(i).getPreview(),
                                    model.getResults().get(i).getResourceUri());
                            values.put("data", dataRoom);

                            PositonGroupModel positonGroupModel = new PositonGroupModel();

                            positonGroupModel.setPosition("1");
                            positonGroupModel.setResourceUrl(model.getResults().get(i).getResourceUri());

                            mDatabase.child("RoomsOnline").push().setValue(values);
                            mDatabase.child("VideosOffline").child(PreferencesManager.getID(mContext)).push().setValue(values);
                            mDatabase.child("PositionGroup").child(PreferencesManager.getID(mContext)).child(PreferencesManager.getID(mContext)).setValue(positonGroupModel);
                        }
                    }
                } else {
                    Log.e(TAG,"Response not successful");
                }
            }

            @Override
            public void onFailure(Call<IrisModel> call, Throwable t) {
                Log.e("TAG", "Not ok" + t);
            }
        });
    }

    private void removeBroadCast(){
        final Query id =  mDatabase.child("RoomsOnline")
                .orderByChild("idRoom").equalTo(PreferencesManager.getID(mContext));

        id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child("RoomsOnline").child(dataSnapshot.getKey()).removeValue();
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

    public static void switchCamera(){
        if(mBroadcaster.canSwitchCameraWithoutResolutionChange()){
            mBroadcaster.switchCamera();
        }
    }

    public void retrievePositionGroup() {
        mDatabase.child("PositionGroup").child(PreferencesManager.getID(mContext)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 1) return;

                //Get infor
                //for (DataSnapshot data : dataSnapsho));

                bindVideosOnView((int) dataSnapshot.getChildrenCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,databaseError.getMessage());
            }
        });
    }

    private void bindVideosOnView(int position){
        switch (position){
            case 1:
                mVideoSurfaceView_1 = mView.findViewById(R.id.VideoSurfaceView_user_1);
                mVideoSurfaceView_1.setVisibility(View.VISIBLE);
                listMembers.put(String.valueOf(position),new GroupBroadcast(mVideoSurfaceView_1,"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",mContext));
                break;
            case 2:
                mVideoSurfaceView_2 = mView.findViewById(R.id.VideoSurfaceView_user_2);
                mVideoSurfaceView_2.setVisibility(View.VISIBLE);
                listMembers.put(String.valueOf(position),new GroupBroadcast((SurfaceView) mView.findViewById(R.id.VideoSurfaceView_user_2),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",mContext));
                break;
            case 3:
                mVideoSurfaceView_3 = mView.findViewById(R.id.VideoSurfaceView_user_3);
                mVideoSurfaceView_3.setVisibility(View.VISIBLE);
                listMembers.put(String.valueOf(position),new GroupBroadcast((SurfaceView) mView.findViewById(R.id.VideoSurfaceView_user_3),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",mContext));
                break;
            case 4:
                mVideoSurfaceView_4 = mView.findViewById(R.id.VideoSurfaceView_user_4);
                mVideoSurfaceView_4.setVisibility(View.VISIBLE);
                listMembers.put(String.valueOf(position),new GroupBroadcast((SurfaceView) mView.findViewById(R.id.VideoSurfaceView_user_4),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",mContext));
                break;
            case 5:
                mVideoSurfaceView_5 = mView.findViewById(R.id.VideoSurfaceView_user_5);
                mVideoSurfaceView_5.setVisibility(View.VISIBLE);
                listMembers.put(String.valueOf(position),new GroupBroadcast((SurfaceView) mView.findViewById(R.id.VideoSurfaceView_user_5),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",mContext));
                break;
            case 6:
                mVideoSurfaceView_6 = mView.findViewById(R.id.VideoSurfaceView_user_6);
                mVideoSurfaceView_6.setVisibility(View.VISIBLE);
                listMembers.put(String.valueOf(position),new GroupBroadcast((SurfaceView) mView.findViewById(R.id.VideoSurfaceView_user_6),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",mContext));
                break;
            case 7:
                mVideoSurfaceView_7 = mView.findViewById(R.id.VideoSurfaceView_user_7);
                mVideoSurfaceView_7.setVisibility(View.VISIBLE);
                listMembers.put(String.valueOf(position),new GroupBroadcast((SurfaceView) mView.findViewById(R.id.VideoSurfaceView_user_7),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",mContext));
                break;
            case 8:
                mVideoSurfaceView_8 = mView.findViewById(R.id.VideoSurfaceView_user_8);
                mVideoSurfaceView_8.setVisibility(View.VISIBLE);
                listMembers.put(String.valueOf(position),new GroupBroadcast((SurfaceView) mView.findViewById(R.id.VideoSurfaceView_user_8),"https://cdn.bambuser.net/broadcasts/13fe5714-cf72-48e4-85b1-9339cb9aa5fe?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1524189180&da_static=1&da_ttl=0&da_signature=8796f4168938a4afc1463df332daeb83b28842ed23cb596cc0dc11753fe24cb0",mContext));
                break;
        }

    }

    private void removePositionGroup(){
        final Query id =  mDatabase.child("PositionGroup")
                .child(PreferencesManager.getID(mContext)).orderByChild("id").equalTo(PreferencesManager.getID(mContext));

        id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child("PositionGroup").child(dataSnapshot.getKey()).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }

}
