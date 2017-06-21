package mrerror.bakingtime.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.util.ArrayList;

import mrerror.bakingtime.R;
import mrerror.bakingtime.StepsActivity;
import mrerror.bakingtime.models.Ingredient;
import mrerror.bakingtime.models.Recipe;

/**
 * Created by ahmed on 21/06/17.
 */

public class ListProvider implements RemoteViewsFactory {
    public static Recipe recipe;
    public static String recipeName;
    public ArrayList<Ingredient> items;
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        if (recipe != null) {
            items = recipe.getIngredients();
            items.add(0, new Ingredient(0, null, recipeName));
        }
//        populateListItem();
    }


    @Override
    public int getCount() {

        return items == null ? 0 : items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
    *Similar to getView of Adapter where instead of View
    *we return RemoteViews
    *
    */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_ingredients);

        if (position == 0) {
            remoteView.setInt(R.id.name_ing, "setBackgroundColor",
                    android.graphics.Color.RED);
            remoteView.setTextViewText(R.id.name_ing, items.get(position).getName());
        } else {
            remoteView.setInt(R.id.name_ing, "setBackgroundColor",
                    0x817ba237);
            remoteView.setTextViewText(R.id.name_ing, items.get(position).getQuantity()
                    + " " + items.get(position).getMeasure() + " " + items.get(position).getName());
        }
        Intent i = new Intent(context, StepsActivity.class);
        i.putExtra("item", recipe);
        remoteView.setOnClickFillInIntent(R.id.widget_layout_parent, i);
//        ListItem listItem = listItemList.get(position);
//        remoteView.setTextViewText(R.id.name_rec, listItem.heading);
//        remoteView.setTextViewText(R.id.content, listItem.content);

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }
}
