package me.ceciliosilva.ipass.mealmaster.model;

public class MealIngredient {
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
