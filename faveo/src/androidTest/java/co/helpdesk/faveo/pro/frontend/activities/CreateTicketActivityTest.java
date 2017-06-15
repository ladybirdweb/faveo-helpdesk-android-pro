package co.helpdesk.faveo.pro.frontend.activities;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.helpdesk.faveo.pro.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Lenovo on 6/12/2017.
 */
@RunWith(AndroidJUnit4.class)
public class CreateTicketActivityTest {

    @Rule
    public ActivityTestRule<CreateTicketActivity> mActivityTestRule = new ActivityTestRule<>(CreateTicketActivity.class);

    @Test
    public void createTicketActivityTest() {
        String fname="Sayar";
        String lname="samanta";
        String email="sayar@gmail.com";
        String mobile="8981692638";
        String phone="03325927338";
        String subject="Ticket Query";
        String message="Want to know about my ticket";
//        onView(withId(R.id.spinner_pri)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("High"))).perform(click());
//        onView(withId(R.id.spinner_help_topics)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("Support query"))).perform(click());
        onView(withId(R.id.fname_edittext)).perform(typeText(fname), closeSoftKeyboard());
        onView(withId(R.id.lname_edittext)).perform(typeText(lname), closeSoftKeyboard());
        onView(withId(R.id.email_edittext)).perform(typeText(email),closeSoftKeyboard());

         //Click on the Spinner
        onView(withId(R.id.spinner_code)).perform(click());

        /** Click on the first item from the list. is – is a hamcrest matcher which is decorator to improve readability. It is equivalent to equalto().   **/
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.phone_edittext)).perform(typeText(mobile),closeSoftKeyboard());
        onView(withId(R.id.phone_edittext10)).perform(typeText(phone),closeSoftKeyboard());
        onView(withId(R.id.sub_edittext)).perform(typeText(subject), closeSoftKeyboard());
        ViewInteraction appCompatSpinner = onView(
                Matchers.allOf(withId(R.id.spinner_pri), isDisplayed()));
        appCompatSpinner.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                Matchers.allOf(withId(android.R.id.text1), withText("Low"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.spinner_help), isDisplayed()));
        appCompatSpinner2.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Support query"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());
        //onView(withId(R.id.msg_edittext)).perform(closeSoftKeyboard());
        onView(withId(R.id.msg_edittext)).perform(typeText(message));
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.button_submit), withContentDescription("Create Ticket"), isDisplayed()));
        actionMenuItemView.perform(click());
//        onView(withId(R.id.spinner_pri)).perform(click());
//        onData(allOf(is(instanceOf(Data.class)))).atPosition(2).perform(click());
//        onView(withId(R.id.spinner_help_topics)).perform(click());
//        onData(allOf(is(instanceOf(Data.class)))).atPosition(2).perform(click());









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
//        onView(withId(R.id.input_username)).perform(typeText(email), closeSoftKeyboard());
//
        //type in password
//        onView(withId(R.id.input_password)).perform(typeText(password), closeSoftKeyboard());

        //click on login button
//        onView(withId(R.id.button_signin)).perform(click());
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
