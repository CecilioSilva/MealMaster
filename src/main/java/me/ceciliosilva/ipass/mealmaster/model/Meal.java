package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Meal implements Serializable {
    static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String image;
    private Integer numberOfPeople;
    private String description;
    private ArrayList<MealIngredient> ingredients = new ArrayList<>();

    public Meal(String name, String image, Integer numberOfPeople, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.image = image;
        this.numberOfPeople = numberOfPeople;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public void addIngredient(MealIngredient ingredient) {
        if (!this.ingredients.contains(ingredient)) {
            this.ingredients.add(ingredient);
        }
    }

    public ArrayList<MealIngredient> getIngredients() {
        // Returns the ingredients of the meal
        return (ArrayList<MealIngredient>) this.ingredients.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Meal meal = (Meal) o;
        return Objects.equals(id, meal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", this.id);
        map.put("name", this.name);
        map.put("description", this.description);
        map.put("numberOfPeople", this.numberOfPeople);
        map.put("image", this.image);

        ArrayList<HashMap<String, Object>> ings = new ArrayList<>();
        for (MealIngredient ing : this.ingredients) {
            ings.add(ing.toMap());
        }
        map.put("ingredients", ings);
        return map;
    }
}
