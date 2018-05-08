package com.panda.live.pandalive.LiveViewer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.panda.live.pandalive.Login.LoginActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.CustomRoundView;
import com.panda.live.pandalive.Utils.HorizontalListView;
import com.panda.live.pandalive.Utils.MagicTextView;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.adapter.ChatAdapter;
import com.panda.live.pandalive.data.adapter.GiftAdapter;
import com.panda.live.pandalive.data.adapter.PandaAdapter;
import com.panda.live.pandalive.data.model.Data;
import com.panda.live.pandalive.data.model.DataChat;
import com.panda.live.pandalive.data.model.FollowModel;
import com.panda.live.pandalive.data.model.GiftModel;
import com.panda.live.pandalive.data.model.GiftModelFireBase;
import com.panda.live.pandalive.data.model.PandaModel;
import com.panda.live.pandalive.data.model.User;
import com.panda.live.pandalive.profile.ProfileActivity;
import com.panda.live.pandalive.profile.ProfileDetailActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lib.homhomlib.view2.DivergeView;

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
    private TextView mID;
    private LinearLayout llInputParent;
    private EditText mMessage;
    private RelativeLayout rlMain;
    private ImageView mCamera;
    private TextView mCoinIdol;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private CustomRoundView mAvatar;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private static RecyclerView mRecyclerView, mRecyclerViewGift;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DataChat> mData;
    private ChatAdapter mAdapter;
    private Intent mIntentProfileDetail;
    private ImageView mFollow;

    private BottomSheetBehavior mBottomSheetBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View mBottomSheetView;
    private Spinner mSpinner;
    private TextView mSend;
    private TextView mCoinUser;
    private ArrayList<GiftModel> mGiftModel;
    private GiftAdapter mAdapterGift;

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

    //Khai báo hiệu ứng trái tim
    private DivergeView mDivergeView;
    private ImageView mImvSentHeart;
    private ArrayList<Bitmap> mList;
    private int mIndex = 0;
    private static String mUserIdIdol = "";

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
        llgiftcontent = (LinearLayout) view.findViewById(R.id.llgiftcontent);
        lvmessage = (ListView) view.findViewById(R.id.lvmessage);
        tvChat = (TextView) view.findViewById(R.id.tvChat);
//        tvSendone = (TextView) view.findViewById(R.id.tvSendone);
//        tvSendtwo = (TextView) view.findViewById(R.id.tvSendtwo);
//        tvSendthree = (TextView) view.findViewById(R.id.tvSendthree);
//        tvSendfor = (TextView) view.findViewById(R.id.tvSendfor);
        tvName = view.findViewById(R.id.tv_name);
        llInputParent = (LinearLayout) view.findViewById(R.id.llinputparent);
        rlMain = view.findViewById(R.id.rlmain);
        etInput = (EditText) view.findViewById(R.id.etInput);
        sendInput = (TextView) view.findViewById(R.id.sendInput);
        mAvatar = view.findViewById(R.id.imgAvatar);
        mMessage = view.findViewById(R.id.etInput);
        mID = view.findViewById(R.id.tv_id);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new ChatAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
        mRecyclerView.setHasFixedSize(true);
        mCamera = view.findViewById(R.id.imv_switch_camera);
        giftNumAnim = new NumAnim();
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_in);
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_out);
        mImvSentHeart = view.findViewById(R.id.imv_heart);
        mDivergeView = view.findViewById(R.id.divergeView);
        mCoinIdol = view.findViewById(R.id.tv_coin_idol);
        mFollow = view.findViewById(R.id.imv_follow);
        mBottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_view_gifts, null);
        mBottomSheetDialog = new BottomSheetDialog(this.getContext());
        mBottomSheetDialog.setContentView(mBottomSheetView);
        mBottomSheetBehavior = BottomSheetBehavior.from((View) mBottomSheetView.getParent());
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        mSend = mBottomSheetView.findViewById(R.id.tv_send_gift);
        mCoinUser = mBottomSheetView.findViewById(R.id.tv_coin_user);
        mSpinner = mBottomSheetView.findViewById(R.id.sn_quantity_gift);
        mRecyclerViewGift = mBottomSheetView.findViewById(R.id.rcv_gift);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getContext(),
                        R.array.value_gift, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mGiftModel = new ArrayList<>();
        mAdapterGift = new GiftAdapter(this.getContext(), mGiftModel);

        mRecyclerViewGift.setLayoutManager(new GridLayoutManager(this.getContext(), 4));
        mRecyclerViewGift.setHasFixedSize(true);
        mRecyclerViewGift.setAdapter(mAdapterGift);

        mSpinner.setAdapter(adapter);
        mList = new ArrayList<>();
        tvChat.setOnClickListener(this);
//        tvSendone.setOnClickListener(this);
//        tvSendtwo.setOnClickListener(this);
//        tvSendthree.setOnClickListener(this);
//        tvSendfor.setOnClickListener(this);
        sendInput.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mCamera.setOnClickListener(this);
        mImvSentHeart.setOnClickListener(this);
        rlMain.setOnClickListener(this);
        mFollow.setOnClickListener(this);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int coinuser = Integer.parseInt(mCoinUser.getText().toString());
                int quanity = Integer.parseInt( mSpinner.getSelectedItem().toString());
                int value = mAdapterGift.value;

                if(coinuser >= (quanity*value)){
                    int coin = coinuser - quanity*value;
                    updateCoinUser(coin);
                    updateCoinIdol(quanity*value);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Xu trong tài khoản không đủ !")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        clearTiming();
        binData();
        loadGift();
        addResourceHeart();
        setStateFollow();
        return view;
    }

    public void setStateFollow(){
        mRef.child("MeFollow").child(PreferencesManager.getID(getContext())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot id : dataSnapshot.getChildren()){
                    FollowModel follow = id.getValue(FollowModel.class);
                    if (follow.getId().equals(mID.getText().toString())){
                        mFollow.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateCoinUser(int coin){
        mRef.child("users").child(PreferencesManager.getUserIdFirebase(getContext())).child("coin").setValue(coin);
    }

    public void updateCoinIdol( final int coin){
        mRef.child("users").child(mUserIdIdol).child("coin").setValue(Integer.parseInt(mCoinIdol.getText().toString()) + coin);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChat: // Bình luận
                showChat();
                break;
//            case R.id.tvSendone: // Gửi quà tặng
//                showGift("Lê Văn Ngà");
//                break;
//            case R.id.tvSendtwo:
//                showGift("Huỳnh Trọng thành");
//                break;
//            case R.id.tvSendthree:
//                showGift("Nguyễn Đình Trọng");
//                break;
//            case R.id.tvSendfor:
//                showGift("Nguyễn Văn Lộc");
//                break;
            case R.id.rlmain:
                hideKeyboard();
                break;
            case R.id.sendInput:/*Gửi bình luận*/
                sendText();
                break;
            case R.id.imgAvatar:
                transferData();
                break;
            case R.id.imv_switch_camera:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                mBottomSheetDialog.show();
                break;
            case R.id.imv_heart:
                handleHeartAnimation();
                break;
            case R.id.imv_follow:
                mFollow.setVisibility(View.GONE);
                followIdol();
                IdolFollowedById();
        }

    }

    public void followIdol(){
        mRef.child("MeFollow").child(PreferencesManager.getID(this.getContext()))
                .push().child("id").setValue(mID.getText().toString());
    }

    public void IdolFollowedById(){
        mRef.child("FollowMe").child(mID.getText().toString())
                .push().child("id").setValue(mID.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mList != null){
            mList.clear();
            mList = null;
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


    private void hideChat(){
        tvChat.setVisibility(View.VISIBLE);
        llInputParent.setVisibility(View.GONE);
        llInputParent.requestFocus();
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
        hideChat();
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
        mRef.child("chat").child(PreferencesManager.getID(getContext())).setValue(datachat);
    }


    public void retrieveMessage() {
        mRef.child("chat").child(PreferencesManager.getID(this.getContext())).addValueEventListener(new ValueEventListener() {

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
                        mID.setText(mIdRoom);
                        mCoinIdol.setText(user.coin+"");
                        mUserIdIdol = idRoomSnapshot.getKey().toString();
                    }
                    if(user.id.equals(PreferencesManager.getID(getContext()))){
                        mCoinUser.setText(user.coin+"");
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

    public void transferData(){
        mIntentProfileDetail = new Intent(getContext(), ProfileDetailActivity.class);
        mIntentProfileDetail.putExtra("id", mID.getText());
        startActivity(mIntentProfileDetail);

    }

    private void addResourceHeart() {
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_blue, null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.ic_heart_blue_x2,null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.ic_heart_green,null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.ic_heart_orange,null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.ic_heart_pink,null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_red, null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_yellow, null)).getBitmap());

        mDivergeView.post(new Runnable() {
            @Override
            public void run() {
                mDivergeView.setEndPoint(new PointF(mDivergeView.getMeasuredWidth()/2,0));
                mDivergeView.setDivergeViewProvider(new Provider());
            }
        });

        autoSendHeartAnimation();
    }

    private void autoSendHeartAnimation(){
        new Thread() {
            public void run() {
               while (true){
                   try {
                       if(mIndex == 6){
                           mIndex = 0 ;
                       }
                       mDivergeView.startDiverges(mIndex);
                       mIndex++;
                       Thread.sleep(100);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        }.start();
    }

    private void handleHeartAnimation(){
        if(mIndex == 6){
            mIndex = 0 ;
        }
        mDivergeView.startDiverges(mIndex);
        mIndex++;
    }


    private void loadGift(){
        mRef.child("gift").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot gift : dataSnapshot.getChildren()){
                    GiftModelFireBase model = gift.getValue(GiftModelFireBase.class);
                    mGiftModel.add(new GiftModel(model.getNameGift(),
                            model.getValueGift(), setImageGift(model.getNameGift())));
                    mAdapterGift.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int setImageGift(String name){
        switch(name){
            case "Cỏ ba lá":
                return R.drawable.lucky_clover;
            case "Ngọc trai":
                return R.drawable.ic_nt;
            case "Ngôi sao":
                return R.drawable.lucky_star;
            case "Kim cương":
                return R.drawable.ic_kimcuong;
            case "Nhẫn":
                return R.drawable.ic_nhan;
            case "Mũ hề":
                return R.drawable.ic_non;
            case "Lâu đài":
                return R.drawable.ic_laudai;
            case "Bơ":
                return R.drawable.ic_avocado;
        }
        return 0;
    }


    class Provider implements DivergeView.DivergeViewProvider{

        @Override
        public Bitmap getBitmap(Object obj) {
            return mList == null ? null : mList.get((int)obj);
        }
    }
}
