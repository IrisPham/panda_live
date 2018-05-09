package com.panda.live.pandalive.data.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.panda.live.pandalive.LiveViewer.LiveActivity;
import com.panda.live.pandalive.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_panda, parent, false);
        return new PandaAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTvNameChannel.setText(pandaModels.get(position).getTitle());
        Glide.with(mContext).load(pandaModels.get(position).getData().getAvatarLink()).into(holder.mImvIconChannel);
    }

    @Override
    public int getItemCount() {
        return pandaModels.size();
    }

    private void handlePasswordGroup(int channelId,final int adapterPositon) {
        if (channelId == 0) {
            final EditText edittext = new EditText(mContext);
            edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
            final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle("Nhập mật khẩu phòng");
            alert.setView(edittext);
            alert.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast.makeText(mContext, "Tham gia phòng riêng tư cần phải có mật khẩu", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String passwordGroup = edittext.getText().toString();

                    if (passwordGroup.equals(pandaModels.get(adapterPositon).getPwdRoom()) && !passwordGroup.equals("none")) {
                        Intent intent = new Intent(mContext, LiveActivity.class);
                        intent.putExtra("URL", pandaModels.get(adapterPositon).getData().getResourceUri());
                        intent.putExtra("idRoom", pandaModels.get(adapterPositon).getIdRoom());
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "Sai mật khẩu! Nhập lại!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            alert.show();
        } else {
            Intent intent = new Intent(mContext, LiveActivity.class);
            intent.putExtra("URL", pandaModels.get(adapterPositon).getData().getResourceUri());
            intent.putExtra("idRoom", pandaModels.get(adapterPositon).getIdRoom());
            mContext.startActivity(intent);
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImvIconChannel;
        private TextView mTvNameChannel;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImvIconChannel = itemView.findViewById(R.id.imv_item_channel);
            mTvNameChannel = itemView.findViewById(R.id.tv_live_content);
            mImvIconChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int channelId = pandaModels.get(getAdapterPosition()).getData().getChannelId();
                    handlePasswordGroup(channelId, getAdapterPosition());
                }
            });
        }
    }
}
