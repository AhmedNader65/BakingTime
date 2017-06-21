package mrerror.bakingtime;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by ahmed on 21/06/17.
 */

@RunWith(AndroidJUnit4.class)
public class TestRealData {
    private static final String DESCRIPTION_TEXT = "Recipe Introduction" ;
    @Rule
    public ActivityTestRule<RecipesActivity> mActivityRule = new ActivityTestRule<>(
            RecipesActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void fetch_data_from_server() {
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.description)).check(matches(withText(DESCRIPTION_TEXT)));
    }



    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
