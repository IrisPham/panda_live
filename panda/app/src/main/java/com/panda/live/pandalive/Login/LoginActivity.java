package com.panda.live.pandalive.Login;

/**
 * Created by levan on 25/02/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.panda.live.pandalive.MainActivity.MainActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.Data;


import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager mCallbackManager;
    private Intent intent;
    private Data data;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());// tích hợp SDK vào app
        mCallbackManager = CallbackManager.Factory.create();//Lấy dữ liệu
        intent = new Intent(this,MainActivity.class);

        loginButton = findViewById(R.id.button_facebook_login);
        //https://developers.facebook.com/docs/facebook-login/permissions#reference-email
        //xem link rõ hơn
        loginButton.setReadPermissions("email", "public_profile");

        // Lớp Data chứa các thuộc tính static
        data = new Data();
        //printKeyHash(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.value = 1;// nếu data.value = 1 là đăng nhập = face, = 2 là google
                ConnectToFacebook();

            }
        });

        //Sử dụng builder để tạo các tùy chọn yêu cầu quyền truy cập khi đăng nhập
        //DEFAULT_SIGN_IN chỉ bao gồm thông tin cơ bản (ID, tên, thông tin chung) và email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //Xây dựng một người dùng với các tùy chọn được thiếp lập bởi gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Đặt kích thước của nút đăng nhập.
        final SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.value = 2;
                signIn();
            }
        });

    }



    //Lấy kết quả trả về từ Google Sign in và xử lý kết quả đó bằng hàm handleSignInResult
    //đối với gmail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        //Kết quả được trả về sẽ được xử lí bởi hàm handleSignInResult
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }



    //Kết nối đến Facebook và lấy thông tin
    public void ConnectToFacebook() {
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            //Khi đăng nhập thành công
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Yêu câu gửi về các thông tin
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            //Gửi về thành công
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                // Lấy ID
                                String id = object.optString(getString(R.string.id));
                                /*Chỗ này bị null*/
                                // Lấy tên
                                String name = object.optString(getString(R.string.name));
                                data.ID = id;
                                data.name = name;
                                startActivity(intent);
                            }
                        });
                Bundle parameters = new Bundle();
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    //Hàm lưu lại thông tin sau khi đăng nhập thành công.
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
            data.name = account.getDisplayName();
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

}

