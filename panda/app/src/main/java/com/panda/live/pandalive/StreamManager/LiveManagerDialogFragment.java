package com.panda.live.pandalive.StreamManager;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.panda.live.pandalive.LiveViewer.NonInteractionFragment;
import com.panda.live.pandalive.R;

/**
 * Created by Android Studio on 2/6/2018.
 */

public class LiveManagerDialogFragment extends DialogFragment {
    private ViewPager mViewpager;
    private LiveManagerInteractionFragment mInteractionFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_live_manager_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewpager = view.findViewById(R.id.viewpager_user_interactive);

        mViewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return mInteractionFragment = new LiveManagerInteractionFragment(); /*Quay lại phần giao diện tương tác*/
                }
                return null;
            }

            @Override
            public int getCount() {
                return 1; //Chỉ có 2 layer
            }
        });
        mViewpager.addOnPageChangeListener(new InteractionOnPageChangeListener());
        mViewpager.setCurrentItem(1); //Bắt đầu sẽ xem ở giao diện tương tác.

        /*Đồng thời thay đổi giao diện để thay đổi kích cỡ đã đạt đến bàn phím mềm
        pop-up LiveFragment sẽ không làm theo các di chuyển */
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

//    /*
//    * 设置MainDialogFragment的样式，这里的代码最好还是用我的，大家不要改动
//    * */
//    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.MainDialog){
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                getActivity().finish();
            }
        };
        return dialog;
    }

    /*
        * Inner class này dùng để theo dõi sự kiện khi người dùng muốn xem live stream mà không
        * cần xem các tương tác của người dùng khác
        * Hoặc ngược lại.
        * */
    private class InteractionOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position==0){
               // mInteractionFragment.hideKeyboard();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
