//package com.panda.live.pandalive.cache;
//
///**
// * Created by Android Studio on 3/27/2018.
// */
//
////public class TheProvincial {
////}
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//
//import com.flyco.tablayout.SegmentTabLayout;
//import com.flyco.tablayout.listener.OnTabSelectListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.luseen.spacenavigation.SpaceItem;
//import com.luseen.spacenavigation.SpaceNavigationView;
//import com.luseen.spacenavigation.SpaceOnClickListener;
//import com.panda.live.pandalive.Home.ChannelsFragment;
//import com.panda.live.pandalive.Home.PandaFragment;
//import com.panda.live.pandalive.Home.SimpleCardFragment;
//import com.panda.live.pandalive.R;
//import com.panda.live.pandalive.Utils.ViewFindUtils;
//import com.panda.live.pandalive.profile.ProfileActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;package com.panda.live.pandalive.Home;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.widget.Toast;
//
//import com.facebook.AccessToken;
//import com.flyco.tablayout.SegmentTabLayout;
//import com.flyco.tablayout.listener.OnTabSelectListener;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FacebookAuthProvider;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.luseen.spacenavigation.SpaceItem;
//import com.luseen.spacenavigation.SpaceNavigationView;
//import com.luseen.spacenavigation.SpaceOnClickListener;
//import com.panda.live.pandalive.R;
//import com.panda.live.pandalive.Utils.ViewFindUtils;
//import com.panda.live.pandalive.profile.ProfileActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.util.ArrayList;
//
//public class HomeActivity extends AppCompatActivity {
//
//    //Tab
//    private ArrayList<Fragment> mFragments = new ArrayList<>();
//    private String[] mTitles_3 = {"Kênh", "Panda", "Khám phá"};
//    private View mDecorView;
//    private SegmentTabLayout mTabLayout_3;
//
//    //Bottom Navigation
//    private SpaceNavigationView mSpaceNavigationView;
//
//    private Toolbar mToolbar;
//
//    // Fire base
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//        mToolbar = findViewById(R.id.toolbar);
//
//        setSupportActionBar(mToolbar);
//
//        for (int i = 0; i < 3; i++) {
//            if (i == 0) {
//                mFragments.add(ChannelsFragment.newInstance("", ""));
//            } else {
//                if (i == 1) {
//                    mFragments.add(PandaFragment.newInstance("", ""));
//                } else {
//                    mFragments.add(SimpleCardFragment.getInstance("Switch ViewPager " + mTitles_3[i].toString()));
//                }
//            }
//        }
//
//        mDecorView = getWindow().getDecorView();
//
//        mTabLayout_3 = ViewFindUtils.find(mDecorView, R.id.tl_3);
//
//        tl_3();
//
//        //Hiển thị chấm đỏ chưa đọc
//        mTabLayout_3.showDot(1);
//
////        MsgView rtv_3_2 = mTabLayout_3.getMsgView(2);
////        if (rtv_3_2 != null) {
////            rtv_3_2.setBackgroundColor(Color.parseColor("#6D8FB0"));
////        }
//
//        configBottomNavigation(savedInstanceState);
//        configFirebase();
//        setDataProvincial();
//    }
//    private void configFirebase(){
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//    }
//
//    private void configBottomNavigation(Bundle savedInstanceState) {
//        mSpaceNavigationView = (SpaceNavigationView) findViewById(R.id.nv_bottom);
//
//        mSpaceNavigationView.initWithSaveInstanceState(savedInstanceState);
//        mSpaceNavigationView.showIconOnly();
//        mSpaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_live_tv_black_24dp));
//        mSpaceNavigationView.addSpaceItem(new SpaceItem("SEARCH", R.drawable.ic_perm_identity_black_24dp));
//        mSpaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
//        mSpaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
//            @Override
//            public void onCentreButtonClick() {
//
//            }
//
//            @Override
//            public void onItemClick(int itemIndex, String itemName) {
//                switch (itemIndex) {
//                    case 0:
//                        break;
//                    case 1:
//                        startActivity(new Intent(com.panda.live.pandalive.Home.HomeActivity.this, ProfileActivity.class));
//                        finish();
//                        break;
//                }
//            }
//
//            @Override
//            public void onItemReselected(int itemIndex, String itemName) {
//            }
//        });
//    }
//
//    private void tl_3() {
//        final ViewPager vp_3 = ViewFindUtils.find(mDecorView, R.id.vp_2);
//        vp_3.setAdapter(new com.panda.live.pandalive.Home.HomeActivity.MyPagerAdapter(getSupportFragmentManager()));
//
//        mTabLayout_3.setTabData(mTitles_3);
//        mTabLayout_3.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                vp_3.setCurrentItem(position);
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//            }
//        });
//
//        vp_3.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                mTabLayout_3.setCurrentTab(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        vp_3.setCurrentItem(1);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    private void setDataProvincial() {
//        setDataPrefix();
//    }
//
//    private void setDataPrefix(){
//        try {
//            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
//            com.panda.live.pandalive.Home.HomeActivity.provincial provincial = new com.panda.live.pandalive.Home.HomeActivity.provincial();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject object = (JSONObject) jsonArray.get(i);
//                provincial.setName(object.getString("name"));
//                provincial.setSlug(object.getString("slug"));
//                provincial.setType(object.getString("type"));
//                provincial.setName_with_type(object.getString("name_with_type"));
//                provincial.setCode(object.getString("code"));
//                mDatabase.child("TheProvincial").push().setValue(provincial);
//                Log.e("TAG", i + " ok ");
//            }
//
//        } catch (JSONException e) {
//            Log.i("LOGCAT", e.getMessage());
//        }
//    }
//    class provincial{
//        String name;
//        String slug;
//        String type;
//        String name_with_type;
//        String code;
//
//        public provincial() {
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getSlug() {
//            return slug;
//        }
//
//        public void setSlug(String slug) {
//            this.slug = slug;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getName_with_type() {
//            return name_with_type;
//        }
//
//        public void setName_with_type(String name_with_type) {
//            this.name_with_type = name_with_type;
//        }
//
//        public String getCode() {
//            return code;
//        }
//
//        public void setCode(String code) {
//            this.code = code;
//        }
//    }
//
//    private String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = getAssets().open("tree.json");
//
//            int size = is.available();
//
//            byte[] buffer = new byte[size];
//
//            is.read(buffer);
//
//            is.close();
//
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            Log.e("TAG", ex.getMessage());
//            return null;
//        }
//        return json;
//    }
//
////    private void saveFacebookCredentialsInFirebase(AccessToken accessToken) {
////        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
////        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
////            @Override
////            public void onComplete(@NonNull Task<AuthResult> task) {
////                if (!task.isSuccessful()) {
////                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
////                } else {
////                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
////                }
////            }
////        });
////    }
////
////    private void saveGoogleCredentialsInFirebase(GoogleSignInAccount acct) {
////
////        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
////        mAuth.signInWithCredential(credential)
////                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
////                    @Override
////                    public void onComplete(@NonNull Task<AuthResult> task) {
////                        if (task.isSuccessful()) {
////                            Toast.makeText(getApplicationContext(), "Đăng nhập thật bại", Toast.LENGTH_LONG).show();
////                        } else {
////                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
////                        }
////                    }
////                });
////    }
//
//
//    private class MyPagerAdapter extends FragmentPagerAdapter {
//        public MyPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragments.size();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mTitles_3[position];
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragments.get(position);
//        }
//    }
//}


//Channels
//private void setDataPrefix(){
//        try {
//        JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
//        Channels channels = new Channels();
//        String[] name = new String[4];
//        name[0] = "Học tập";
//        name[1] = "Âm nhạc";
//        name[2] = " Phim";
//        name[3] = "Tán ngẫu";
//        for (int i = 0; i < 4; i++) {
////                provincial.setName(object.getString("name"));
////                provincial.setSlug(object.getString("slug"));
////                provincial.setType(object.getString("type"));
////                provincial.setName_with_type(object.getString("name_with_type"));
////                provincial.setCode(object.getString("code"));
//
//        channels.setId(String.valueOf(i));
//        channels.setName(name[i]);
//        mDatabase.child("Channels").push().setValue(channels);
//        Log.e("TAG", i + " ok ");
//        }
//
//        } catch (JSONException e) {
//        Log.i("LOGCAT", e.getMessage());
//        }
//        }
//class Channels{
//    String id;
//    String name;
//
//    public Channels() {
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}