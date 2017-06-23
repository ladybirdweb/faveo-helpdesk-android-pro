package co.helpdesk.faveo.pro.frontend.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.helpdesk.faveo.pro.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NotificationActivityTest {

    @Rule
    public ActivityTestRule<NotificationActivity> mActivityTestRule = new ActivityTestRule<>(NotificationActivity.class);

    @Test
    public void notificationActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(6926);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction actionMenuItemView = onView(
                allOf(ViewMatchers.withId(R.id.action_noti), withContentDescription("Notifications"), isDisplayed()));
        actionMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(604800000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction cardView = onView(
                allOf(withId(R.id.notification_cardview),
                        withParent(allOf(withId(R.id.recycler_view),
                                withParent(withId(R.id.swipeRefresh)))),
                        isDisplayed()));
        cardView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(86400000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction cardView2 = onView(
                allOf(withId(R.id.thread), isDisplayed()));
        cardView2.perform(click());

        ViewInteraction cardView3 = onView(
                allOf(withId(R.id.thread), isDisplayed()));
        cardView3.perform(click());

        ViewInteraction cardView4 = onView(
                allOf(withId(R.id.thread), isDisplayed()));
        cardView4.perform(click());

        ViewInteraction cardView5 = onView(
                allOf(withId(R.id.thread), isDisplayed()));
        cardView5.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withText("Details"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editText_subject), withText("Ticket Status Related Query")));
        appCompatEditText4.perform(scrollTo(), click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editText_subject), withText("Ticket Status Related Query")));
        appCompatEditText5.perform(scrollTo(), replaceText("Ticket Status Related Query testing"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                withId(R.id.spinner_priority));
        appCompatSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("Low"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                withId(R.id.spinner_type));
        appCompatSpinner2.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Incident"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatSpinner3 = onView(
                withId(R.id.spinner_source));
        appCompatSpinner3.perform(scrollTo(), click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("web"), isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        pressBack();

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button_save), withText("Save")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.button_save), withText("Save")));
        appCompatButton3.perform(scrollTo(), click());

    }

}
