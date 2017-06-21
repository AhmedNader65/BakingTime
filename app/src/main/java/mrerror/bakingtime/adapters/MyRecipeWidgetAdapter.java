package mrerror.bakingtime.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mrerror.bakingtime.R;
import mrerror.bakingtime.models.Recipe;
import mrerror.bakingtime.widget.ListProvider;

public class MyRecipeWidgetAdapter extends RecyclerView.Adapter<MyRecipeWidgetAdapter.ViewHolder> {
    private final List<Recipe> mValues;
    OnSelectRecipe mListener;

    public MyRecipeWidgetAdapter(List<Recipe> items, OnSelectRecipe mListener) {
        mValues = items;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mRecipeName.setText(mValues.get(position).getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                ListProvider.recipe = mValues.get(position);
                ListProvider.recipeName = mValues.get(position).getName();
                mListener.onWidgetSelectRecipe();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface OnSelectRecipe {
        // TODO: Update argument type and name
        void onWidgetSelectRecipe();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mRecipeName;
        public Recipe mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRecipeName = (TextView) view.findViewById(R.id.name_rec);
        }

    }
}
