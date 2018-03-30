package com.panda.live.pandalive.StreamManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.panda.live.pandalive.LiveSingle.LiveSingleActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.Utils.Settings;

/**
 * Created by Android Studio on 2/6/2018.
 */

public class LiveManagerInteractionFragment extends Fragment implements View.OnClickListener{
    private View mView;
    private EditText mEdtTitle;
    private boolean isGroup = false;
    private Settings mSetting;

    public LiveManagerInteractionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSetting = Settings.getInstance(this.getContext());
        mSetting.setAuthor(PreferencesManager.getID(this.getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live_manager_interaction, container, false);
            mEdtTitle = mView.findViewById(R.id.edt_title_live);
            mView.findViewById(R.id.btn_start_live).setOnClickListener(this);
            mView.findViewById(R.id.layout_live_group).setOnClickListener(this);
            mView.findViewById(R.id.layout_live_single).setOnClickListener(this);

            mEdtTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s != null ) mSetting.setLiveTitle(s.toString().trim());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
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
        if (isGroup) {
            startActivity(new Intent(this.getActivity(), LiveSingleActivity.class));
        } else {
            //startActivity(new Intent(this.getActivity(), L.class));
        }
    }
}
