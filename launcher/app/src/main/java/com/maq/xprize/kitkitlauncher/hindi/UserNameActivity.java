package com.maq.xprize.kitkitlauncher.hindi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.maq.kitkitProvider.KitkitDBHandler;
import com.maq.kitkitProvider.User;
import com.maq.kitkitlogger.KitKitLogger;
import com.maq.kitkitlogger.KitKitLoggerActivity;

import java.util.ArrayList;

public class UserNameActivity extends KitKitLoggerActivity {

    private static final String TAG = "UserNameActivity";
    private View mVBack;

    private TextView mTvUserNo;
    private TextView mTvUserName;
    private View mVRename;

    private Context schoolContext;
    private SharedPreferences schoolPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.hideSystemUI(this);
        setContentView(R.layout.activity_username);
        Util.setScale(this, findViewById(R.id.root_container));

        mVBack = findViewById(R.id.v_back);
        mVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTvUserNo = findViewById(R.id.tvUserNo);
        mTvUserName = findViewById(R.id.tvUserName);
        mTvUserName.setOnClickListener(mOnClickListener);
        mVRename = findViewById(R.id.vRename);
        mVRename.setOnClickListener(mOnClickListener);
        findViewById(R.id.vSelectUserNumber).setOnClickListener(mOnClickListener);
        findViewById(R.id.vUserNameList).setOnClickListener(mOnClickListener);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Util.hideSystemUI(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Util.mBlockingView != null) {
            Util.mBlockingView.setOnTouchListener(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        KitKitLogger logger = ((LauncherApplication) getApplication()).getLogger();
        logger.tagScreen("UserNameActivity");

        refreshUI();

        if (Util.mBlockingView != null) {
            Util.mBlockingView.setOnTouchListener(mBlockViewTouchListener);
        }
    }

    public void refreshUI() {
        Util.displayUserName(this, (TextView) findViewById(R.id.textView_currentUsername));

        User user = ((LauncherApplication) getApplication()).getDbHandler().getCurrentUser();
        mTvUserNo.setText(getString(R.string.user_no) + " " + user.getUserName().replace("user", ""));
        String displayName = user.getDisplayName();

        mTvUserName.setBackgroundColor(Color.WHITE);
        mTvUserName.setText(displayName);
        mVRename.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvUserName:
                case R.id.vRename:

                    if (view.getId() == R.id.tvUserName && mVRename.getVisibility() == View.VISIBLE) {
                        return;
                    }

                {
                    User user = ((LauncherApplication) getApplication()).getDbHandler().getCurrentUser();
                    RenameUserDialog registerUserDialog = new RenameUserDialog(UserNameActivity.this, user, new RenameUserDialog.Callback() {
                        @Override
                        public void onSubmit(String name) {
                            KitkitDBHandler kitkitDBHandler = ((LauncherApplication) getApplication()).getDbHandler();
                            User user = kitkitDBHandler.getCurrentUser();
                            if (user != null) {
                                user.setDisplayName(name);
                                kitkitDBHandler.updateUser(user);
                            }
                            refreshUI();
                        }
                    });

                    registerUserDialog.show();
                }
                break;
                case R.id.vSelectUserNumber: {
                    SelectNumberDialog selectNumberDialog = new SelectNumberDialog(UserNameActivity.this, SelectNumberDialog.MODE.USER_NO, new SelectNumberDialog.Callback() {
                        @Override
                        public void onSelectedNumber(int number) {
                            KitkitDBHandler dbHandler = ((LauncherApplication) getApplication()).getDbHandler();
                            User user = dbHandler.findUser("user" + number);
                            if (user != null) {
                                dbHandler.setCurrentUser(user);
                                refreshUI();
                            }
                        }
                    });

                    selectNumberDialog.show();

                }
                break;
                case R.id.vUserNameList: {
                    KitkitDBHandler dbHandler = ((LauncherApplication) getApplication()).getDbHandler();
                    ArrayList<User> users = dbHandler.getUserList();
                    try {
                        schoolContext = getApplicationContext().createPackageContext("com.maq.xprize.kitkitschool.hindi", 0);
                        schoolPref = schoolContext.getSharedPreferences("Cocos2dxPrefsFile", Context.MODE_PRIVATE);
                        for (User u : users) {
                            u.setGamesClearedInTotal_L(schoolPref.getInt((u.getUserName() + "_gamesClearedInTotal_en-US_L"), 0));
                            u.setGamesClearedInTotal_M(schoolPref.getInt((u.getUserName() + "_gamesClearedInTotal_en-US_M"), 0));
                        }
                    } catch (PackageManager.NameNotFoundException ne) {
                        Log.e(TAG, ne.toString());
                    }
                    UserNameListDialog userNameListDialog = new UserNameListDialog(UserNameActivity.this, users);
                    userNameListDialog.show();
                }
                break;
            }
        }
    };

    private Rect mTempRect = new Rect();
    private boolean mbPressed = false;
    private View.OnTouchListener mBlockViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mVBack.getGlobalVisibleRect(mTempRect);
                if (mTempRect.contains((int) event.getX(), (int) event.getY())) {
                    mbPressed = true;
                    mVBack.dispatchTouchEvent(event);
                } else {
                    mbPressed = false;
                }
            } else {
                if (mbPressed) {
                    mVBack.dispatchTouchEvent(event);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mbPressed = false;
                }
            }

            return true;
        }
    };
}
