package com.panda.live.pandalive.Home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.adapter.ChannelAdapter;
import com.panda.live.pandalive.data.model.ChannelModel;

import java.util.ArrayList;

public class ChannelsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ArrayList<ChannelModel> channelModels;
    private ChannelAdapter mAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChannelsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChannelsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChannelsFragment newInstance(String param1, String param2) {
        ChannelsFragment fragment = new ChannelsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_channels, container, false);
        mRecyclerView = itemView.findViewById(R.id.rcv_channel);
        channelModels = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false);
        mAdapter = new ChannelAdapter(channelModels);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        channelModels.add(new ChannelModel(12,""));
        mAdapter.notifyDataSetChanged();
        return itemView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}