package com.panda.live.pandalive.Login;

/**
 * Created by levan on 25/02/2018.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.panda.live.pandalive.Home.HomeActivity;
import com.panda.live.pandalive.R;
import com.panda.live.pandalive.data.model.Profile;
import com.panda.live.pandalive.data.model.User;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.model.Data;
import com.panda.live.pandalive.profile.ProfileActivity;
import com.panda.live.pandalive.profile.ProfileDetailActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private CallbackManager mCallbackManager;
    private LinearLayout mLoginButton, mPhone;
    private Button mBtnSignUp, mBtnSignIn;
    private Intent intentMain, intentRegistry, intentPhoneLogin, intentProfileDetail;
    private Data mData;
    private BottomSheetBehavior mBottomSheetBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View mBottomSheetView;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private String userID;
    private FirebaseAuth mAuth;
    private Context mContext;
    private AVLoadingIndicatorView mAvi;

    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;
    private StorageReference mRefStorage;
    private Uri mFilePath;

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info...
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if (!hasPermission(Manifest.permission.CAMERA) && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

        mContext = this.getApplicationContext();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();


        FacebookSdk.sdkInitialize(getApplicationContext());

        mAvi = findViewById(R.id.avi);
        mCallbackManager = CallbackManager.Factory.create();//Lấy dữ liệu


        intentRegistry = new Intent(this, PhoneAuthActivity.class);
        intentPhoneLogin = new Intent(this, PhoneLogin.class);

        intentMain = new Intent(this, HomeActivity.class);
        intentRegistry = new Intent(this, PhoneAuthActivity.class);
        intentPhoneLogin = new Intent(this, PhoneLogin.class);
        intentProfileDetail = new Intent(this, ProfileDetailActivity.class);
        mLoginButton = findViewById(R.id.button_facebook_login);

        mPhone = findViewById(R.id.phone_button);
        mAuth = FirebaseAuth.getInstance();
        //https://developers.facebook.com/docs/facebook-login/permissions#reference-email
        //xem link rõ hơn
        //mLoginButton.setReadPermissions("email", "public_profile");

        // Lớp Data chứa các thuộc tín
        //printKeyHash(this);h static
        mData = new Data();
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                ConnectToFacebook();
                PreferencesManager.setStateLogin(mContext, 1);;// nếu data.value = 1 là đăng nhập = face, = 2 là google
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));

            }
        });

        mBottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_view_phone, null);
        mBottomSheetDialog = new BottomSheetDialog(LoginActivity.this);
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
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setStateLogin(mContext, 3);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                mBottomSheetDialog.show();
                mBtnSignUp = findViewById(R.id.btn_sign_up);
            }
        });

        mBtnSignUp = mBottomSheetView.findViewById(R.id.btn_sign_up);
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentRegistry);
            }
        });
        mBtnSignIn = mBottomSheetView.findViewById(R.id.btn_sign_in);
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentPhoneLogin);
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();


        //Sử dụng builder để tạo các tùy chọn yêu cầu quyền truy cập khi đăng nhập
        //DEFAULT_SIGN_IN chỉ bao gồm thông tin cơ bản (ID, tên, thông tin chung) và email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Xây dựng một người dùng với các tùy chọn được thiếp lập bởi gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Đặt kích thước của nút đăng nhập.
        final LinearLayout signInButton = findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        //signInButton.setColorScheme(SignInButton.COLOR_LIGHT);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setStateLogin(mContext, 2);
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
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            //Khi đăng nhập thành công
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // Yêu câu gửi về các thông tin
                final GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            //Gửi về thành công
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
//
                                String id = object.optString(getString(R.string.id));
                                String name = object.optString(getString(R.string.name));
                                String uri = "http://graph.facebook.com/" + id + "/picture?type=large";
                                mFilePath = Uri.parse(uri);

                                PreferencesManager.saveUserInfo(mContext,name,
                                        id, "none","none","none",
                                        Uri.parse(uri),"none", "none","none","none",1000);
                                saveFacebookCredentialsInFirebase(loginResult.getAccessToken());
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
                Log.e("Error", error.getMessage());
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }

        });

    }

    private void saveFacebookCredentialsInFirebase(final AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                } else {

                    if(PreferencesManager.getValueLoginFace(mContext) + 1 == 1){
                        writeDataTheFirst();
                        PreferencesManager.setValueLoginFace(mContext, 2);
                    }
                    PreferencesManager.setAccessToken(mContext, FirebaseAuth.getInstance().getUid());
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(intentMain);
                    finish();
                }
            }
        });
    }

    private void saveGoogleCredentialsInFirebase(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //loadData();
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        } else {
                            if(PreferencesManager.getValueLoginGoogle(mContext) + 1 == 1){
                                writeDataTheFirst();
                                PreferencesManager.setValueLoginGoogle(mContext, 2);
                            }
                            PreferencesManager.setAccessToken(mContext, FirebaseAuth.getInstance().getUid());
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(intentMain);
                            finish();
                        }
                    }
                });
    }

    //Hàm lưu lại thông tin sau khi đăng nhập thành công.
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            startAnim();
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            PreferencesManager.saveUserInfo(mContext,account.getDisplayName(),
                    account.getId(), "","","",
                        account.getPhotoUrl(),"",
                            "","","",1000);
            mFilePath = account.getPhotoUrl();
            saveGoogleCredentialsInFirebase(account);
        } catch (ApiException e) {
            Log.e("TAG", e.getMessage());
            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public void writeDataTheFirst() {
        FirebaseUser users = mAuth.getCurrentUser();
        userID = users.getUid();
        User user = new User(PreferencesManager.getID(mContext), "NO",
                PreferencesManager.getName(mContext),"none",1000, 0, 0);
        Profile profile = new Profile("none", "none",
                        "none", "none", "none", "Vui lòng chọn ngày sinh");
        mRef.child("users").child(userID).setValue(user);
        mRef.child("users").child(userID).child("profile").setValue(profile);
    }


    void startAnim(){
        mAvi.show();
        // or avi.smoothToShow();
    }

    private void uploadImage() {

        if (mFilePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            mRefStorage = mStorageReference.child("images").child(userID + "/avatarLive");
            mRefStorage.putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }


}

