//package co.helpdesk.faveo.pro.frontend.activities;
//
//
//import android.support.test.espresso.ViewInteraction;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//
//import org.hamcrest.Description;
//import org.hamcrest.Matcher;
//import org.hamcrest.TypeSafeMatcher;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import co.helpdesk.faveo.pro.R;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.replaceText;
//import static android.support.test.espresso.action.ViewActions.scrollTo;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
//import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withParent;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.allOf;
//import static org.hamcrest.Matchers.is;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class UnitTestCasesForFaveoApplication {
//
//    @Rule
//    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
//
//    @Test
//    public void unitTestCasesForFaveoApplication() {
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatAutoCompleteTextView = onView(
//                allOf(withId(R.id.editText_company_url), isDisplayed()));
//        appCompatAutoCompleteTextView.perform(replaceText("http://"), closeSoftKeyboard());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatAutoCompleteTextView2 = onView(
//                allOf(withId(R.id.editText_company_url), withText("http://"), isDisplayed()));
//        appCompatAutoCompleteTextView2.perform(replaceText("http://jamboreebliss.com/sayarnew/public"), closeSoftKeyboard());
//
//        ViewInteraction floatingActionButton = onView(
//                allOf(withId(R.id.fab_verify_url), isDisplayed()));
//        floatingActionButton.perform(click());
//
//        ViewInteraction appCompatEditText = onView(
//                withId(R.id.input_username));
//        appCompatEditText.perform(scrollTo(), replaceText("sayar_samanta"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText2 = onView(
//                withId(R.id.input_password));
//        appCompatEditText2.perform(scrollTo(), replaceText("home@1234"), closeSoftKeyboard());
//
//        ViewInteraction appCompatButton = onView(
//                allOf(withId(R.id.button_signin), withText("Sign In")));
//        appCompatButton.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(86400000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton.perform(click());
//
//        ViewInteraction linearLayout = onView(
//                withId(R.id.create_ticket));
//        linearLayout.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3590453);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatButton2 = onView(
//                allOf(withId(R.id.buttonSubmit), withText("submit"),
//                        withParent(withId(R.id.toolbar)),
//                        isDisplayed()));
//        appCompatButton2.perform(click());
//
//        ViewInteraction appCompatEditText3 = onView(
//                allOf(withId(R.id.fname_edittext), isDisplayed()));
//        appCompatEditText3.perform(replaceText("sayar"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText4 = onView(
//                allOf(withId(R.id.lname_edittext), isDisplayed()));
//        appCompatEditText4.perform(replaceText("samanta"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText5 = onView(
//                allOf(withId(R.id.email_edittext), isDisplayed()));
//        appCompatEditText5.perform(replaceText("sayarsamanta@gmail.com"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText6 = onView(
//                allOf(withId(R.id.phone_edittext), isDisplayed()));
//        appCompatEditText6.perform(replaceText("8981692638"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText7 = onView(
//                allOf(withId(R.id.phone_edittext10), isDisplayed()));
//        appCompatEditText7.perform(replaceText("9740144475"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText8 = onView(
//                allOf(withId(R.id.sub_edittext), isDisplayed()));
//        appCompatEditText8.perform(replaceText("Testing Ticket"), closeSoftKeyboard());
//
//        ViewInteraction appCompatSpinner = onView(
//                allOf(withId(R.id.spinner_pri), isDisplayed()));
//        appCompatSpinner.perform(click());
//
//        ViewInteraction appCompatCheckedTextView = onView(
//                allOf(withId(android.R.id.text1), withText("Low"), isDisplayed()));
//        appCompatCheckedTextView.perform(click());
//
//        ViewInteraction appCompatSpinner2 = onView(
//                allOf(withId(R.id.spinner_help), isDisplayed()));
//        appCompatSpinner2.perform(click());
//
//        ViewInteraction appCompatCheckedTextView2 = onView(
//                allOf(withId(android.R.id.text1), withText("Support query"), isDisplayed()));
//        appCompatCheckedTextView2.perform(click());
//
//        ViewInteraction appCompatEditText9 = onView(
//                allOf(withId(R.id.msg_edittext), isDisplayed()));
//        appCompatEditText9.perform(replaceText("Want to know the status of the ticket?"), closeSoftKeyboard());
//
//        ViewInteraction searchAutoComplete = onView(
//                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
//                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
//                                withParent(withClassName(is("android.widget.LinearLayout"))))),
//                        isDisplayed()));
//        searchAutoComplete.perform(replaceText("in"), closeSoftKeyboard());
//
//        ViewInteraction appCompatTextView = onView(
//                allOf(withId(android.R.id.text1), withText("India,91"),
//                        childAtPosition(
//                                withId(R.id.listItems),
//                                1),
//                        isDisplayed()));
//        appCompatTextView.perform(click());
//
//        ViewInteraction appCompatButton3 = onView(
//                allOf(withId(R.id.buttonSubmit), withText("submit"),
//                        withParent(withId(R.id.toolbar)),
//                        isDisplayed()));
//        appCompatButton3.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(86400000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction cardView = onView(
//                allOf(withId(R.id.ticket),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed()));
//        cardView.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3530599);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction fab = onView(
//                allOf(withId(R.id.fab), isDisplayed()));
//        fab.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(150);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView2 = onView(
//                allOf(withId(R.id.fab_sheet_item_reply), withText("Reply"), isDisplayed()));
//        appCompatTextView2.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(180);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatEditText10 = onView(
//                allOf(withId(R.id.editText_reply_message),
//                        withParent(allOf(withId(R.id.section_reply),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText10.perform(replaceText("Test reply"), closeSoftKeyboard());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatButton4 = onView(
//                allOf(withId(R.id.button_send), withText("Send"),
//                        withParent(allOf(withId(R.id.section_reply),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatButton4.perform(click());
//
//        ViewInteraction appCompatEditText11 = onView(
//                allOf(withId(R.id.editText_reply_message), withText("Test reply"),
//                        withParent(allOf(withId(R.id.section_reply),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText11.perform(replaceText(""), closeSoftKeyboard());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction fab2 = onView(
//                allOf(withId(R.id.fab), isDisplayed()));
//        fab2.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(150);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView3 = onView(
//                allOf(withId(R.id.fab_sheet_item_note), withText("Internal Notes"), isDisplayed()));
//        appCompatTextView3.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(180);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatEditText12 = onView(
//                allOf(withId(R.id.editText_internal_note),
//                        withParent(allOf(withId(R.id.section_internal_note),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText12.perform(replaceText("Sample Internal Note"), closeSoftKeyboard());
//
//        ViewInteraction appCompatButton5 = onView(
//                allOf(withId(R.id.button_create), withText("Create"),
//                        withParent(allOf(withId(R.id.section_internal_note),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatButton5.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatEditText13 = onView(
//                allOf(withId(R.id.editText_internal_note), withText("Sample Internal Note"),
//                        withParent(allOf(withId(R.id.section_internal_note),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText13.perform(replaceText(""), closeSoftKeyboard());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView4 = onView(
//                allOf(withText("Details"), isDisplayed()));
//        appCompatTextView4.perform(click());
//
//        ViewInteraction appCompatEditText14 = onView(
//                allOf(withId(R.id.editText_subject), withText("Testing Ticket")));
//        appCompatEditText14.perform(scrollTo(), click());
//
//        ViewInteraction appCompatEditText15 = onView(
//                allOf(withId(R.id.editText_subject), withText("Testing Ticket")));
//        appCompatEditText15.perform(scrollTo(), replaceText("Testing Ticket for Test Cases"), closeSoftKeyboard());
//
//        ViewInteraction appCompatSpinner3 = onView(
//                withId(R.id.spinner_priority));
//        appCompatSpinner3.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView3 = onView(
//                allOf(withId(android.R.id.text1), withText("Normal"), isDisplayed()));
//        appCompatCheckedTextView3.perform(click());
//
//        ViewInteraction appCompatSpinner4 = onView(
//                withId(R.id.spinner_type));
//        appCompatSpinner4.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView4 = onView(
//                allOf(withId(android.R.id.text1), withText("Question"), isDisplayed()));
//        appCompatCheckedTextView4.perform(click());
//
//        ViewInteraction appCompatSpinner5 = onView(
//                withId(R.id.spinner_help_topics));
//        appCompatSpinner5.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView5 = onView(
//                allOf(withId(android.R.id.text1), withText("Support query"), isDisplayed()));
//        appCompatCheckedTextView5.perform(click());
//
//        ViewInteraction appCompatSpinner6 = onView(
//                withId(R.id.spinner_source));
//        appCompatSpinner6.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatCheckedTextView6 = onView(
//                allOf(withId(android.R.id.text1), withText("email"), isDisplayed()));
//        appCompatCheckedTextView6.perform(click());
//
//        ViewInteraction appCompatButton6 = onView(
//                allOf(withId(R.id.button_save), withText("Save")));
//        appCompatButton6.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(604800000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton2 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton2.perform(click());
//
////        ViewInteraction relativeLayout = onView(
////                withId(R.id.inbox_tickets));
////        relativeLayout.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(86400000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton3 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton3.perform(click());
//
////        ViewInteraction relativeLayout2 = onView(
////                withId(R.id.my_tickets));
////        relativeLayout2.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(604800000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction cardView2 = onView(
//                allOf(withId(R.id.ticket),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed()));
//        cardView2.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3442398);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView5 = onView(
//                allOf(withText("Conversation"), isDisplayed()));
//        appCompatTextView5.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(86400000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton4 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton4.perform(click());
//
////        ViewInteraction relativeLayout3 = onView(
////                withId(R.id.my_tickets));
////        relativeLayout3.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(604800000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction cardView3 = onView(
//                allOf(withId(R.id.ticket),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed()));
//        cardView3.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(604800000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton5 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton5.perform(click());
//
////        ViewInteraction relativeLayout4 = onView(
////                withId(R.id.unassigned_tickets));
////        relativeLayout4.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(86400000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction cardView4 = onView(
//                allOf(withId(R.id.ticket),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed()));
//        cardView4.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3588870);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView6 = onView(
//                allOf(withText("Details"), isDisplayed()));
//        appCompatTextView6.perform(click());
//
//        ViewInteraction appCompatImageButton6 = onView(
//                allOf(withContentDescription("Navigate up"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appbar)))),
//                        isDisplayed()));
//        appCompatImageButton6.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(86400000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton7 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton7.perform(click());
//
////        ViewInteraction relativeLayout5 = onView(
////                withId(R.id.closed_tickets));
////        relativeLayout5.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(604800000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction cardView5 = onView(
//                allOf(withId(R.id.ticket),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed()));
//        cardView5.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(604800000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView7 = onView(
//                allOf(withText("Details"), isDisplayed()));
//        appCompatTextView7.perform(click());
//
//        ViewInteraction appCompatCheckedTextView7 = onView(
//                allOf(withId(android.R.id.text1), withText("--"), isDisplayed()));
//        appCompatCheckedTextView7.perform(click());
//
//        ViewInteraction appCompatSpinner7 = onView(
//                withId(R.id.spinner_help_topics));
//        appCompatSpinner7.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView8 = onView(
//                allOf(withId(android.R.id.text1), withText("Support query"), isDisplayed()));
//        appCompatCheckedTextView8.perform(click());
//
//        ViewInteraction appCompatImageButton8 = onView(
//                allOf(withContentDescription("Navigate up"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appbar)))),
//                        isDisplayed()));
//        appCompatImageButton8.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(604800000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton9 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton9.perform(click());
//
////        ViewInteraction relativeLayout6 = onView(
////                withId(R.id.trash_tickets));
////        relativeLayout6.perform(scrollTo(), click());
//
//        ViewInteraction appCompatImageButton10 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton10.perform(click());
//
//        ViewInteraction linearLayout2 = onView(
//                withId(R.id.client_list));
//        linearLayout2.perform(scrollTo(), click());
//
//        ViewInteraction relativeLayout7 = onView(
//                allOf(withId(R.id.client),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed()));
//        relativeLayout7.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3576764);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView8 = onView(
//                allOf(withText("CLOSED TICKET"), isDisplayed()));
//        appCompatTextView8.perform(click());
//
//        ViewInteraction appCompatImageButton11 = onView(
//                allOf(withContentDescription("Navigate up"),
//                        withParent(withId(R.id.toolbar)),
//                        isDisplayed()));
//        appCompatImageButton11.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3588467);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton12 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton12.perform(click());
//
//        ViewInteraction linearLayout3 = onView(
//                withId(R.id.settings));
//        linearLayout3.perform(scrollTo(), click());
//
//        ViewInteraction switchCompat = onView(
//                allOf(withId(R.id.switch_crash_reports), isDisplayed()));
//        switchCompat.perform(click());
//
//        ViewInteraction appCompatButton7 = onView(
//                allOf(withId(android.R.id.button2), withText("Cancel")));
//        appCompatButton7.perform(scrollTo(), click());
//
//        ViewInteraction appCompatImageButton13 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton13.perform(click());
//
//        ViewInteraction linearLayout4 = onView(
//                withId(R.id.about));
//        linearLayout4.perform(scrollTo(), click());
//
//        ViewInteraction appCompatButton8 = onView(
//                allOf(withId(R.id.button_website), withText("Website"), isDisplayed()));
//        appCompatButton8.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3572465);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton14 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton14.perform(click());
//
////        ViewInteraction relativeLayout8 = onView(
////                withId(R.id.my_tickets));
////        relativeLayout8.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(604800000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction cardView6 = onView(
//                allOf(withId(R.id.ticket),
//                        withParent(allOf(withId(R.id.cardList),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed()));
//        cardView6.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3544150);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction fab3 = onView(
//                allOf(withId(R.id.fab), isDisplayed()));
//        fab3.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(150);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView9 = onView(
//                allOf(withId(R.id.fab_sheet_item_reply), withText("Reply"), isDisplayed()));
//        appCompatTextView9.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(180);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatEditText16 = onView(
//                allOf(withId(R.id.editText_reply_message),
//                        withParent(allOf(withId(R.id.section_reply),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText16.perform(replaceText("Testing"), closeSoftKeyboard());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatEditText17 = onView(
//                allOf(withId(R.id.editText_reply_message), withText("Testing"),
//                        withParent(allOf(withId(R.id.section_reply),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText17.perform(replaceText("Testing Reply"), closeSoftKeyboard());
//
//        ViewInteraction appCompatButton9 = onView(
//                allOf(withId(R.id.button_send), withText("Send"),
//                        withParent(allOf(withId(R.id.section_reply),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatButton9.perform(click());
//
//        ViewInteraction appCompatEditText18 = onView(
//                allOf(withId(R.id.editText_reply_message), withText("Testing Reply"),
//                        withParent(allOf(withId(R.id.section_reply),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText18.perform(replaceText(""), closeSoftKeyboard());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction fab4 = onView(
//                allOf(withId(R.id.fab), isDisplayed()));
//        fab4.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(150);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView10 = onView(
//                allOf(withId(R.id.fab_sheet_item_note), withText("Internal Notes"), isDisplayed()));
//        appCompatTextView10.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(180);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatEditText19 = onView(
//                allOf(withId(R.id.editText_internal_note),
//                        withParent(allOf(withId(R.id.section_internal_note),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText19.perform(replaceText("Testing Inte"), closeSoftKeyboard());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatEditText20 = onView(
//                allOf(withId(R.id.editText_internal_note), withText("Testing Inte"),
//                        withParent(allOf(withId(R.id.section_internal_note),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText20.perform(replaceText("Testing Internal Note"), closeSoftKeyboard());
//
//        ViewInteraction appCompatButton10 = onView(
//                allOf(withId(R.id.button_create), withText("Create"),
//                        withParent(allOf(withId(R.id.section_internal_note),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatButton10.perform(click());
//
//        ViewInteraction appCompatEditText21 = onView(
//                allOf(withId(R.id.editText_internal_note), withText("Testing Internal Note"),
//                        withParent(allOf(withId(R.id.section_internal_note),
//                                withParent(withId(R.id.reveal)))),
//                        isDisplayed()));
//        appCompatEditText21.perform(replaceText(""), closeSoftKeyboard());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatTextView11 = onView(
//                allOf(withText("Details"), isDisplayed()));
//        appCompatTextView11.perform(click());
//
//        ViewInteraction appCompatEditText22 = onView(
//                allOf(withId(R.id.editText_subject), withText("Testing Ticket for Test Cases")));
//        appCompatEditText22.perform(scrollTo(), click());
//
//        ViewInteraction appCompatEditText23 = onView(
//                allOf(withId(R.id.editText_subject), withText("Testing Ticket for Test Cases")));
//        appCompatEditText23.perform(scrollTo(), replaceText("Testing Ticket for Test Cases From My Tickets."), closeSoftKeyboard());
//
//        ViewInteraction appCompatSpinner8 = onView(
//                withId(R.id.spinner_priority));
//        appCompatSpinner8.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView9 = onView(
//                allOf(withId(android.R.id.text1), withText("High"), isDisplayed()));
//        appCompatCheckedTextView9.perform(click());
//
//        ViewInteraction appCompatSpinner9 = onView(
//                withId(R.id.spinner_type));
//        appCompatSpinner9.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView10 = onView(
//                allOf(withId(android.R.id.text1), withText("Incident"), isDisplayed()));
//        appCompatCheckedTextView10.perform(click());
//
//        ViewInteraction appCompatSpinner10 = onView(
//                withId(R.id.spinner_help_topics));
//        appCompatSpinner10.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView11 = onView(
//                allOf(withId(android.R.id.text1), withText("Sales query"), isDisplayed()));
//        appCompatCheckedTextView11.perform(click());
//
//        ViewInteraction appCompatSpinner11 = onView(
//                withId(R.id.spinner_source));
//        appCompatSpinner11.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView12 = onView(
//                allOf(withId(android.R.id.text1), withText("web"), isDisplayed()));
//        appCompatCheckedTextView12.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatButton11 = onView(
//                allOf(withId(R.id.button_save), withText("Save")));
//        appCompatButton11.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(86400000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction actionMenuItemView = onView(
//                allOf(withId(R.id.action_noti), withContentDescription("Notifications"), isDisplayed()));
//        actionMenuItemView.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(604800000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction cardView7 = onView(
//                allOf(withId(R.id.notification_cardview),
//                        withParent(allOf(withId(R.id.recycler_view),
//                                withParent(withId(R.id.swipeRefresh)))),
//                        isDisplayed()));
//        cardView7.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3574423);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatSpinner12 = onView(
//                withId(R.id.spinner_source));
//        appCompatSpinner12.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView13 = onView(
//                allOf(withId(android.R.id.text1), withText("email"), isDisplayed()));
//        appCompatCheckedTextView13.perform(click());
//
//        ViewInteraction appCompatSpinner13 = onView(
//                withId(R.id.spinner_type));
//        appCompatSpinner13.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView14 = onView(
//                allOf(withId(android.R.id.text1), withText("Question"), isDisplayed()));
//        appCompatCheckedTextView14.perform(click());
//
//        ViewInteraction appCompatSpinner14 = onView(
//                withId(R.id.spinner_priority));
//        appCompatSpinner14.perform(scrollTo(), click());
//
//        ViewInteraction appCompatCheckedTextView15 = onView(
//                allOf(withId(android.R.id.text1), withText("Normal"), isDisplayed()));
//        appCompatCheckedTextView15.perform(click());
//
//        ViewInteraction appCompatEditText24 = onView(
//                allOf(withId(R.id.editText_subject), withText("Testing Ticket for Test Cases From My Tickets.")));
//        appCompatEditText24.perform(scrollTo(), replaceText("Testing Ticket for Test Cases From Notification."), closeSoftKeyboard());
//
//        ViewInteraction appCompatButton12 = onView(
//                allOf(withId(R.id.button_save), withText("Save")));
//        appCompatButton12.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(86400000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatImageButton15 = onView(
//                allOf(withContentDescription("Open"),
//                        withParent(allOf(withId(R.id.toolbar),
//                                withParent(withId(R.id.appBarLayout)))),
//                        isDisplayed()));
//        appCompatImageButton15.perform(click());
//
//        ViewInteraction linearLayout5 = onView(
//                withId(R.id.logout));
//        linearLayout5.perform(scrollTo(), click());
//
//        ViewInteraction appCompatButton13 = onView(
//                allOf(withId(android.R.id.button2), withText("no")));
//        appCompatButton13.perform(scrollTo(), click());
//
//        ViewInteraction linearLayout6 = onView(
//                withId(R.id.logout));
//        linearLayout6.perform(scrollTo(), click());
//
//        ViewInteraction appCompatButton14 = onView(
//                allOf(withId(android.R.id.button1), withText("yes")));
//        appCompatButton14.perform(scrollTo(), click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3260075);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatAutoCompleteTextView3 = onView(
//                allOf(withId(R.id.editText_company_url), isDisplayed()));
//        appCompatAutoCompleteTextView3.perform(replaceText("http://"), closeSoftKeyboard());
//
//    }
//
//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
//    }
//}
