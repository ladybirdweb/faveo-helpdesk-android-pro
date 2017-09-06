//package co.helpdesk.faveo.pro.frontend.activities;
//
//
//import android.support.test.espresso.ViewInteraction;
//import android.support.test.espresso.matcher.ViewMatchers;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import co.helpdesk.faveo.pro.R;
//import co.helpdesk.faveo.pro.SplashScreen;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.replaceText;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static org.hamcrest.Matchers.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class SplashScreenTest {
//
//    @Rule
//    public ActivityTestRule<SplashScreen> mActivityTestRule = new ActivityTestRule<>(SplashScreen.class);
//
//    @Test
//    public void splashScreenTest() {
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(3596971);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction appCompatAutoCompleteTextView = onView(
//                allOf(ViewMatchers.withId(R.id.editText_company_url), isDisplayed()));
//        appCompatAutoCompleteTextView.perform(replaceText("http://"), closeSoftKeyboard());
//
//    }
//
//}
