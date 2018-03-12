package com.panda.live.pandalive.Home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.Utils.ViewFindUtils;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    //Tab
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles_3 = {"Kênh","Panda","Khám phá"};
    private View mDecorView;
    private SegmentTabLayout mTabLayout_3;

    //Bottom Navigation
    private SpaceNavigationView mSpaceNavigationView;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        for( int i = 0; i < 3; i++){
            if (i == 0){
                mFragments.add(ChannelsFragment.newInstance("",""));
            }else {
                if ( i == 1){
                    mFragments.add(PandaFragment.newInstance("",""));
                } else {
                    mFragments.add(SimpleCardFragment.getInstance("Switch ViewPager " + mTitles_3[i].toString()));
                }
            }
        }

        mDecorView = getWindow().getDecorView();

        mTabLayout_3 = ViewFindUtils.find(mDecorView, R.id.tl_3);

        tl_3();

        //Hiển thị chấm đỏ chưa đọc
        mTabLayout_3.showDot(1);

//        MsgView rtv_3_2 = mTabLayout_3.getMsgView(2);
//        if (rtv_3_2 != null) {
//            rtv_3_2.setBackgroundColor(Color.parseColor("#6D8FB0"));
//        }

        configBottomNavigation(savedInstanceState);
    }

    private void configBottomNavigation(Bundle savedInstanceState) {
        mSpaceNavigationView = (SpaceNavigationView) findViewById(R.id.nv_bottom);

        mSpaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        mSpaceNavigationView.showIconOnly();
        mSpaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_live_tv_black_24dp));
        mSpaceNavigationView.addSpaceItem(new SpaceItem("SEARCH", R.drawable.ic_perm_identity_black_24dp));
        mSpaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
    }

    private void tl_3() {
        final ViewPager vp_3 = ViewFindUtils.find(mDecorView, R.id.vp_2);
        vp_3.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mTabLayout_3.setTabData(mTitles_3);
        mTabLayout_3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp_3.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        vp_3.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_3.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_3.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles_3[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
