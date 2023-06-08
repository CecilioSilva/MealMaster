package me.ceciliosilva.ipass.mealmaster.model;

import java.io.Serializable;

public class MealIngredient implements Serializable {
    private double amount;
    private boolean bought;
    private MeasurementUnit measurementUnit;
    private Ingredient ingredient;

    public MealIngredient(double amount, boolean bought, MeasurementUnit measurementUnit, Ingredient ingredient) {
        this.amount = amount;
        this.bought = bought;
        this.measurementUnit = measurementUnit;
        this.ingredient = ingredient;
    }

}
