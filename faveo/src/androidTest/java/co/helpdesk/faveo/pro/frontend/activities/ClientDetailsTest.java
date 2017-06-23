package co.helpdesk.faveo.pro.frontend.activities;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.helpdesk.faveo.pro.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ClientDetailsTest {

    @Rule
    public ActivityTestRule<ClientDetailActivity> mActivityTestRule = new ActivityTestRule<>(ClientDetailActivity.class);

    @Test
    public void clientDetailsTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        onView(
//                allOf(ViewMatchers.withId(R.id.ticket),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed())).perform(click());
//        onView(
//                allOf(withContentDescription("Navigate up"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appbar)))),
//                        isDisplayed())).perform(click());

        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());

//        onView(
//                withId(R.id.client_list)).perform(scrollTo(),click());

//        onView(
//                allOf(withId(R.id.client),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed())).perform(click());


        onView(
                allOf(withText("CLOSED TICKET"), isDisplayed())).perform(click());
        onView(
                allOf(withText("OPEN TICKET"), isDisplayed())).perform(click());

//        onView(
//                allOf(withContentDescription("Navigate up"),
//                        withParent(withId(R.id.toolbar)),
//                        isDisplayed())).perform(click());
//
//
  }

}
