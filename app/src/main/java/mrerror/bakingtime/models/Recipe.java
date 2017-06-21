package mrerror.bakingtime.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ahmed on 20/06/17.
 */

public class Recipe implements Parcelable {
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    private int id;
    private String name;
    private String image_url;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;

    public Recipe(int id, String name, String image_url,
                  ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image_url = in.readString();
        steps = new ArrayList<>();
        ingredients = new ArrayList<>();
        in.readList(ingredients, getClass().getClassLoader());
        in.readList(steps, getClass().getClassLoader());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image_url);
        dest.writeList(ingredients);
        dest.writeList(steps);
    }
}
