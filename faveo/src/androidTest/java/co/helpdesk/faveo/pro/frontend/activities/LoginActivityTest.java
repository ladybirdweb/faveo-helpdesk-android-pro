package co.helpdesk.faveo.pro.frontend.activities;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.helpdesk.faveo.pro.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityTest() {
        String email = "baratam.narendra";
        String password = "demopass";
        String url = "www.ladybirdweb.com/support";

        onView(withId(R.id.editText_company_url)).perform(typeText(url), closeSoftKeyboard());
        onView(withId(R.id.fab_verify_url)).perform(click());
//        onView(withText(R.string.verifying_url))
//                .check(matches(isDisplayed()));
//        onView(withText(R.string.access_checking))
//                .check(matches(isDisplayed()));
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //type in email
        onView(withId(R.id.input_username)).perform(typeText(email), closeSoftKeyboard());

        //type in password
        onView(withId(R.id.input_password)).perform(typeText(password), closeSoftKeyboard());

        //click on login button
        onView(withId(R.id.button_signin)).perform(click());
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatEditText = onView(
//                allOf(withId(R.id.editText_company_url), isDisplayed()));
//        appCompatEditText.perform(replaceText("http://"), closeSoftKeyboard());
    }

}
