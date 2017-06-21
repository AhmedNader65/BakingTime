package mrerror.bakingtime;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import mrerror.bakingtime.fragments.DetailsFragment;
import mrerror.bakingtime.fragments.IngredientsFragment;
import mrerror.bakingtime.fragments.StepFragment;
import mrerror.bakingtime.models.Ingredient;
import mrerror.bakingtime.models.Recipe;
import mrerror.bakingtime.models.Step;

public class StepsActivity extends AppCompatActivity implements
        StepFragment.OnListFragmentInteractionListener, FragmentManager.OnBackStackChangedListener
        , DetailsFragment.OnFragmentInteractionListener {
    public static ArrayList<Step> mValues;
    private static OnIngredientsListener mListener;
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean isTwoPanel;

    public static void here(OnIngredientsListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        if (savedInstanceState == null) {
            if (findViewById(R.id.step_layout) != null) {
                isTwoPanel = true;

                StepFragment fragment = StepFragment.newInstance((Recipe) getIntent().getParcelableExtra("item"));
                getSupportFragmentManager().beginTransaction().replace(R.id.left_fragment, fragment).addToBackStack(null).commit();
            } else {
                isTwoPanel = false;
                StepFragment fragment = StepFragment.newInstance((Recipe) getIntent().getParcelableExtra("item"));
                getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment, fragment).addToBackStack(null).commit();
            }
        }
        //Handle when activity is recreated like on orientation Change
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();

    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        if (isTwoPanel) {
            this.finish();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                this.finish();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    // Step Fragment Listener
    @Override
    public void onListFragmentInteraction(ArrayList<Step> items, ArrayList<Ingredient> ingredients, int itemId, int stepsNum) {
        if (items != null) {
            mValues = items;
        }
        if (isTwoPanel) {
            TextView notSelectedTV = (TextView) findViewById(R.id.not_selected);
            notSelectedTV.setVisibility(View.GONE);
            if (items == null) {
                // Ingredients Selection
                mListener.OnIngredientsClick();
                IngredientsFragment fragment = IngredientsFragment.newInstance(ingredients);
                getSupportFragmentManager().beginTransaction().replace(R.id.step_layout, fragment).addToBackStack(null).commit();

            } else {
                // STEP SELECTION
                DetailsFragment fragment = DetailsFragment.newInstance(items.get(itemId), stepsNum);
                getSupportFragmentManager().beginTransaction().replace(R.id.step_layout, fragment).addToBackStack(null).commit();

            }
        } else {
            if (items == null) {
                // Ingredients Selection
                IngredientsFragment fragment = IngredientsFragment.newInstance(ingredients);
                getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment, fragment).addToBackStack(null).commit();
            } else {
                {
                    // STEP SELECTION
                    DetailsFragment fragment = DetailsFragment.newInstance(items.get(itemId), stepsNum);
                    getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment, fragment).addToBackStack(null).commit();
                }
            }
        }
    }

    @Override
    public void onFragmentInteraction(int index) {
        if (!isTwoPanel) {
            DetailsFragment fragment = DetailsFragment.newInstance(mValues.get(index), mValues.size());
            getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment, fragment).commit();
        } else {
            DetailsFragment fragment = DetailsFragment.newInstance(mValues.get(index), mValues.size());
            getSupportFragmentManager().beginTransaction().replace(R.id.step_layout, fragment).addToBackStack(null).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("don't reCreate", 1);

    }

    public interface OnIngredientsListener {
        void OnIngredientsClick();
    }
}
