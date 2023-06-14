package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MealIngredient implements Serializable {
    static final long serialVersionUID = 1L;

    private String id;
    private double amount;
    private boolean bought;
    private MeasurementUnit measurementUnit;
    private Ingredient ingredient;

    public MealIngredient(double amount, boolean bought, MeasurementUnit measurementUnit, Ingredient ingredient) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.bought = bought;
        this.measurementUnit = measurementUnit;
        this.ingredient = ingredient;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", this.id);
        map.put("amount", this.amount);
        map.put("bought", this.bought);
        map.put("measurementUnit", this.measurementUnit);
        map.put("ingredient", this.ingredient.toMap());
        return map;
    }
}
