package eu.tesseractsoft.mintimedialog.sample;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import eu.tesseractsoft.mintimedialog.MinTimeDialog;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Context context;

    @Rule
    public ActivityTestRule<TestingActivity> mActivityRule = new ActivityTestRule<>(TestingActivity.class);

    @Before
    public void setUp() throws Exception {
        context = mActivityRule.getActivity().getBaseContext();
    }

    @Test
    public void useAppContext() throws Exception {
        assertEquals("eu.tesseractsoft.mintimedialog.sample", context.getPackageName());
    }

    @Test
    public void testShow()  throws Exception{
        onView(withId(R.id.btnShow)).check(matches(isDisplayed()));
        onView(withId(R.id.btnShow)).perform(click());
    }
}
