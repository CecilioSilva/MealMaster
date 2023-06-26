package me.ceciliosilva.ipass.mealmaster.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.ceciliosilva.ipass.mealmaster.model.Meal;
import me.ceciliosilva.ipass.mealmaster.model.ShoppingList;
import me.ceciliosilva.ipass.mealmaster.model.Weekday;

public class ShoppingListTest {

    ShoppingList shoppingList;
    Weekday weekday;
    Meal meal;

    @BeforeEach
    public void setup() {
        shoppingList = new ShoppingList(false, "Test");

        meal = new Meal("Lasagna", "food", 4, "/https");
        weekday = new Weekday(LocalDate.now(), meal);
        shoppingList.addWeekMeal(weekday);
    }

    @Test
    public void getIsPublic() {
        // Tests if the checked is returned
        assertEquals(shoppingList.getIsPublic(), false);
    }

    @Test
    public void setChecked() {
        // Tests if the checked is set
        shoppingList.setPublic(true);
        assertEquals(shoppingList.getIsPublic(), true);
    }

    @Test
    public void getName() {
        // Tests if the name is returned
        assertEquals("Test", shoppingList.getName());
    }

    @Test
    public void equalsFalse() {
        // Tests if the equals method works
        ShoppingList shoppingList2 = new ShoppingList(false, "Test");
        assertNotEquals(shoppingList, shoppingList2);
    }

    @Test
    public void addWeekMeal() {
        // Tests if the weekMeal is added
        assertEquals(1, shoppingList.getMeals().size());
    }

    @Test
    public void addSameWeekMeal() {
        // Tests if the same weekMeal is not added
        shoppingList.addWeekMeal(weekday);
        assertEquals(1, shoppingList.getMeals().size());
    }

    @Test
    public void getMeals() {
        // Tests if the meals are returned
        assertEquals(1, shoppingList.getMeals().size());
    }
}
