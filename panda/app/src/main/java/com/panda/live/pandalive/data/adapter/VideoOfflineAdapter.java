package com.panda.live.pandalive.data.adapter;

/**
 * Created by levan on 19/04/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.panda.live.pandalive.LiveViewer.LiveActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.PandaModel;

import java.util.ArrayList;

/**
 * Created by levan on 19/04/2018.
 */



/**
 * Created by Android Studio on 3/12/2018.
 */

public class VideoOfflineAdapter extends RecyclerView.Adapter<VideoOfflineAdapter.MyViewHolder> {
    private ArrayList<PandaModel> pandaModels;
    private Context mContext;

    public VideoOfflineAdapter(ArrayList<PandaModel> pandaModels, Context mContext) {
        this.pandaModels = pandaModels;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_videoffline,parent,false);
        return new VideoOfflineAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mContext).load(pandaModels.get(position).getData().getAvatarLink()).into(holder.mImvIconChannel);
    }

    @Override
    public int getItemCount() {
        return pandaModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImvIconChannel;
        private TextView mTvNameChannel;
        public MyViewHolder(View itemView) {
            super(itemView);
            mImvIconChannel = itemView.findViewById(R.id.imv_item_channel);
            mImvIconChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LiveActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("URL",pandaModels.get(getAdapterPosition()).getData().getResourceUri());
                    intent.putExtra("idRoom",pandaModels.get(getAdapterPosition()).getIdRoom());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

