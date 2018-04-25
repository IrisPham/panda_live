package com.panda.live.pandalive.data.adapter;

/**
 * Created by levan on 30/03/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.DataChat;
import com.panda.live.pandalive.data.model.RankModel;

import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyViewHolder> {

    private ArrayList<RankModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumeric;
        TextView textViewName;
        TextView textViewCoin;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewNumeric = itemView.findViewById(R.id.tv_numberic);
            this.textViewName =  itemView.findViewById(R.id.tv_name_rec);
            this.textViewCoin =  itemView.findViewById(R.id.tv_coin);

        }
    }

    public RankAdapter(ArrayList<RankModel> data) {
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
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView textViewNumeric = holder.textViewNumeric;
        TextView textViewName = holder.textViewName;
        TextView textViewCoin = holder.textViewCoin;

        textViewNumeric.setText(dataSet.get(listPosition).getNumeric());
        textViewName.setText(dataSet.get(listPosition).getName());
        textViewCoin.setText(dataSet.get(listPosition).getCoin()+"");

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}