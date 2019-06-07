package com.maq.xprize.kitkitlauncher.hindi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.maq.kitkitProvider.KitkitDBHandler;
import com.maq.kitkitProvider.User;

import java.util.ArrayList;

public class MultiUserActivity extends AppCompatActivity {


    private static final String TAG = "UserNameActivity" ;
    Dialog addUserDialog;
    TextView gender;
    TextView usrname;
    EditText usrnameInput;
    EditText userAge;
    String userGender;
    Button submit;

    private RadioGroup genderRadioGroup;
    private Context schoolContext;
    private SharedPreferences schoolPref;
    private UserNameListDialog userNameListDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = this.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_multi_user);
        addUserDialog = new Dialog(this);
        usrname = (TextView)findViewById(R.id.tv_name);
    }

    public void AddAllUser(View v) {

        addUserDialog.setContentView(R.layout.add_all_user);
        usrnameInput = (EditText) addUserDialog.findViewById(R.id.editText);
        gender = (TextView) addUserDialog.findViewById(R.id.textView4);
        userAge = (EditText) addUserDialog.findViewById(R.id.editText2);
        submit =  (Button) addUserDialog.findViewById(R.id.button2);
        genderRadioGroup = (RadioGroup) addUserDialog.findViewById(R.id.radio);

        genderRadioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get selected radio button from Radio Group
                int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                switch(selectedId){
                    case R.id.radioFemale:
                        userGender = "Female";
                        break;
                    case R.id.radioMale:
                        userGender = "Male";
                        break;
                }
            }
        });

        // to show the Add user dialog
        addUserDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    KitkitDBHandler dbHandler = new KitkitDBHandler(getApplicationContext());
                    User user = new User(usrnameInput.getText().toString(), userGender,userAge.getText().toString());
                    dbHandler.addUser(user);
                    dbHandler.setCurrentUser(user);

                    Intent intent = new Intent(MultiUserActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    System.out.println("Error in adding user " + e.getMessage());
                }
            }
        });
    }

    // code for selecting all users.

    public void SelectAllUser(View view) {
        try {
            KitkitDBHandler dbHandler = ((LauncherApplication) getApplication()).getDbHandler();
            ArrayList<User> users = dbHandler.getUserList();
            schoolContext = getApplicationContext().createPackageContext("com.maq.xprize.kitkitschool.hindi", 0);
            schoolPref = schoolContext.getSharedPreferences("Cocos2dxPrefsFile", Context.MODE_PRIVATE);
            for (User u:users) {
                u.setGamesClearedInTotal_L(schoolPref.getInt((u.getUserName() + "_gamesClearedInTotal_en-US_L"), 0));
                u.setGamesClearedInTotal_M(schoolPref.getInt((u.getUserName() + "_gamesClearedInTotal_en-US_M"), 0));
            }
            userNameListDialog  = new UserNameListDialog(MultiUserActivity.this, users);
            userNameListDialog.show();
        }
        catch (PackageManager.NameNotFoundException ne) {
            userNameListDialog  = new UserNameListDialog(MultiUserActivity.this, null);
            Log.e(TAG, ne.toString());
            userNameListDialog.show();
        }
    }

    public void SetUser(View view) {
        KitkitDBHandler dbHandler = ((LauncherApplication) getApplication()).getDbHandler();
        usrname = view.findViewById(R.id.tv_name);
        User user = dbHandler.findUser(usrname.getText().toString());
        if (user != null) {
            dbHandler.setCurrentUser(user);
        }
        Intent intent = new Intent(MultiUserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}