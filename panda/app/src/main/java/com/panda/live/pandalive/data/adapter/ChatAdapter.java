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

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private ArrayList<DataChat> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewMessage;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName =  itemView.findViewById(R.id.textName);
            this.textViewMessage =  itemView.findViewById(R.id.textMessage);

        }
    }

    public ChatAdapter(ArrayList<DataChat> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewMessage = holder.textViewMessage;


        textViewName.setText(dataSet.get(listPosition).getName());
        textViewMessage.setText(dataSet.get(listPosition).getMessage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}