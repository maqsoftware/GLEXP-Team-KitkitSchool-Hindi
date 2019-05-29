package com.maq.xprize.kitkitlauncher.hindi;

import android.os.Build;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Video_Test {
    private static final int MAIN_APP_BUTTON_INSTANCE= 1;
    private static final int LIBRARY_BUTTON_INSTANCE = 2;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);
    ///////////////////////////////////////////////// Test case /////////////////////////
    @Test
    private void VIDEO_TESTS() {

        permission("ALLOW");
        takess(1);
        ismainsmenuvisible();
        waitfortime(2000);

        clickTest(LIBRARY_BUTTON_INSTANCE);
        waitfortime(2000);
        takess(2);
        isvideomenuvisible();
        waitfortime(67000);                         /// library video = 67 sec   main app = 107 sec
        takess(3);
        clickback();

        clickTest(MAIN_APP_BUTTON_INSTANCE);
        waitfortime(2000);
        takess(4);
        isvideomenuvisible();
        waitfortime(107000);                        /// library video = 67 sec   main app = 107 sec
        clickback();
        ismainsmenuvisible();

    }


    private void clickTest(int INSTANCE_NUMBER){ /// click on defined element

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
            e.printStackTrace();
        }

    }

    private void waitfortime(int TIME){ // wait for mili second
        try {
            Thread.sleep(TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void clickback(){ /// click mobiles back button
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        device.pressBack();
    }

    private void ismainsmenuvisible(){
        onView(withId(R.id.button_todoschool)).check(matches(isDisplayed()));
        onView(withId(R.id.button_library)).check(matches(isDisplayed()));
        onView(withId(R.id.button_tool)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_currentUserId)).check(matches(isDisplayed()));
        onView(withId(R.id.launcher_title_button)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_numCoin)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView_coin)).check(matches(isDisplayed()));
    }

    private void istoolsmenuvisible(){
        onView(withId(R.id.image_coin)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_numCoin)).check(matches(isDisplayed()));
        onView(withId(R.id.app_drum)).check(matches(isDisplayed()));
        onView(withId(R.id.app_marimba)).check(matches(isDisplayed()));
        onView(withId(R.id.app_blackboard)).check(matches(isDisplayed()));
        onView(withId(R.id.app_coloring)).check(matches(isDisplayed()));
        onView(withId(R.id.app_drawing)).check(matches(isDisplayed()));
        onView(withId(R.id.app_album)).check(matches(isDisplayed()));
        onView(withId(R.id.app_fish_bowl)).check(matches(isDisplayed()));
        onView(withId(R.id.app_writing_board)).check(matches(isDisplayed()));
    }

    private void isvideomenuvisible(){
        onView(withId(R.id.installAppButton)).check(matches(isDisplayed()));
        onView(withId(R.id.videoCloseButton)).check(matches(isDisplayed()));
        onView(withId(R.id.videoSurface)).check(matches(isDisplayed()));
    }
    private void permission(String action) {
        if (Build.VERSION.SDK_INT >= 23) {
            UiObject allowPermissions = UiDevice.getInstance(getInstrumentation()).findObject(new UiSelector().text(action));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void takess(int i){    // takes screen shot of instance
        File path = new File("/storage/emulated/0/test-screenshots/filename" + Integer.toString(i));
        int SDK_VERSION = android.os.Build.VERSION.SDK_INT;
        if (SDK_VERSION >= 17) {
            waitfortime(1000);
            UiDevice.getInstance(getInstrumentation()).takeScreenshot(path);
            waitfortime(1000);
        }
    }
}
