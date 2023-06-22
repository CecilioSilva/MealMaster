package me.ceciliosilva.ipass.mealmaster.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.ceciliosilva.ipass.mealmaster.model.Ingredient;

public class IngredientTest {

    private Ingredient ingredient;

    @BeforeEach
    public void setup() {
        ingredient = new Ingredient("ingredient", "test ingredient", "https://www.test.com");
    }

    @Test
    public void getName() {
        // Tests if the name is returned
        assertEquals("ingredient", ingredient.getName());
    }

    @Test
    public void getDescription() {
        // Tests if the description is returned
        assertEquals("test ingredient", ingredient.getDescription());
    }

    @Test
    public void getImageUrl() {
        // Tests if the imageUrl is returned
        assertEquals("https://www.test.com", ingredient.getImageUrl());
    }

    @Test
    public void equals() {
        // Tests if the ingredient is equal to itself
        assertEquals(true, ingredient.equals(ingredient));
    }

    @Test
    public void equalsSameName() {
        // Tests if the ingredient is not equal to another ingredient with the same
        // name
        assertEquals(false, ingredient.equals(new Ingredient("ingredient", "test ingredient", "https://www.test.com")));
    }

    @Test
    public void uniqueId() {
        // Tests if the ingredient has a unique id
        assertEquals(true,
                ingredient.getId() != new Ingredient("ingredient", "test ingredient", "https://www.test.com").getId());
    }
}
