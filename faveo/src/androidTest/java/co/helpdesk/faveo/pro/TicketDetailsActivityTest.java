//package co.helpdesk.faveo.pro;
//
//
//import android.support.test.espresso.ViewInteraction;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.replaceText;
//import static android.support.test.espresso.action.ViewActions.scrollTo;
//import static android.support.test.espresso.action.ViewActions.typeText;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withParent;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.core.AllOf.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class TicketDetailsActivityTest {
//
//    @Rule
//    public ActivityTestRule<TicketDetailActivity> mActivityTestRule = new ActivityTestRule<>(TicketDetailActivity.class);
//
//    @Test
//    public void ticketDetailsActivityTest() {
//
////        onView(
////withId(R.id.editText_company_url)).perform(typeText("jamboreebliss.com/sayarnew/public"), closeSoftKeyboard());
////
////        onView(
////                withId(R.id.fab_verify_url)).perform(click());
////
////
////        onView(
////withId(R.id.input_username)).perform(typeText("sayar_samanta"), closeSoftKeyboard());
////
////
////        onView(
////withId(R.id.input_password)).perform(typeText("home@1234"), closeSoftKeyboard());;
////        onView(
////                withId(R.id.button_signin)).perform(scrollTo(),click());
//
//        onView(allOf(withId(R.id.ticket),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))))).perform(click());
//        ViewInteraction cardView = onView(
//allOf(withId(R.id.ticket),
//withParent(allOf(withId(R.id.cardList),
//withParent(withId(R.id.swipeRefresh)))),
//isDisplayed()));
//        cardView.perform(click());
//
//
//         onView(
//allOf(withId(R.id.fab), isDisplayed())).perform(click());
//
////
////         // Added a sleep statement to match the app's execution delay.
//// // The recommended way to handle such scenarios is to use Espresso idling resources:
////  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
////try {
//// Thread.sleep(150);
//// } catch (InterruptedException e) {
//// e.printStackTrace();
//// }
////
//         onView(
//allOf(withId(R.id.fab_sheet_item_reply), withText("Reply"), isDisplayed())).perform(click());
//
////
////         // Added a sleep statement to match the app's execution delay.
//// // The recommended way to handle such scenarios is to use Espresso idling resources:
////  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
////try {
//// Thread.sleep(180);
//// } catch (InterruptedException e) {
//// e.printStackTrace();
//// }
////
//         onView(
//allOf(withId(R.id.editText_reply_message),
//withParent(allOf(withId(R.id.section_reply),
//withParent(withId(R.id.reveal)))),
//isDisplayed())).perform(replaceText("Ticket Query."), closeSoftKeyboard());
//
////
//        onView(
//allOf(withId(R.id.button_send), withText("Send"),
//withParent(allOf(withId(R.id.section_reply),
//withParent(withId(R.id.reveal)))),
//isDisplayed())).perform(click());;
//
////
//        onView(
//allOf(withId(R.id.editText_reply_message), withText("Ticket Query."),
//withParent(allOf(withId(R.id.section_reply),
//withParent(withId(R.id.reveal)))),
//isDisplayed())).perform(replaceText(""), closeSoftKeyboard());
//
//
//
//        onView(
//allOf(withId(R.id.fab_sheet_item_note), withText("Internal Notes"), isDisplayed())).perform(click());
//
//
//        onView(
//allOf(withId(R.id.editText_internal_note),
//withParent(allOf(withId(R.id.section_internal_note),
//withParent(withId(R.id.reveal)))),
//isDisplayed())).perform(replaceText("Sample "), closeSoftKeyboard());
//
//
//        onView(
//allOf(withId(R.id.editText_internal_note), withText("Sample "),
//withParent(allOf(withId(R.id.section_internal_note),
//withParent(withId(R.id.reveal)))),
//isDisplayed())).perform(click());
//
////
//        onView(
//allOf(withId(R.id.editText_internal_note), withText("Sample "),
//withParent(allOf(withId(R.id.section_internal_note),
//withParent(withId(R.id.reveal)))),
//isDisplayed())).perform(replaceText("Sample Internal Note."), closeSoftKeyboard());
//
////
//        onView(
//allOf(withId(R.id.button_create), withText("Create"),
//withParent(allOf(withId(R.id.section_internal_note),
//withParent(withId(R.id.reveal)))),
//isDisplayed())).perform(click());
//
////
//        onView(
//allOf(withId(R.id.editText_internal_note), withText("Sample Internal Note."),
//withParent(allOf(withId(R.id.section_internal_note),
//withParent(withId(R.id.reveal)))),
//isDisplayed())).perform(replaceText(""), closeSoftKeyboard());
//
//
////
//         onView(
//allOf(withText("Details"), isDisplayed())).perform(click());
//
//        onView(
//allOf(withId(R.id.editText_subject))).perform(scrollTo(), typeText("Ticket Status Related Query testing sample"), closeSoftKeyboard());
//
//
//       onView(
//withId(R.id.spinner_priority)).perform(scrollTo(), click());
//
//
//       onView(
//allOf(withId(android.R.id.text1), withText("Normal"), isDisplayed())).perform(click());
//
//
//       onView(
//withId(R.id.spinner_type)).perform(scrollTo(), click());
//
//
//         onView(
//allOf(withId(android.R.id.text1), withText("Question"), isDisplayed())).perform(click());
//
//
//       onView(
//withId(R.id.spinner_help_topics)).perform(scrollTo(), click());
//
//
//        onView(
//allOf(withId(android.R.id.text1), withText("Sales query"), isDisplayed())).perform(click());
//
//
//         onView(allOf(
//withId(R.id.spinner_source),withText("web"),isDisplayed())).perform(scrollTo(), click());
//
//
//
//        onView(allOf(withId(android.R.id.text1), withText("email"), isDisplayed())).perform(click());
//
//
//         onView(
//allOf(withId(R.id.button_save))).perform(scrollTo(), click());
//
//
//        }
//
//    }
