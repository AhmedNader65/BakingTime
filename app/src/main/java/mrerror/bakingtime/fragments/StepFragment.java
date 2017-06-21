package mrerror.bakingtime.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mrerror.bakingtime.R;
import mrerror.bakingtime.adapters.MyStepRecyclerViewAdapter;
import mrerror.bakingtime.databinding.FragmentStepListBinding;
import mrerror.bakingtime.models.Ingredient;
import mrerror.bakingtime.models.Recipe;
import mrerror.bakingtime.models.Step;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StepFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_RECIPE_KEY = "recipe";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Recipe mCurrentRecipe;
    private OnListFragmentInteractionListener mListener;
    private MyStepRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StepFragment newInstance(Recipe recipe) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE_KEY, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCurrentRecipe = getArguments().getParcelable(ARG_RECIPE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentStepListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_list, container, false);
        View view = binding.getRoot();
        Context context = view.getContext();
        // Set the adapter
        if (mColumnCount <= 1) {
            binding.list.setLayoutManager(new LinearLayoutManager(context));
        } else {
            binding.list.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapter = new MyStepRecyclerViewAdapter(mCurrentRecipe.getSteps(), mListener);
        binding.list.setAdapter(adapter);
        binding.ingredientCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentInteraction(null, mCurrentRecipe.getIngredients(), 0, 0);
            }
        });
        return view;
    }

    //    public static void updateSelected(){
//        adapter.updateHighLight();
//    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ArrayList<Step> items, ArrayList<Ingredient> ingredients, int itemId, int StepsNum);
    }


}
