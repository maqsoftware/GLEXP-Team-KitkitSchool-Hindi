package com.maq.xprize.kitkitlauncher.hindi;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.maq.kitkitProvider.KitkitDBHandler;
import com.maq.kitkitProvider.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MultiUserActivity extends AppCompatActivity {


    Dialog addUserDialog;
    EditText userAge;
    Button submit;
    ImageButton btnTakePic;
    Button close;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_UPDATE = 2;
    private byte imageInByte[];
    private Context schoolContext;
    private SharedPreferences schoolPref;
    private UserNameListDialog userNameListDialog;
    public static Dialog selectUserDialog;
    private ViewPager imagePager;
    Dialog editPurpose;
    Button exit;
    ImageView picture;
    ImageButton pic;

    EditText updateage;
    Button update;
    Button delete;
    Button goback;

    Button goToDashboard;
    Dialog updateUserDialog;
    int k =0;



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
        editPurpose = new Dialog(this);
        editPurpose.setContentView(R.layout.sliding_images);
        addUserDialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        updateUserDialog =new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        updateUserDialog.setContentView(R.layout.activity_update_user);
        selectUserDialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        selectUserDialog.setContentView(R.layout.activity_select_user);
        imagePager = (ViewPager) selectUserDialog.findViewById(R.id.viewpager);


    }

    public Bitmap converToBitmap(byte[] image){
        ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        return theImage;
    }

    public void editUser(View vv){
        updateage = (EditText) updateUserDialog.findViewById(R.id.updateAge);
        update = (Button) updateUserDialog.findViewById(R.id.updateUser);
        goback = (Button) updateUserDialog.findViewById(R.id.goBack);
        delete = (Button) updateUserDialog.findViewById(R.id.deleteUser);
        pic = (ImageButton) updateUserDialog.findViewById(R.id.updatepic);

        int edituserId = imagePager.getCurrentItem();
        KitkitDBHandler dbHandler = new KitkitDBHandler(getApplicationContext());
        User user = dbHandler.findUser("user-" + edituserId);

        pic.setImageBitmap(converToBitmap(user.getImage()));
        updateage.setText(user.getAge());
        Window window = updateUserDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = window.getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = window.getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        updateUserDialog.show();
        // Go Back listener
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserDialog.dismiss();
            }
        });

        // Code to click picture

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takepic, REQUEST_UPDATE);


                takepic.setType("image/*");
                takepic.putExtra("crop", "true");
                takepic.putExtra("aspectX", 0);
                takepic.putExtra("aspectY", 0);
                takepic.putExtra("outputX", 250);
                takepic.putExtra("outputY", 200);

            }
        });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int edituserId = imagePager.getCurrentItem();
                    KitkitDBHandler dbHandler = new KitkitDBHandler(getApplicationContext());
                    User user = dbHandler.findUser("user-" + edituserId);
                    if (user != null) {
                        user.setImage(imageInByte);
                        user.setAge(updateage.getText().toString());
                        dbHandler.updateUser(user);
                        dbHandler.setCurrentUser(user);
                    }

                    Intent intent = new Intent(MultiUserActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KitkitDBHandler dbHandler = new KitkitDBHandler(getApplicationContext());
                    int edituserId = imagePager.getCurrentItem();
                    dbHandler.deleteUser("user-" + edituserId);
                    Intent intent = new Intent(MultiUserActivity.this, MultiUserActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap yourImage = extras.getParcelable("data");
                    picture.setImageBitmap(yourImage);

                    //convert Bitmap to byte

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imageInByte = stream.toByteArray();
                    Log.e("out before conversion", imageInByte.toString());
                }
            }
            else if(requestCode == REQUEST_UPDATE){
                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap yourImage = extras.getParcelable("data");
                    pic.setImageBitmap(yourImage);

                    //convert Bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imageInByte = stream.toByteArray();
                    Log.e("out before conversion", imageInByte.toString());
                }
            }
        }
    }



    public void AddAllUser(View v) {

        try {
            addUserDialog.setContentView(R.layout.add_all_user);
            close = (Button) addUserDialog.findViewById(R.id.close);
            btnTakePic = (ImageButton) addUserDialog.findViewById(R.id.imageView);
            userAge = (EditText) addUserDialog.findViewById(R.id.ageInput);
            submit = (Button) addUserDialog.findViewById(R.id.register);
            picture = (ImageView) addUserDialog.findViewById(R.id.updatepic);


            // to show the Add user dialog
            addUserDialog.show();
            Window window = addUserDialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                View vv = window.getDecorView();
                vv.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                View decorView = window.getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
            }


            // Go Back listener
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addUserDialog.dismiss();
                }
            });

            // Code to click picture

            btnTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takepic, REQUEST_IMAGE_CAPTURE);


                    takepic.setType("image/*");
                    takepic.putExtra("crop", "true");
                    takepic.putExtra("aspectX", 0);
                    takepic.putExtra("aspectY", 0);
                    takepic.putExtra("outputX", 250);
                    takepic.putExtra("outputY", 200);

                }
            });


            //Register user.
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int result = Integer.parseInt(userAge.getText().toString());
                        KitkitDBHandler dbHandler = new KitkitDBHandler(getApplicationContext());
                        k = dbHandler.numUser();
                        if (imageInByte != null) {
                            if( result>= 1) {
                                User user = new User("user-" + Integer.toString(k), userAge.getText().toString(), imageInByte);
                                dbHandler.addUser(user);
                                dbHandler.setCurrentUser(user);

                                Intent intent = new Intent(MultiUserActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Invalid Age",Toast.LENGTH_LONG).show();
                            }
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "Age or Image missing",Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        System.out.println("Error in adding user " + e.getMessage());
                    }
                }
            });
        }
        catch (Exception ne){
            Toast.makeText(this, "Age or Image missing",Toast.LENGTH_LONG).show();
        }


    }


    public void SelectUser(View view) {
        try {
            KitkitDBHandler dbHandler = ((LauncherApplication) getApplication()).getDbHandler();
            ArrayList<User> users = dbHandler.getUserList();
            exit = (Button) selectUserDialog.findViewById(R.id.close);
            goToDashboard = (Button) selectUserDialog.findViewById(R.id.gotodashboard);

            schoolContext = getApplicationContext().createPackageContext("com.maq.xprize.kitkitschool.hindi", 0);
            schoolPref = schoolContext.getSharedPreferences("Cocos2dxPrefsFile", Context.MODE_PRIVATE);
            for (User u:users) {
                u.setGamesClearedInTotal_L(schoolPref.getInt((u.getUserName() + "_gamesClearedInTotal_en-US_L"), 0));
                u.setGamesClearedInTotal_M(schoolPref.getInt((u.getUserName() + "_gamesClearedInTotal_en-US_M"), 0));
            }

            imagePager.setAdapter(new SlidingPagerAdapter(MultiUserActivity.this, users));

            selectUserDialog.show();
            Window window = selectUserDialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                View v = window.getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                View decorView = window.getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
            }

            goToDashboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        KitkitDBHandler dbHandler = new KitkitDBHandler(getApplicationContext());
                        int userId = imagePager.getCurrentItem();
                        User user = dbHandler.findUser("user-" + userId);
                        if(user != null){
                            dbHandler.setCurrentUser(user);
                        }
                        Intent intent = new Intent(MultiUserActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    catch (Exception e) {
                        System.out.println("Error in setting User " + e.getMessage());
                    }
                }
            });



            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectUserDialog.dismiss();
                }
            });
        }
        catch (Exception ne) {
            Toast.makeText(this, "No user exist",
                    Toast.LENGTH_LONG).show();
        }
    }

}