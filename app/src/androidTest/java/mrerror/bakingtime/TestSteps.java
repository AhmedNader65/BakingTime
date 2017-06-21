package mrerror.bakingtime;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import mrerror.bakingtime.models.Ingredient;
import mrerror.bakingtime.models.Recipe;
import mrerror.bakingtime.models.Step;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestSteps {
    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(
            StepsActivity.class,false,false);
    private ArrayList<Step> steps;
    private ArrayList<Ingredient> ingredients;

    // check on that recipes recycler shows list of steps correctly
    @Test
    public void fragment_can_be_instantiated() {
        // FAKE DATA
        steps  = new ArrayList<>();
        ingredients  = new ArrayList<>();

        steps.add(new Step(1,"short","desc","",""));
        ingredients.add(new Ingredient(2,"cup","milk"));
        Recipe recipe = new Recipe(1,"example","",ingredients,steps);
        Intent i = new Intent();
        i.putExtra("item", recipe);
        activityRule.launchActivity(i);
        activityRule.getActivity();
        onView(withText("1- short")).check(matches(isDisplayed()));
    }
}
