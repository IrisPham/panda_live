package com.panda.live.pandalive.Home;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.Util;
import com.panda.live.pandalive.data.adapter.ChannelAdapter;
import com.panda.live.pandalive.data.adapter.PandaAdapter;
import com.panda.live.pandalive.data.model.PandaModel;

import java.util.ArrayList;

/**
 * @author Android Studio
 * @since 12/03/2018
 * 
 * */
public class PandaFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ArrayList<PandaModel> mPandaModels;
    private PandaAdapter mAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PandaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PandaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PandaFragment newInstance(String param1, String param2) {
        PandaFragment fragment = new PandaFragment();
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
        View itemView = inflater.inflate(R.layout.fragment_panda, container, false);
        mRecyclerView = itemView.findViewById(R.id.rcv_panda);
        mPandaModels = new ArrayList<>();
        mAdapter = new PandaAdapter(mPandaModels, this.getContext());

        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mPandaModels.add(new PandaModel(12,""));
        mAdapter.notifyDataSetChanged();
        Log.e("TAG","pxToDp: " + Util.convertPixelsToDp(528,this.getContext()));
        return itemView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
