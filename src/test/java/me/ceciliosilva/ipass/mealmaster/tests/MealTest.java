package me.ceciliosilva.ipass.mealmaster.tests;

import me.ceciliosilva.ipass.mealmaster.model.Ingredient;
import me.ceciliosilva.ipass.mealmaster.model.Meal;
import me.ceciliosilva.ipass.mealmaster.model.MealIngredient;
import me.ceciliosilva.ipass.mealmaster.model.MeasurementUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MealTest {

    private Meal meal;
    private MealIngredient mealIngredient;
    private MealIngredient mealIngredient2;

    @BeforeEach
    public void setup() {
        meal = new Meal("Lasagna", "/https", 4, "food");

        mealIngredient = new MealIngredient(
                1,
                false,
                MeasurementUnit.gram,
                new Ingredient(
                        "Tomato",
                        "Red",
                        "https://image"));
        meal.addIngredient(mealIngredient);

        mealIngredient2 = new MealIngredient(
                1,
                false,
                MeasurementUnit.gram,
                new Ingredient(
                        "Cheese",
                        "Hard milk",
                        "https://image"));
        meal.addIngredient(mealIngredient2);
    }

    @Test
    public void uniqueId() {
        // Tests if the id is unique
        Meal meal2 = new Meal("Lasagna", "/https", 4, "food");
        assertEquals(false, meal.getId().equals(meal2.getId()));
    }

    @Test
    public void addIngredient() {
        // Tests if the ingredient is added to the meal
        assertEquals(2, meal.getIngredients().size());
    }

    @Test
    public void addSameIngredient() {
        // Tests if the same ingredient is not added to the meal
        meal.addIngredient(mealIngredient);
        assertEquals(2, meal.getIngredients().size());
    }

    @Test
    public void getIngredients() {
        // Tests if the ingredients are returned
        assertEquals(2, meal.getIngredients().size());
    }
}