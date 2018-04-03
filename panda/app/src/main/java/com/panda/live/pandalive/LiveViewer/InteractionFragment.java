package com.panda.live.pandalive.LiveViewer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.CustomRoundView;
import com.panda.live.pandalive.Utils.HorizontalListView;
import com.panda.live.pandalive.Utils.MagicTextView;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.adapter.ChatAdapter;
import com.panda.live.pandalive.data.model.DataChat;
import com.panda.live.pandalive.data.model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Android Studio on 2/6/2018.
 */

public class InteractionFragment extends Fragment implements View.OnClickListener {

    private String mUrl ="";
    private String mIdRoom = "";

    private LinearLayout llpicimage;
    private RelativeLayout rlsentimenttime;
    private HorizontalListView hlvaudience;
    private TextView tvtime;
    private TextView tvdate;
    private LinearLayout llgiftcontent;
    private ListView lvmessage;
    private TextView tvSendone;
    private TextView tvSendtwo;
    private TextView tvSendthree;
    private TextView tvSendfor;
    private EditText etInput;
    private TextView tvChat;
    private TextView sendInput;
    private TextView tvName;
    private LinearLayout llInputParent;
    private EditText mMessage;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private CustomRoundView mAvatar;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private static RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DataChat> mData;
    private ChatAdapter mAdapter;


    /**
     * Khai báo các hiệu ứng
     */
    private NumAnim giftNumAnim;
    private TranslateAnimation inAnim;
    private TranslateAnimation outAnim;
    private AnimatorSet animatorSetHide = new AnimatorSet();
    private AnimatorSet animatorSetShow = new AnimatorSet();

    /**
     * Khai báo các data liên quan đến quà, bình luận,...
     */
    private List<View> giftViewCollection = new ArrayList<View>();
    private List<String> messageData = new LinkedList<>();
    private MessageAdapter messageAdapter;

    private Timer timer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InteractionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InteractionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InteractionFragment newInstance(String param1, String param2) {
        InteractionFragment fragment = new InteractionFragment();
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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interaction, container, false);
        llpicimage = (LinearLayout) view.findViewById(R.id.llpicimage);
        rlsentimenttime = (RelativeLayout) view.findViewById(R.id.rlsentimenttime);
        hlvaudience = (HorizontalListView) view.findViewById(R.id.hlvaudience);
        tvtime = (TextView) view.findViewById(R.id.tvtime);
        tvdate = (TextView) view.findViewById(R.id.tvdate);
        llgiftcontent = (LinearLayout) view.findViewById(R.id.llgiftcontent);
        lvmessage = (ListView) view.findViewById(R.id.lvmessage);
        tvChat = (TextView) view.findViewById(R.id.tvChat);
        tvSendone = (TextView) view.findViewById(R.id.tvSendone);
        tvSendtwo = (TextView) view.findViewById(R.id.tvSendtwo);
        tvSendthree = (TextView) view.findViewById(R.id.tvSendthree);
        tvSendfor = (TextView) view.findViewById(R.id.tvSendfor);
        tvName = view.findViewById(R.id.tv_name);
        llInputParent = (LinearLayout) view.findViewById(R.id.llinputparent);
        etInput = (EditText) view.findViewById(R.id.etInput);
        sendInput = (TextView) view.findViewById(R.id.sendInput);
        mAvatar = view.findViewById(R.id.imgAvatar);
        mMessage = view.findViewById(R.id.etInput);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new ChatAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
        mRecyclerView.setHasFixedSize(true);

        giftNumAnim = new NumAnim();
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_out);

        tvChat.setOnClickListener(this);
        tvSendone.setOnClickListener(this);
        tvSendtwo.setOnClickListener(this);
        tvSendthree.setOnClickListener(this);
        tvSendfor.setOnClickListener(this);
        sendInput.setOnClickListener(this);
        clearTiming();
        binData();
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChat: // Bình luận
                showChat();
                break;
            case R.id.tvSendone: // Gửi quà tặng
                showGift("Lê Văn Ngà");
                break;
            case R.id.tvSendtwo:
                showGift("Huỳnh Trọng thành");
                break;
            case R.id.tvSendthree:
                showGift("Nguyễn Đình Trọng");
                break;
            case R.id.tvSendfor:
                showGift("Nguyễn Văn Lộc");
                break;
            case R.id.sendInput:/*Gửi bình luận*/
                sendText();
                break;
        }

    }

    /**
     * Hiển thị khung bình luận
     */
    private void showChat() {
        tvChat.setVisibility(View.GONE);
        llInputParent.setVisibility(View.VISIBLE);
        llInputParent.requestFocus();
        //showKeyboard();
    }

    /**
     * Gửi bình luận
     */
    private void sendText() {
//        if(!etInput.getText().toString().trim().isEmpty()){
//            messageData.add("Johnny: "+etInput.getText().toString().trim());
//            etInput.setText("");
//            messageAdapter.NotifyAdapter(messageData);
//            lvmessage.setSelection(messageData.size());
//            hideKeyboard();
//        }else
//            hideKeyboard();
        sendMessage(mMessage.getText().toString());
        mMessage.setText("");
    }

//    /**
//     * Hiển thị bàn phím mềm và do đó bố trí đầu
//     */
//    private void showKeyboard() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                InputMethodManager imm = (InputMethodManager)
//                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etInput, InputMethodManager.SHOW_FORCED);
//            }
//        }, 100);
//    }

    /**
     * Hiển thị quà tặng lên màn hình
     */
    private void showGift(String tag) {
        View giftView = llgiftcontent.findViewWithTag(tag);
        if (giftView == null) {/*Người dùng không có tên trong danh sách quà tặng*/

            /*Nếu số lượng quà tặng đang được trưng bày nhiều hơn hai lần thì thời gian cập nhật cuối cùng sẽ được gỡ bỏ*/
            if (llgiftcontent.getChildCount() > 2) {
                View giftView1 = llgiftcontent.getChildAt(0);
                CustomRoundView picTv1 = (CustomRoundView) giftView1.findViewById(R.id.crvheadimage);
                long lastTime1 = (Long) picTv1.getTag();
                View giftView2 = llgiftcontent.getChildAt(1);
                CustomRoundView picTv2 = (CustomRoundView) giftView2.findViewById(R.id.crvheadimage);
                long lastTime2 = (Long) picTv2.getTag();
                if (lastTime1 > lastTime2) {/*Nếu Chế độ xem thứ hai hiển thị một thời gian dài hơn*/
                    removeGiftView(1);
                } else {/*Nếu Chế độ xem đầu tiên hiển thị một thời gian dài*/
                    removeGiftView(0);
                }
            }

            giftView = addGiftView();/*Chế độ xem bố cục của quà tặng*/
            giftView.setTag(tag);/*Đặt ID chế độ xem*/

            CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);
            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*Tìm số điều khiển*/
            giftNum.setText("x1");/*Đặt số lượng quà tặng*/
            crvheadimage.setTag(System.currentTimeMillis());/*Đặt tem thời gian*/
            giftNum.setTag(1);/*Đặt một lá cờ cho việc kiểm soát số*/

            llgiftcontent.addView(giftView);/*Thêm Chế độ xem quà tặng vào ViewGroup của quà tặng*/
            llgiftcontent.invalidate();/*Làm mới chế độ xem*/
            giftView.startAnimation(inAnim);/*Bắt đầu thực hiện hoạt hình hiển thị món quà*/
            inAnim.setAnimationListener(new Animation.AnimationListener() {/*Hiển thị màn hình hoạt ảnh*/
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumAnim.start(giftNum);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {/*Người dùng nằm trong danh sách hiển thị quà tặng*/
            CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);/*Tìm các điều khiển hình đại diện*/
            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*Tìm số điều khiển*/
            int showNum = (Integer) giftNum.getTag() + 1;
            giftNum.setText("x" + showNum);
            giftNum.setTag(showNum);
            crvheadimage.setTag(System.currentTimeMillis());
            giftNumAnim.start(giftNum);
        }
    }

    /**
     * Thêm chế độ xem quà tặng, (xem xét việc thu gom rác)
     */
    private View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /* Nếu không có quan điểm trong việc thu gom rác thải, một */
            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            llgiftcontent.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) {
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    giftViewCollection.add(view);
                }
            });
        } else {
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }


    /**
     * Xóa chế độ xem quà tặng
     */
    private void removeGiftView(final int index) {
        final View removeView = llgiftcontent.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llgiftcontent.removeViewAt(index);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeView.startAnimation(outAnim);
            }
        });
    }

    /**
     * Thường xuyên xóa món quà
     */
    private void clearTiming() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = llgiftcontent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = llgiftcontent.getChildAt(i);
                    CustomRoundView crvheadimage = (CustomRoundView) view.findViewById(R.id.crvheadimage);
                    long nowtime = System.currentTimeMillis();
                    long upTime = (Long) crvheadimage.getTag();
                    if ((nowtime - upTime) >= 3000) {
                        removeGiftView(i);
                        return;
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 3000);
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
     * Ẩn bàn phím và bố cục ban đầu
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
    }


    /*
    * Inner class này dùng để tạo hiệu ứng số lần tặng quà từ người dùng
    * */
    public class NumAnim {
        private Animator lastAnimator = null;

        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.0f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(200);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
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


    public void sendMessage(String s) {
        DataChat datachat = new DataChat(PreferencesManager.getName(this.getContext()), s);
        if(!mIdRoom.equals("")){ mRef.child("chat").child(mIdRoom).setValue(datachat);};
    }

    public void retrieveMessage() {
        mRef.child("chat").child(mIdRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataChat datachat = dataSnapshot.getValue(DataChat.class);
                String name = datachat.name;
                String message = datachat.message;
                mData.add(new DataChat(name + ": ", message));
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
        mRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot idRoomSnapshot: dataSnapshot.getChildren()) {
                    User user = idRoomSnapshot.getValue(User.class);
                    if(user.id.equals(mIdRoom)){
                        tvName.setText(user.username);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void binData() {
        mUrl = getActivity().getIntent().getStringExtra("URL");
        mIdRoom = getActivity().getIntent().getStringExtra("idRoom");

        if (!mUrl.equals("") && !mIdRoom.equals("")){
            retrieveMessage();
            downloadImage();
        }
    }

    public void downloadImage(){
        mStorageReference.child("images/"+mIdRoom+"/avatarLive").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(getActivity()).load(uri).into(mAvatar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
