package com.maq.xprize.kitkitlauncher.hindi;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MultiUserActivity extends AppCompatActivity {


    private static final String TAG = "UserNameActivity" ;
    Dialog addUserDialog;
    TextView usrname;
    TextView deleteUser;
    EditText usrnameInput;
    EditText userAge;
    Button submit;
    ImageButton btnTakePic;
    Button close;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private byte imageInByte[];
    private Uri picUri;
    static final int PIC_CROP = 2;
    private Context schoolContext;
    private SharedPreferences schoolPref;
    private UserNameListDialog userNameListDialog;
    Dialog selectUserDialog;
    private ViewPager imagePager;
    Button exit;
    ImageView picture;
    //
    ImageButton updatepic;
    EditText updateage;
    Button update;
    Button goback;
    //
    ImageView goToDashboard;
    Spinner editUser;
    ArrayAdapter<String> editAdapter;
    Dialog updateUserDialog;                                      ////////////////////////
    int k =0;

    String[] names = {"Edit Profile", "Delete Profile"};


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
       // updateUserDialog =new Dialog(this);                                 ///////////////////////////////
        //updateUserDialog.setContentView(R.layout.activity_update_user);
        selectUserDialog = new Dialog(this);
        selectUserDialog.setContentView(R.layout.activity_select_user);
        imagePager = (ViewPager) selectUserDialog.findViewById(R.id.viewpager);

        //editUser = (Spinner) selectUserDialog.findViewById(R.id.spinner);
       // editUser.setOnItemSelectedListener(this);
       // editAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, names);
       // editAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // editUser.setAdapter(editAdapter);




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_IMAGE_CAPTURE) {
               // picUri = data.getData();
               // performCrop();
            //}
      //  else if(requestCode == PIC_CROP) {                                                      //////////////////////////////
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
            }                                                                                  /////////////////////////////
        }
    }

    // function to perform crop
    ///////////////////////////////////
    private void performCrop() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 200);
            cropIntent.putExtra("outputY", 200);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
                                   ///////////////////////////////

    public void AddAllUser(View v) {

        addUserDialog.setContentView(R.layout.add_all_user);
        close = (Button) addUserDialog.findViewById(R.id.close);
        btnTakePic = (ImageButton) addUserDialog.findViewById(R.id.imageView);
        userAge = (EditText) addUserDialog.findViewById(R.id.ageInput);
        submit =  (Button) addUserDialog.findViewById(R.id.register);
        picture = (ImageView) addUserDialog.findViewById(R.id.picture);


        // to show the Add user dialog
        addUserDialog.show();
        Window window = addUserDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


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
                    KitkitDBHandler dbHandler = new KitkitDBHandler(getApplicationContext());
                    k = dbHandler.numUser();
                    User user = new User("user-"+Integer.toString(k), userAge.getText().toString(),imageInByte);
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




    public void SelectUser(View view) {
        try {
            KitkitDBHandler dbHandler = ((LauncherApplication) getApplication()).getDbHandler();
            ArrayList<User> users = dbHandler.getUserList();
            exit = (Button) selectUserDialog.findViewById(R.id.close);
            goToDashboard = (ImageView) selectUserDialog.findViewById(R.id.gotodashboard);

            schoolContext = getApplicationContext().createPackageContext("com.maq.xprize.kitkitschool.hindi", 0);
            schoolPref = schoolContext.getSharedPreferences("Cocos2dxPrefsFile", Context.MODE_PRIVATE);
            for (User u:users) {
                u.setGamesClearedInTotal_L(schoolPref.getInt((u.getUserName() + "_gamesClearedInTotal_en-US_L"), 0));
                u.setGamesClearedInTotal_M(schoolPref.getInt((u.getUserName() + "_gamesClearedInTotal_en-US_M"), 0));
            }

            imagePager.setAdapter(new SlidingPagerAdapter(MultiUserActivity.this, users));
            selectUserDialog.show();

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
            Log.e(TAG, ne.toString());
            imagePager.setAdapter(new SlidingPagerAdapter(MultiUserActivity.this, null));
            selectUserDialog.show();
        }
    }
/*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final KitkitDBHandler dbHandler = new KitkitDBHandler(getApplicationContext());
        deleteUser = selectUserDialog.findViewById(R.id.userN);
        String item =  parent.getItemAtPosition(position).toString();
        if(item == "Edit Profile") {
            updatepic = (ImageButton) updateUserDialog.findViewById(R.id.updateImage);
            updateage = (EditText) updateUserDialog.findViewById(R.id.updateAge);
            update = (Button) updateUserDialog.findViewById(R.id.updateUser);
            goback = (Button) updateUserDialog.findViewById(R.id.goBack);

            updateUserDialog.show();
            // Go Back listener
            goback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUserDialog.dismiss();
                }
            });

            // Code to click picture

            updatepic.setOnClickListener(new View.OnClickListener() {
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

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = dbHandler.findUser(deleteUser.getText().toString());
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
        }

            else if(item == "Delete Profile"){

                dbHandler.deleteUser(deleteUser.getText().toString());

        }


}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    */


    // code for selecting all users.
/*
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
    */

    /*
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
    */
}