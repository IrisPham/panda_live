package com.panda.live.pandalive.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.ChannelModel;

import java.util.ArrayList;

/**
 * Created by Android Studio on 2/1/2018.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.MyViewHolder> {
    private ArrayList<ChannelModel> channelModels;

    public ChannelAdapter(ArrayList<ChannelModel> channelModels) {
        this.channelModels = channelModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_channel,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return channelModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImvIconChannel;
        private TextView mTvNameChannel;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
