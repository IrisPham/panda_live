package com.panda.live.pandalive.data.adapter;

/**
 * Created by levan on 04/05/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.cache.ItemClickListener;
import com.panda.live.pandalive.data.model.GiftModel;
import com.panda.live.pandalive.data.model.RankModel;

import java.util.ArrayList;

/**
 * Created by levan on 30/03/2018.
 */


public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.MyViewHolder> {
    public Context mContext;
    private ArrayList<GiftModel> dataSet;
    public static int value = 0;


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewName;
        TextView textViewCoin;
        public RippleView rippleView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imv_gift);
            this.textViewName = itemView.findViewById(R.id.tv_name_gift);
            this.textViewCoin = itemView.findViewById(R.id.tv_coin_gift);
            this.rippleView = itemView.findViewById(R.id.rippleView);
        }
    }

    public GiftAdapter(Context mContext, ArrayList<GiftModel> data) {
        this.mContext = mContext;
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_gift, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.imageView.setImageDrawable(mContext.getResources().getDrawable(dataSet.get(listPosition).getImageGift()));
        holder.textViewName.setText(dataSet.get(listPosition).getNameGift());
        holder.textViewCoin.setText(dataSet.get(listPosition).getCoinGift() + "");


        holder.rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = Integer.parseInt(holder.textViewCoin.getText().toString());
            }
        });




    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}