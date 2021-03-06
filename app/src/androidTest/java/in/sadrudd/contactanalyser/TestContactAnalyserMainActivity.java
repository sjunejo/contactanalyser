package in.sadrudd.contactanalyser;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.sadrudd.contactanalyser.ui.ContactAnalyserMainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by sjunjo on 28/07/15.
 */
@RunWith(AndroidJUnit4.class)
public class TestContactAnalyserMainActivity extends ActivityInstrumentationTestCase2<ContactAnalyserMainActivity> {

    private ContactAnalyserMainActivity contactAnalyserMainActivity;

    public TestContactAnalyserMainActivity(){
        super(ContactAnalyserMainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        contactAnalyserMainActivity = getActivity();
    }

    @Test
    public void testBtnAnalyseCallLogAddsRemoveContactsFragment(){
        ViewInteraction removeContactsFragmentListView = onView(withId(R.id.removecontacts_recycle_view));
        removeContactsFragmentListView.check(ViewAssertions.doesNotExist());
        onView(withId(R.id.btn_analyse_call_log)).perform(click());
        removeContactsFragmentListView.check(ViewAssertions.matches(isDisplayed()));

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
