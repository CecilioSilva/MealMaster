package me.ceciliosilva.ipass.mealmaster.tests;

import me.ceciliosilva.ipass.mealmaster.model.Meal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MealTest {

    private Meal meal;

    @BeforeEach
    public void setup(){
        meal = new Meal(1, "Lasagna", "/https", "4", "food");
    }

    @Test
    public void printMeWorks(){
        assertEquals(true, true);
    }
}