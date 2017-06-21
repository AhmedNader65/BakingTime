package mrerror.bakingtime.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mrerror.bakingtime.R;
import mrerror.bakingtime.models.Ingredient;

public class MyIngredientsRecyclerViewAdapter extends RecyclerView.Adapter<MyIngredientsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Ingredient> mValues;

    public MyIngredientsRecyclerViewAdapter(ArrayList<Ingredient> ingredients) {
        mValues = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredients, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Ingredient in = mValues.get(position);
        int quantity = in.getQuantity();
        String measure = in.getMeasure();
        String name = in.getName();
        holder.mNameView.setText(quantity + " " + measure + " " + name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name_ing);
        }

    }
}
