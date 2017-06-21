package mrerror.bakingtime.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mrerror.bakingtime.R;
import mrerror.bakingtime.fragments.RecipesFragment.OnListFragmentInteractionListener;
import mrerror.bakingtime.models.Recipe;

public class MyRecipeRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipeRecyclerViewAdapter.ViewHolder> {

    private final List<Recipe> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context context ;
    public MyRecipeRecyclerViewAdapter(List<Recipe> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        Log.e("data size", mValues.size() + "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mRecipeName.setText(mValues.get(position).getName());
        if(mValues.get(position).getImage_url()!=null&&mValues.get(position).getImage_url().length()>0) {
            Picasso.with(context).load(mValues.get(position).getImage_url())
                    .placeholder(R.drawable.recipe_default_img).into(holder.mRecipeImg);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircleImageView mRecipeImg;
        public final TextView mRecipeName;
        public Recipe mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRecipeName = (TextView) view.findViewById(R.id.name_rec);
            mRecipeImg = (CircleImageView) view.findViewById(R.id.recipe_image);
        }
    }
}
