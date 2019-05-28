package com.maq.xprize.kitkitlauncher.hindi;


import android.content.Context;

import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.intent.Intents;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
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


private static final int GRANT_BUTTON_INDEX = 1;    //// 1 for accp and 0 for deny
private static final int MAIN_APP_BUTTON_INDEX = 1;  // mainapp index = 1
private static final int CROSS_TILE_BUTTON_INDEX = 1; // mainapp -> close tile index = 1 , install tile index = 2
    private static final int INSTALL_BUTTON_BUTTON_INDEX = 2;
    @Test
    public void allowPermissionsIfNeeded() {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                UiDevice device = UiDevice.getInstance(getInstrumentation());
//                UiObject allowPermissions = device.findObject(new UiSelector()
//                        .clickable(true)
//                        .checkable(false)
//                        .index(GRANT_BUTTON_INDEX));
//                if (allowPermissions.exists()) {
//                    allowPermissions.click();
//                }
//            }
//        } catch (UiObjectNotFoundException e) {
//            System.out.println("There is no permissions dialog to interact with");
//        }
        onView(withId(R.id.relativeLayout)).check(matches(isDisplayed()));
       /* onView(withId(R.id.library2)).check(matches(isDisplayed()));
        onView(withId(R.id.overlay_tools)).check(matches(isDisplayed()));*/


        //onView(withId(R.id.relativeLayout)).perform(click());

////        // mainapp index = 1
//        try {
//
//                UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//                UiObject allowPermissions = device.findObject(new UiSelector()
//                        .clickable(true)
//                        .checkable(false)
//                        .index(MAIN_APP_BUTTON_INDEX));
//                if (allowPermissions.exists()) {
//                    allowPermissions.click();
//                }
//
//
//        } catch (UiObjectNotFoundException e) {
//
//        }
//        // mainapp -> close tile index = 1 , install tile index = 2
//        try {
//
//            UiDevice device = UiDevice.getInstance(getInstrumentation());
//            UiObject allowPermissions = device.findObject(new UiSelector()
//                    .clickable(true)
//                    .checkable(false)
//                    .index(INSTALL_BUTTON_BUTTON_INDEX));
//            if (allowPermissions.exists()) {
//                allowPermissions.click();
//            }
//
//        } catch (UiObjectNotFoundException e) {
//
//        }


    }



//    @After
//    public void intentsTeardown() {
//// release Espresso Intents capturing
//        Intents.release();
//    }



}

