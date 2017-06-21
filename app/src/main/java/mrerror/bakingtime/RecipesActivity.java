package mrerror.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mrerror.bakingtime.fragments.RecipesFragment;
import mrerror.bakingtime.models.Recipe;

public class RecipesActivity extends AppCompatActivity implements RecipesFragment.OnListFragmentInteractionListener {
    public static CountingIdlingResource idlingResource = new CountingIdlingResource("DATA_LOADING");

    public CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Log.e("said","mahmod");
        RecipesFragment recipesFragment;
        if (findViewById(R.id.tablet_view) != null) {
            // Tablet
            recipesFragment = RecipesFragment.newInstance(3);
        } else if (findViewById(R.id.land_view) != null) {
            // Landscape
            recipesFragment = RecipesFragment.newInstance(2);
        } else {
            // Portrait
            recipesFragment = new RecipesFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.list_fragment, recipesFragment).commit();
    }
    // Recipe Fragment Listener

    @Override
    public void onListFragmentInteraction(Recipe item) {
        startActivity(new Intent(this, StepsActivity.class).putExtra("item", item));
    }

}
