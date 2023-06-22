package me.ceciliosilva.ipass.mealmaster.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.ceciliosilva.ipass.mealmaster.model.Ingredient;
import me.ceciliosilva.ipass.mealmaster.model.MealIngredient;
import me.ceciliosilva.ipass.mealmaster.model.MeasurementUnit;

public class MealIngredientTest {

    private MealIngredient mealIngredient;
    private Ingredient ingredient;

    @BeforeEach
    public void setup() {
        ingredient = new Ingredient("ingredient", "test ingredient", "https://www.test.com");
        mealIngredient = new MealIngredient(100, false, MeasurementUnit.gram, ingredient);
    }

    @Test
    public void getIngredient() {
        // Tests if the ingredient is returned
        assertEquals(ingredient, mealIngredient.getIngredient());
    }

    @Test
    public void getAmount() {
        // Tests if the amount is returned
        assertEquals(100, mealIngredient.getAmount());
    }

    @Test
    public void getIsBought() {
        // Tests if the isBought is returned
        assertEquals(false, mealIngredient.getIsBought());
    }

    @Test
    public void getMeasurementUnit() {
        // Tests if the measurementUnit is returned
        assertEquals(MeasurementUnit.gram, mealIngredient.getMeasurementUnit());
    }

    @Test
    public void equals() {
        // Tests if the mealIngredient is equal to itself
        assertEquals(true, mealIngredient.equals(mealIngredient));
    }

    @Test
    public void equalsSameName() {
        // Tests if the mealIngredient is not equal to another mealIngredient with the
        // same name
        assertEquals(false, mealIngredient.equals(new MealIngredient(100, false, MeasurementUnit.gram, ingredient)));
    }
}
