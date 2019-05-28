package com.maq.xprize.kitkitlauncher.hindi;


import android.content.Context;
import android.graphics.Point;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test_Deny {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);



//    @Test
//    public void useAppContext() throws Exception {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("com.maq.xprize.kitkitlauncher.hindi", appContext.getPackageName());
//
//

//    @Rule
//    public ActivityTestRule<MainActivity> ActivityRule = new ActivityTestRule(MainActivity.class);
//    @Before
//public void intentsInit() {
//// initialize Espresso Intents capturing
//    Intents.init();
//}

    private static final int DENY_BUTTON_INDEX = 0;

    private static final int MAIN_APP_BUTTON_INSTANCE= 1;// mainapp INSTANCE= 1
    private static final int LIBRARY_BUTTON_INSTANCE = 2;
    private static final int TOOL_BUTTON_INSTANCE= 3;
    private static final int BACK_ARROW_INSTANCE= 0;

    @Test
    public void allowPermissionsIfNeeded() {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        Point[] coordinates = new Point[4];
        coordinates[0] = new Point(248, 1520);
        coordinates[1] = new Point(248, 929);
        coordinates[2] = new Point(796, 1520);
        coordinates[3] = new Point(796, 929);
        try {
            if (!device.isScreenOn()) {
                device.wakeUp();
                device.swipe(coordinates, 10);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        clickTest(DENY_BUTTON_INDEX);
        clickTest(TOOL_BUTTON_INSTANCE);
        clickTest(DENY_BUTTON_INDEX);
        clickTest(BACK_ARROW_INSTANCE);
        clickTest(MAIN_APP_BUTTON_INSTANCE);
       // onView(isRoot()).perform(ViewActions.pressBack());
//        clickTest(LIBRARY_BUTTON_INSTANCE);



    }


    private void clickTest(int INSTANCE_NUMBER){

        try {

            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .instance(INSTANCE_NUMBER));
            if (allowPermissions.exists()) {
                allowPermissions.click();
            }else {
                Log.d("Instance","Does Not Exist");
            }

        } catch (UiObjectNotFoundException e) {

        }

    }




}

