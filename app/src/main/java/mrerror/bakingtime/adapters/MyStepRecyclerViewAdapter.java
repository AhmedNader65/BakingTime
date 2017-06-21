package mrerror.bakingtime.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mrerror.bakingtime.R;
import mrerror.bakingtime.StepsActivity;
import mrerror.bakingtime.fragments.StepFragment.OnListFragmentInteractionListener;
import mrerror.bakingtime.models.Step;

public class MyStepRecyclerViewAdapter extends RecyclerView.Adapter<MyStepRecyclerViewAdapter.ViewHolder>
        implements StepsActivity.OnIngredientsListener {

    private final ArrayList<Step> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context context;
    private int selected_position = -1;

    public MyStepRecyclerViewAdapter(ArrayList<Step> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_step, parent, false);
        context = parent.getContext();
        StepsActivity.here(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mStepView.setText((position + 1) + "- " + mValues.get(position).getShortDescription());
        if (selected_position == position) {
            // Here I am just highlighting the background
            holder.itemView.setBackgroundColor(Color.GREEN);
        } else {
            holder.itemView.setBackgroundColor(0x4f000000);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(mValues, null, position, mValues.size());
                    updateHighLight();
                    selected_position = position;
                    updateHighLight();
                }
            }
        });
    }

    private void updateHighLight() {
        notifyItemChanged(selected_position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void OnIngredientsClick() {
        updateHighLight();
        selected_position = -1;
        updateHighLight();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mStepView;
        public Step mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mStepView = (TextView) view.findViewById(R.id.step);
        }

    }


}
