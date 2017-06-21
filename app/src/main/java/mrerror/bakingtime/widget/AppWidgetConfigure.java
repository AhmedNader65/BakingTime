package mrerror.bakingtime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrerror.bakingtime.R;
import mrerror.bakingtime.adapters.MyRecipeWidgetAdapter;
import mrerror.bakingtime.databinding.FragmentRecipeListBinding;
import mrerror.bakingtime.fragments.RecipesFragment;
import mrerror.bakingtime.models.Ingredient;
import mrerror.bakingtime.models.Recipe;
import mrerror.bakingtime.models.Step;
import mrerror.bakingtime.volley.NetworkConnection;

/**
 * Created by ahmed on 21/06/17.
 */

public class AppWidgetConfigure extends Activity implements NetworkConnection.OnCompleteFetchingData
        , MyRecipeWidgetAdapter.OnSelectRecipe {

    private static final String PREFS_NAME = "mrerror.bakingtime.widget.RecipesWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    //    EditText mAppWidgetText;
//    View.OnClickListener mOnClickListener = new View.OnClickListener() {
//        public void onClick(View v) {
//            final Context context = AppWidgetConfigure.this;
//
//            // When the button is clicked, store the string locally
//            String widgetText = mAppWidgetText.getText().toString();
//            saveTitlePref(context, mAppWidgetId, widgetText);
//
//            // It is the responsibility of the configuration activity to update the app widget
////            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
////            (context, appWidgetManager, mAppWidgetId);
//
//            // Make sure we pass back the original appWidgetId
//
//        }
//    };
    private ArrayList<Recipe> dataArrayList;
    private MyRecipeWidgetAdapter adapter;

    public AppWidgetConfigure() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        FragmentRecipeListBinding binding = DataBindingUtil.setContentView(this, R.layout.fragment_recipe_list);
        ;
        View view = binding.getRoot();
        Context context = view.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        if (RecipesFragment.dataArrayList == null || RecipesFragment.dataArrayList.size() == 0) {
            dataArrayList = new ArrayList<>();
            new NetworkConnection(this).getData(context);
        } else {
            dataArrayList = RecipesFragment.dataArrayList;
        }
        binding.list.setLayoutManager(layoutManager);
        adapter = new MyRecipeWidgetAdapter(dataArrayList, this);
        binding.list.setAdapter(adapter);
//        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
//        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

//        mAppWidgetText.setText(loadTitlePref(AppWidgetConfigure.this, mAppWidgetId));
    }

    @Override
    public void onCompleted(JSONArray result) throws JSONException {
        // DATA BACK FROM THE SERVER
        // PARSE IT
        for (int i = 0; i < result.length(); i++) {
            JSONObject recObj = result.getJSONObject(i);
            int id = recObj.getInt("id");
            String name = recObj.getString("name");
            String image_url = recObj.getString("image");
            // Parse the ingredients
            ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
            JSONArray ingredientJsoArr = recObj.getJSONArray("ingredients");
            for (int j = 0; j < ingredientJsoArr.length(); j++) {
                JSONObject ingObj = ingredientJsoArr.getJSONObject(j);
                int quant = ingObj.getInt("quantity");
                String measure = ingObj.getString("measure");
                String ingredient = ingObj.getString("ingredient");
                ingredientArrayList.add(new Ingredient(quant, measure, ingredient));
            }

            //Parse the steps
            ArrayList<Step> stepsArrayList = new ArrayList<>();
            JSONArray stepsJsoArr = recObj.getJSONArray("steps");
            for (int j = 0; j < stepsJsoArr.length(); j++) {
                JSONObject stpObj = stepsJsoArr.getJSONObject(j);
                int stp_id = stpObj.getInt("id");
                String shortDescription = stpObj.getString("shortDescription");
                String description = stpObj.getString("description");
                String videoURL = stpObj.getString("videoURL");
                String thumbnailURL = stpObj.getString("thumbnailURL");
                stepsArrayList.add(new Step(stp_id, shortDescription, description, videoURL, thumbnailURL));
            }

            // add the recipe to recipes list

            dataArrayList.add(new Recipe(id, name, image_url, ingredientArrayList, stepsArrayList));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onWidgetSelectRecipe() {
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}