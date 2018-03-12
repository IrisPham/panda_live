package com.panda.live.pandalive.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.ChannelModel;
import com.panda.live.pandalive.data.model.PandaModel;

import java.util.ArrayList;

/**
 * Created by Android Studio on 3/12/2018.
 */

public class PandaAdapter extends RecyclerView.Adapter<PandaAdapter.MyViewHolder> {
    private ArrayList<PandaModel> pandaModels;
    private Context mContext;

    public PandaAdapter(ArrayList<PandaModel> pandaModels, Context mContext) {
        this.pandaModels = pandaModels;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_panda,parent,false);
        return new PandaAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

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
        }
    }
}
