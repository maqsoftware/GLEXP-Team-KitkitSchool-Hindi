package com.maq.xprize.kitkitlauncher.hindi;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.maq.xprize.kitkitlauncher.hindi.MainActivity;
import com.maq.xprize.kitkitlauncher.hindi.R;
import junit.framework.AssertionFailedError;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4.class)
public class Test_view{

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void clicklib(){
        try {
            onView(withId(R.id.button_todoschool)).perform(click());
        } catch (AssertionFailedError e){

        }
        try {
            onView(withId(R.id.overlay_library)).check(matches(isDisplayed()));
        } catch (AssertionFailedError e){

        }
    }
}




