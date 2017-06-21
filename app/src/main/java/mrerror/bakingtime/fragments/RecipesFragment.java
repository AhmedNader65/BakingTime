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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrerror.bakingtime.R;
import mrerror.bakingtime.RecipesActivity;
import mrerror.bakingtime.adapters.MyRecipeRecyclerViewAdapter;
import mrerror.bakingtime.databinding.FragmentRecipeListBinding;
import mrerror.bakingtime.models.Ingredient;
import mrerror.bakingtime.models.Recipe;
import mrerror.bakingtime.models.Step;
import mrerror.bakingtime.volley.NetworkConnection;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipesFragment extends Fragment implements NetworkConnection.OnCompleteFetchingData {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static ArrayList<Recipe> dataArrayList;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyRecipeRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecipesFragment newInstance(int columnCount) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRecipeListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);
        View view = binding.getRoot();
        Context context = view.getContext();
        ((RecipesActivity) getActivity()).idlingResource.increment();

        if (mColumnCount <= 1) {
            binding.list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        } else {
            binding.list.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        dataArrayList = new ArrayList<>();
        new NetworkConnection(this).getData(context);
        adapter = new MyRecipeRecyclerViewAdapter(dataArrayList, mListener);
        binding.list.setAdapter(adapter);
        return view;
    }


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

    @Override
    public void onCompleted(JSONArray result) throws JSONException {
        // DATA BACK FROM THE SERVER
        // PARSE IT
        dataArrayList.clear();
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

            dataArrayList.add(new Recipe(id, name, image_url, ingredientArrayList, stepsArrayList));
        }
        ((RecipesActivity) getActivity()).idlingResource.decrement();
        adapter.notifyDataSetChanged();
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
        void onListFragmentInteraction(Recipe item);
    }


}
