package com.panda.live.pandalive.StreamManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.panda.live.pandalive.LiveSingle.LiveSingleActivity;
import com.panda.live.pandalive.LiveViewer.InteractionFragment;
import com.panda.live.pandalive.R;

/**
 * Created by Android Studio on 2/6/2018.
 */

public class LiveManagerInteractionFragment extends Fragment implements View.OnClickListener{
    private View  mView;
    private boolean isGroup = false;

    public LiveManagerInteractionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live_manager_interaction, container, false);
            mView.findViewById(R.id.btn_start_live).setOnClickListener(this);
            mView.findViewById(R.id.layout_live_group).setOnClickListener(this);
            mView.findViewById(R.id.layout_live_single).setOnClickListener(this);
        }
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_live:
                handleStartBroadCast();
                break;
            case R.id.layout_live_single:
                isGroup = false;
                break;
            case R.id.layout_live_group:
                isGroup = true;
                break;
            default:
                break;
        }
    }

    private void handleStartBroadCast() {
        if (isGroup){
            startActivity(new Intent(this.getActivity(), LiveSingleActivity.class));
        } else {
            startActivity(new Intent(this.getActivity(), LiveSingleActivity.class));
        }
    }
}
