package com.panda.live.pandalive.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.panda.live.pandalive.GroupViewer.GroupDasboardActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.ChannelModel;

import java.util.ArrayList;

/**
 * Created by Android Studio on 2/1/2018.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.MyViewHolder> {
    private ArrayList<ChannelModel> channelModels;
    private Context mContext;

    public ChannelAdapter(ArrayList<ChannelModel> channelModels) {
        this.channelModels = channelModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_channel, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mImvIconChannel.setImageResource(channelModels.get(position).getmIdIcon());
    }

    @Override
    public int getItemCount() {
        return channelModels.size();
    }

    private void sendGroupName(int position, Intent intent) {
        switch (position) {
            case 0:
                intent.putExtra("CHANNEL_NAME", "Học tập");
                break;
            case 1:
                intent.putExtra("CHANNEL_NAME", "Âm nhạc");
                break;
            case 2:
                intent.putExtra("CHANNEL_NAME", "Phim");
                break;
            case 3:
                intent.putExtra("CHANNEL_NAME", "Tán Ngẫu");
                break;
            default:
                break;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImvIconChannel;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImvIconChannel = itemView.findViewById(R.id.imv_item_channel);
            mImvIconChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GroupDasboardActivity.class);
                    intent.putExtra("CHANNELID", getAdapterPosition());
                    sendGroupName(getAdapterPosition(), intent);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
