package com.panda.live.pandalive.data.adapter;

/**
 * Created by levan on 30/03/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.RankModel;

import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<RankModel> dataSet;

    public RankAdapter(Context mContext, ArrayList<RankModel> data) {
        this.mContext = mContext;
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_rank, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int listPosition) {
        holder.textViewNumeric.setText(String.valueOf(listPosition+4));
        Glide.with(mContext).load(dataSet.get(listPosition).getUri()).into(holder.imageView);
        holder.textViewName.setText(dataSet.get(listPosition).getName());
        holder.textViewCoin.setText(String.valueOf(dataSet.get(listPosition).getCoin()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumeric;
        RoundedImageView imageView;
        TextView textViewName;
        TextView textViewCoin;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewNumeric = itemView.findViewById(R.id.tv_numberic);
            this.imageView = itemView.findViewById(R.id.imgAvatar);
            this.textViewName = itemView.findViewById(R.id.tv_name_rec);
            this.textViewCoin = itemView.findViewById(R.id.tv_coin);

        }
    }
}