package me.ceciliosilva.ipass.mealmaster.tests;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.ceciliosilva.ipass.mealmaster.model.Meal;
import me.ceciliosilva.ipass.mealmaster.model.Weekday;

public class WeekdayTest {

    private Weekday weekday;
    private Meal meal;

    @BeforeEach
    public void setup() {
        meal = new Meal("Lasagna", "food", 4, "/https");
        weekday = new Weekday(LocalDate.now(), meal);
    }

    @Test
    public void getMeal() {
        // Tests if the meal is returned
        assert weekday.getMeal().equals(meal);
    }

    @Test
    public void getDate() {
        // Tests if the date is returned
        assert weekday.getDate().equals(LocalDate.now());
    }

}
