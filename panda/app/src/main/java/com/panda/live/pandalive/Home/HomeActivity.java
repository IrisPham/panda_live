package com.panda.live.pandalive.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.StreamManager.LiveManagerActivity;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.Utils.ViewFindUtils;
import com.panda.live.pandalive.profile.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    //Tab
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles_3 = {"Kênh", "Panda"};
    private View mDecorView;
    private SegmentTabLayout mTabLayout_3;
    private String userID;
    private DatabaseReference mDatabase;
    //Bottom Navigation
    private SpaceNavigationView mSpaceNavigationView;

    private Toolbar mToolbar;

    // Fire base
    private FirebaseAuth mAuth;
    private Intent mIntentInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mToolbar = findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                mFragments.add(ChannelsFragment.newInstance("", ""));
            } else {
                if (i == 1) {
                    mFragments.add(PandaFragment.newInstance("", ""));
                }
            }
        }

        mDecorView = getWindow().getDecorView();

        mTabLayout_3 = ViewFindUtils.find(mDecorView, R.id.tl_3);

        tl_3();

        //Hiển thị chấm đỏ chưa đọc
        //mTabLayout_3.showDot(1);

//        MsgView rtv_3_2 = mTabLayout_3.getMsgView(2);
//        if (rtv_3_2 != null) {
//            rtv_3_2.setBackgroundColor(Color.parseColor("#6D8FB0"));
//        }

        configBottomNavigation(savedInstanceState);
        configFirebase();
        //setDataProvincial();

    }

    private void configFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        PreferencesManager.setUserIdFirebase(getApplicationContext(), userID);
    }

    private void configBottomNavigation(Bundle savedInstanceState) {
        mSpaceNavigationView = (SpaceNavigationView) findViewById(R.id.nv_bottom);

        mSpaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        mSpaceNavigationView.showIconOnly();
        mSpaceNavigationView.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_live_tv_black_24dp));
        mSpaceNavigationView.addSpaceItem(new SpaceItem("SEARCH", R.drawable.ic_perm_identity_black_24dp));
        mSpaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        mSpaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                startActivity(new Intent(HomeActivity.this, LiveManagerActivity.class));
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setDataProvincial() {
        setDataPrefix();
    }

    private void setDataPrefix() {
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());

            Room room = new Room();
            Data data = new Data();
            //AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            String[] reSourceUri = new String[5];
            reSourceUri[0] = "https://cdn.bambuser.net/broadcasts/af8caee9-8ecf-4474-af53-40df7f8e3d12?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1522085601&da_static=1&da_ttl=0&da_signature=0450898cdbd805277a857839c772ef5e3b40576c3aa65a9211799da2c5eb67ad";
            reSourceUri[1] = "https://cdn.bambuser.net/broadcasts/6229edf8-9d31-4c7e-b51c-bc3dbda67a0c?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1522085601&da_static=1&da_ttl=0&da_signature=1dfd48f78a680bdac3b46529e5701d7c2d6ff1249a500c264e164fd5a44d43b4";
            reSourceUri[2] = "https://cdn.bambuser.net/broadcasts/fb014038-ed73-4a1b-b016-87b050cffeae?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1522085601&da_static=1&da_ttl=0&da_signature=3bc0d1f664ae0ab62815f41e96a464f741476ce59091e34f06543f4ab55db199";
            reSourceUri[3] = "https://cdn.bambuser.net/broadcasts/3a9fdbca-3f5d-4652-88c2-a7eb672b4b22?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1522085601&da_static=1&da_ttl=0&da_signature=5fc4355f7129145b1468e617ef2f3d2852cb3c6225eb53e2b6a74ecd9d43c80e";
            reSourceUri[4] = "https://cdn.bambuser.net/broadcasts/9fafeca5-d0cf-4a92-95bc-6628932c61e8?da_signature_method=HMAC-SHA256&da_id=9e1b1e83-657d-7c83-b8e7-0b782ac9543a&da_timestamp=1522085601&da_static=1&da_ttl=0&da_signature=6860dc02a1c05c064e5d550c61b2bf530019ad973a1b6cdd9cbbee668500e82d";

            String[] avatarLink = new String[5];
            avatarLink[0] = "https://preview.bambuser.io/live/eyJyZXNvdXJjZVVyaSI6Imh0dHBzOlwvXC9jZG4uYmFtYnVzZXIubmV0XC9icm9hZGNhc3RzXC9hZjhjYWVlOS04ZWNmLTQ0NzQtYWY1My00MGRmN2Y4ZTNkMTIifQ==/preview.jpg";
            avatarLink[1] = "https://preview.bambuser.io/live/eyJyZXNvdXJjZVVyaSI6Imh0dHBzOlwvXC9jZG4uYmFtYnVzZXIubmV0XC9icm9hZGNhc3RzXC82MjI5ZWRmOC05ZDMxLTRjN2UtYjUxYy1iYzNkYmRhNjdhMGMifQ==/preview.jpg";
            avatarLink[2] = "https://preview.bambuser.io/live/eyJyZXNvdXJjZVVyaSI6Imh0dHBzOlwvXC9jZG4uYmFtYnVzZXIubmV0XC9icm9hZGNhc3RzXC9mYjAxNDAzOC1lZDczLTRhMWItYjAxNi04N2IwNTBjZmZlYWUifQ==/preview.jpg";
            avatarLink[3] = "https://preview.bambuser.io/live/eyJyZXNvdXJjZVVyaSI6Imh0dHBzOlwvXC9jZG4uYmFtYnVzZXIubmV0XC9icm9hZGNhc3RzXC8zYTlmZGJjYS0zZjVkLTQ2NTItODhjMi1hN2ViNjcyYjRiMjIifQ==/preview.jpg";
            avatarLink[4] = "https://preview.bambuser.io/live/eyJyZXNvdXJjZVVyaSI6Imh0dHBzOlwvXC9jZG4uYmFtYnVzZXIubmV0XC9icm9hZGNhc3RzXC85ZmFmZWNhNS1kMGNmLTRhOTItOTViYy02NjI4OTMyYzYxZTgifQ==/preview.jpg";


            for (int i = 0; i < 1; i++) {
                room.setId(PreferencesManager.getAccessToken(this));
                room.setPassword("12345");

                data.setAvatarLink(avatarLink[i]);
                data.setChannelId("1");
                data.setReSourceUri(reSourceUri[i]);
                mDatabase.child("Rooms").push().setValue(room);
                Log.e("TAG", i + " ok ");
            }

        } catch (JSONException e) {
            Log.i("LOGCAT", e.getMessage());
        }
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("tree.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e("TAG", ex.getMessage());
            return null;
        }
        return json;
    }

    public class Room {
        String id;
        String password;
        Data data;

        public Room() {

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public class Data {
        public String reSourceUri;
        public String ChannelId;
        public String avatarLink;

        public Data() {

        }

        public String getReSourceUri() {
            return reSourceUri;
        }

        public void setReSourceUri(String reSourceUri) {
            this.reSourceUri = reSourceUri;
        }

        public String getChannelId() {
            return ChannelId;
        }

        public void setChannelId(String channelId) {
            ChannelId = channelId;
        }

        public String getAvatarLink() {
            return avatarLink;
        }

        public void setAvatarLink(String avatarLink) {
            this.avatarLink = avatarLink;
        }
    }

    class provincial {
        String name;
        String slug;
        String type;
        String name_with_type;
        String code;

        public provincial() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName_with_type() {
            return name_with_type;
        }

        public void setName_with_type(String name_with_type) {
            this.name_with_type = name_with_type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

//    private void saveFacebookCredentialsInFirebase(AccessToken accessToken) {
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (!task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    private void saveGoogleCredentialsInFirebase(GoogleSignInAccount acct) {
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "Đăng nhập thật bại", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }

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
