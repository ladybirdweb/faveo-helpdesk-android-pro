package co.helpdesk.faveo.pro.frontend.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.helpdesk.faveo.pro.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by narendra on 23/05/17.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainTest() {

                // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html

        /**
         * This is for opening the notification page.
         */
        onView(
                Matchers.allOf(withId(R.id.action_noti), withContentDescription("Notifications"),
                        isDisplayed())).perform(click());

        /**
         * This is for performing some action in the recycler view of the notification page.
         */
        onView(
                Matchers.allOf(withId(R.id.notification_cardview),
                        withParent(Matchers.allOf(withId(R.id.recycler_view),
                                withParent(withId(R.id.swipeRefresh)))),
                        isDisplayed())).perform(click());


        /**
         * This is for performing the button click in about page.
         */
        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.about)).perform(click());
        onView(withId(R.id.button_website)).perform(scrollTo(),click());


        /**
         * This is for opening the drawer and clicking on the
         * create ticket option.
         */
        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.create_ticket)).perform(click());

/**
 * This is for opening the drawer and clicking on the
 * inbox ticket option.
 */
        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.inbox_tickets)).perform(click());
/**
 * This is for opening the drawer and clicking on the
 * my ticket option.
 */

        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.my_tickets)).perform(click());

/**
 * This is for opening the drawer and clicking on the
 * unassigned ticket option.
 */
        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.unassigned_tickets)).perform(click());

/**
 * This is for opening the drawer and clicking on the
 * closed ticket option.
 */
        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.closed_tickets)).perform(click());

/**
 * This is for opening the drawer and clicking on the
 * trash ticket option.
 */
        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.trash_tickets)).perform(click());
/**
 * This is for opening the drawer and clicking on the
 * client ticket option.
 */

        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.client_list)).perform(click());

/**
 * This is for opening the drawer and clicking on the
 * settings option.
 */
        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.settings)).perform(click());

        /**
         * This is for enabling the crash report
         */
        onView(
                Matchers.allOf(withId(R.id.switch_crash_reports), isDisplayed())).perform(click());
/**
 * This is for opening the drawer and clicking on the
 * about option.
 */


        //onView(
                //Matchers.allOf(withId(R.id.button_website), withText("Website"), isDisplayed())).perform(click());
/**
 * This is for opening the drawer and clicking on the
 * logout  option.
 */
        onView(
                allOf(withContentDescription("Open"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.appBarLayout)))),
                        isDisplayed())).perform(click());
        onView(withId(R.id.logout)).perform(click());
        onView(
                Matchers.allOf(withId(android.R.id.button1), withText("yes"))).perform(click());



    }
}
